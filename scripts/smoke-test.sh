#!/usr/bin/env bash
set -euo pipefail

VEHICLE_URL="${VEHICLE_URL:-http://localhost:8081}"
WORK_ORDER_URL="${WORK_ORDER_URL:-http://localhost:8082}"
PART_URL="${PART_URL:-http://localhost:8083}"
TMP_DIR="${TMPDIR:-/tmp}/maintenance-smoke-$$"
mkdir -p "$TMP_DIR"
trap 'rm -rf "$TMP_DIR"' EXIT

fail() {
  echo "FAIL: $*" >&2
  exit 1
}

health() {
  local name="$1"
  local url="$2"
  local body
  body=$(curl -fsS "$url/actuator/health") || fail "$name health request failed"
  echo "$name health: $body"
  echo "$body" | grep -q '"status":"UP"' || fail "$name health is not UP"
}

request() {
  local method="$1"
  local url="$2"
  local output="$3"
  local body="${4:-}"
  local status
  if [ -n "$body" ]; then
    status=$(curl -sS -o "$output" -w '%{http_code}' -X "$method" "$url" \
      -H 'Content-Type: application/json' -d "$body")
  else
    status=$(curl -sS -o "$output" -w '%{http_code}' -X "$method" "$url")
  fi
  printf '%s' "$status"
}

extract_id() {
  sed -n 's/.*"id":"\([^"]*\)".*/\1/p' "$1" | head -n 1
}

echo '=== health checks ==='
health vehicle-service "$VEHICLE_URL"
health part-service "$PART_URL"
health work-order-service "$WORK_ORDER_URL"

suffix=$(date +%s)
vehicle_body=$(printf '{"ownerName":"Smoke Tester","plateNumber":"W06-%s","brand":"Volkswagen","model":"Magotan","mileage":60000}' "$suffix")
status=$(request POST "$VEHICLE_URL/api/vehicles" "$TMP_DIR/vehicle.json" "$vehicle_body")
[ "$status" = "201" ] || { cat "$TMP_DIR/vehicle.json"; fail "create vehicle expected 201, got $status"; }
vehicle_id=$(extract_id "$TMP_DIR/vehicle.json")
[ -n "$vehicle_id" ] || fail "vehicle id not found"
echo "vehicle created: $vehicle_id"

part_body=$(printf '{"code":"PART-%s","name":"Oil Filter","stockQuantity":5}' "$suffix")
status=$(request POST "$PART_URL/api/parts" "$TMP_DIR/part.json" "$part_body")
[ "$status" = "201" ] || { cat "$TMP_DIR/part.json"; fail "create part expected 201, got $status"; }
part_id=$(extract_id "$TMP_DIR/part.json")
[ -n "$part_id" ] || fail "part id not found"
echo "part created: $part_id"

order_body=$(printf '{"vehicleId":"%s","partId":"%s","partQuantity":2,"serviceAdvisorName":"Advisor","technicianName":"Technician","faultDescription":"Cross-service smoke test"}' "$vehicle_id" "$part_id")
status=$(request POST "$WORK_ORDER_URL/api/work-orders" "$TMP_DIR/order.json" "$order_body")
[ "$status" = "201" ] || { cat "$TMP_DIR/order.json"; fail "create work order expected 201, got $status"; }
order_id=$(extract_id "$TMP_DIR/order.json")
[ -n "$order_id" ] || fail "work order id not found"
echo "work order created: $order_id"

status=$(request POST "$WORK_ORDER_URL/api/work-orders" "$TMP_DIR/duplicate.json" "$order_body")
[ "$status" = "409" ] || fail "duplicate active work order expected 409, got $status"
echo "duplicate active work order: 409"

missing_body='{"vehicleId":"missing-vehicle","partId":"missing-part","partQuantity":1,"serviceAdvisorName":"Advisor","technicianName":"Technician","faultDescription":"Missing dependency test"}'
status=$(request POST "$WORK_ORDER_URL/api/work-orders" "$TMP_DIR/missing.json" "$missing_body")
[ "$status" = "404" ] || fail "missing vehicle expected 404, got $status"
echo "missing vehicle: 404"

status=$(request GET "$PART_URL/api/parts/$part_id" "$TMP_DIR/part-read.json")
[ "$status" = "200" ] || fail "part query expected 200, got $status"
grep -q '"reservedQuantity":2' "$TMP_DIR/part-read.json" || { cat "$TMP_DIR/part-read.json"; fail "reserved quantity is not 2"; }
echo "part reservation verified: quantity=2"

status=$(request GET "$WORK_ORDER_URL/api/work-orders/$order_id" "$TMP_DIR/order-read.json")
[ "$status" = "200" ] || fail "work order query expected 200, got $status"
grep -q '"status":"CREATED"' "$TMP_DIR/order-read.json" || { cat "$TMP_DIR/order-read.json"; fail "work order status is not CREATED"; }
echo "work order query verified: CREATED"
echo 'SMOKE TEST PASSED'