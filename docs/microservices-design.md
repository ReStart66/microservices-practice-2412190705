# 汽车维修系统微服务拆分设计

## 拆分目标

保留仓库中的 `monolith/` 作为单体版本，在 `services/` 下拆出三个独立 Spring Boot 服务。每个服务拥有独立 Maven 工程、端口、数据库、账号和数据表，只通过 HTTP API 获取其他服务的信息。

## 服务边界

| 微服务 | 端口 | 独立数据库 | 数据表 | 职责 |
|---|---:|---|---|---|
| `vehicle-service` | 8081 | `vehicle_db` | `vehicles` | 车辆建档、车辆查询、车牌号唯一性 |
| `part-service` | 8083 | `part_db` | `parts` | 配件档案、库存查询、库存预留 |
| `work-order-service` | 8082 | `work_order_db` | `work_orders` | 维修工单、状态流转、跨服务流程编排 |

## 数据所有权

- `vehicle-service` 只访问 `vehicles`。
- `part-service` 只访问 `parts`。
- `work-order-service` 只访问 `work_orders`。
- `work_orders.vehicle_id` 与 `work_orders.part_id` 是逻辑引用，不建立跨库外键。
- 任何服务都不能直接访问其他服务的 Repository 或数据库表。

## 核心业务流程

1. 创建车辆。
2. 创建配件并设置库存。
3. 创建维修工单，提交 `vehicleId`、`partId` 和 `partQuantity`。
4. 工单服务先检查该车辆是否已有活跃工单。
5. 工单服务通过 HTTP 查询车辆服务，车辆不存在返回 404。
6. 工单服务通过 HTTP 请求配件服务预留库存，配件不存在返回 404，库存不足返回 409。
7. 工单服务保存本地工单，只保存车辆 ID、配件 ID 和数量。

## 服务调用关系

```mermaid
flowchart LR
    Client[Client] --> V[vehicle-service :8081]
    Client --> P[part-service :8083]
    Client --> W[work-order-service :8082]

    W -->|GET /api/vehicles/{id}| V
    W -->|POST /api/parts/{id}/reservations| P

    V --> VDB[(vehicle_db)]
    P --> PDB[(part_db)]
    W --> WDB[(work_order_db)]
```

## HTTP 调用配置

`work-order-service` 使用 `RestTemplate`，并配置：

- 连接超时：3 秒
- 读取超时：5 秒
- `VEHICLE_SERVICE_URL` 默认 `http://localhost:8081`
- `PART_SERVICE_URL` 默认 `http://localhost:8083`

Compose 环境覆盖为：

```text
VEHICLE_SERVICE_URL=http://vehicle-service:8081
PART_SERVICE_URL=http://part-service:8083
```

不能使用 `localhost` 访问其他容器。

## 失败处理

| 场景 | HTTP 状态 |
|---|---|
| 本地资源不存在 | 404 |
| 车辆不存在 | 404 |
| 配件不存在 | 404 |
| 配件库存不足 | 409 |
| 车辆已有活跃工单 | 409 |
| 依赖服务超时或不可用 | 503 |

## 一致性边界

本周采用顺序 HTTP 调用，不是分布式事务：

- `@Transactional` 只回滚本地数据库。
- 配件服务成功预留库存后，如果工单服务保存本地工单失败，可能出现“远端已预留、本地无工单”的窗口。
- 本周不实现消息、补偿、幂等去重或分布式事务，只做明确的失败处理和一致性说明。
