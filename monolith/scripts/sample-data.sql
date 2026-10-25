-- 在应用首次启动、JPA 建表完成后执行。
INSERT INTO vehicles (id, owner_name, plate_number, brand, model, mileage, created_at)
VALUES ('11111111-1111-1111-1111-111111111111', '陆兵尧', '浙A12345', '大众', '迈腾', 52000, CURRENT_TIMESTAMP(6))
ON DUPLICATE KEY UPDATE
  owner_name = VALUES(owner_name),
  brand = VALUES(brand),
  model = VALUES(model),
  mileage = VALUES(mileage);

INSERT INTO work_orders (
  id, vehicle_id, service_advisor_name, technician_name,
  fault_description, status, created_at, completed_at
)
VALUES (
  '22222222-2222-2222-2222-222222222222',
  '11111111-1111-1111-1111-111111111111',
  '王顾问', '李技师', '发动机异响', 'CREATED', CURRENT_TIMESTAMP(6), NULL
)
ON DUPLICATE KEY UPDATE
  service_advisor_name = VALUES(service_advisor_name),
  technician_name = VALUES(technician_name),
  fault_description = VALUES(fault_description),
  status = VALUES(status),
  completed_at = VALUES(completed_at);
