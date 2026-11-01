# 第 06 周作业记录

## 服务拆分设计

本周保留 `monolith/`，在 `services/` 下建立三个独立服务：

- `vehicle-service`：8081，`vehicle_db`
- `work-order-service`：8082，`work_order_db`
- `part-service`：8083，`part_db`

拆分依据是业务能力和数据所有权，而不是 Controller、Service、Repository 技术层。工单服务只保存 `vehicleId`、`partId`，不建立跨库外键，也不访问其他服务的 Repository。

详细设计和 Mermaid 架构图位于：

```text
docs/microservices-design.md
```

## 代码拆分

三个服务均为独立 Maven / Spring Boot 4.0.8 / Java 25 工程：

```text
services/vehicle-service
services/part-service
services/work-order-service
```

每个服务均包含独立 `pom.xml`、配置文件、Controller、Service、Repository、DTO、错误处理和测试。

`work-order-service` 通过 `RestTemplate` 完成两个真实 HTTP 调用：

```text
GET  /api/vehicles/{id}
POST /api/parts/{id}/reservations
```

连接超时 3 秒，读取超时 5 秒。车辆或配件不存在返回 404，库存不足返回 409，依赖服务不可用或超时返回 503。

测试命令：

```powershell
cd services/vehicle-service
mvn test

cd ../part-service
mvn test

cd ../work-order-service
mvn test
```

测试使用 H2，不依赖 MySQL 或其他运行中的服务。实际测试结果：

```text
vehicle-service:    3 tests
part-service:       3 tests
work-order-service: 10 tests
TOTAL:              16 tests
Failures: 0, Errors: 0
```

## 本地编排

根目录 `compose.yaml` 启动：

```text
vehicle-db
part-db
work-order-db
vehicle-service
part-service
work-order-service
```

每个应用使用自己的数据库、账号和命名卷：

```text
vehicle-data
part-data
work-order-data
```

容器间使用 Compose 服务名通信：

```text
vehicle-service -> vehicle-db:3306
part-service -> part-db:3306
work-order-service -> work-order-db:3306
work-order-service -> vehicle-service:8081
work-order-service -> part-service:8083
```

启动命令：

```powershell
docker compose up -d --build --remove-orphans
docker compose ps
```

数据隔离检查：

```text
vehicle_db     -> vehicles
part_db        -> parts
work_order_db  -> work_orders
```

## 验证

仓库根目录运行：

```bash
bash scripts/smoke-test.sh
```

脚本会执行：

1. 检查三个服务的 `/actuator/health`。
2. 创建车辆和配件。
3. 创建维修工单并触发车辆、配件两次 HTTP 调用。
4. 验证配件预留数量为 2。
5. 验证重复活跃工单返回 409。
6. 验证不存在的车辆返回 404。
7. 查询工单并确认状态为 CREATED。

实际 smoke test 结果：

```text
vehicle-service health: UP
part-service health: UP
work-order-service health: UP
duplicate active work order: 409
missing vehicle: 404
part reservation verified: quantity=2
work order query verified: CREATED
SMOKE TEST PASSED
```

## 一致性说明

本流程采用顺序 HTTP 调用。配件服务成功预留库存后，如果工单服务本地保存失败，会出现“远端成功、本地失败”的窗口。`@Transactional` 只覆盖工单服务自己的 `work_order_db`，不能回滚配件服务。

本周不实现跨服务原子事务、消息补偿或自动回滚。

## 截图

- 服务代码结构：`screenshots/01-service-structure.png`\n- 服务 HTTP 边界代码：`screenshots/01b-service-http-code.png`
- Compose 启动状态：`screenshots/02-compose-status.png`
- 跨服务成功流程：`screenshots/03-cross-service-success.png`
- 依赖资源不存在 404：`screenshots/04-missing-resource.png`
- 三个服务测试结果：`screenshots/05-tests-success.png`


![服务代码结构](<screenshots/01-service-structure.png>)\n

![Compose 启动状态](<screenshots/02-compose-status.png>)

![跨服务成功流程](<screenshots/03-cross-service-success.png>)

![依赖资源不存在](<screenshots/04-missing-resource.png>)

![微服务测试结果](<screenshots/05-tests-success.png>)
