# 第 04 周作业记录

## 业务建模

本周选择汽车维修保养管理系统中的“车辆建档并创建维修工单”流程。

核心实体为 `Vehicle` 和 `WorkOrder`，关系是一辆车辆可以拥有多张维修工单，每张工单必须关联一辆车辆。车辆保存车主、车牌号、品牌、车型和里程；工单保存车辆、服务顾问、维修技师、故障描述、状态及时间信息。

Service 业务规则：同一车辆存在 `CREATED` 或 `IN_PROGRESS` 工单时，不允许再创建新的活跃工单；工单完成后才允许创建下一次维修工单。

运行命令：

```powershell
cd monolith
.\mvnw.cmd test
```

结果：业务模型和实体关系已落地，相关测试通过。

## 单体应用实现

新增分层代码：

- `model`：`Vehicle`、`WorkOrder`、`WorkOrderStatus`
- `dto`：车辆和工单的创建请求、响应对象
- `repository`：Spring Data JPA Repository
- `service`：`VehicleService`、`WorkOrderService`
- `controller`：`VehicleController`、`WorkOrderController`
- `exception`：统一 `ProblemDetail` 错误响应

REST API：

- `POST /api/vehicles`
- `GET /api/vehicles`
- `GET /api/vehicles/{id}`
- `POST /api/work-orders`
- `GET /api/work-orders`
- `GET /api/work-orders/{id}`
- `PATCH /api/work-orders/{id}/complete`

Controller 只接收 DTO、执行校验并转换响应；业务判断和事务位于 Service。

运行命令：

```powershell
cd monolith
.\mvnw.cmd spring-boot:run
```

可使用根目录 README 中的 `curl.exe` 示例验证创建和查询接口。

## 数据库持久化

数据库设计见根目录 `docs/database.md`。应用使用 MySQL 8.4 和 Spring Data JPA：

```text
数据库：maintenance
用户：maintenance
表：vehicles、work_orders
```

主要配置位于 `monolith/src/main/resources/application.yml`，通过 `DB_URL`、`DB_USER`、`DB_PASSWORD` 环境变量覆盖。

MySQL 初始化脚本：`monolith/scripts/mysql-init.sql`。  
可重复样例数据：`monolith/scripts/sample-data.sql`。

测试配置位于 `monolith/src/test/resources/application.yml`，使用 H2 内存数据库和 `create-drop`，不依赖本机 MySQL。

运行命令：

```powershell
cd monolith
$env:DB_PASSWORD = '你的数据库密码'
.\mvnw.cmd spring-boot:run
```

## 测试与验证

测试分为三层：

1. `VehicleRepositoryTest`：验证 Repository 保存、车牌号查询、唯一约束和工单关系。
2. `WorkOrderServiceTest`：验证活跃工单冲突、完成后再次建单、车辆不存在和按车辆查询。
3. `WorkOrderControllerTest`：验证创建成功 201、输入无效 400、业务冲突 409、资源不存在 404。

执行：

```powershell
cd monolith
.\mvnw.cmd test
```

本次实际结果：

```text
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

测试数据由各测试方法通过 Service 或 Repository 创建，事务回滚或 H2 内存库销毁后不会影响其他测试，可重复执行。

### REST API 实际验证

使用 H2 测试配置启动应用后，实际调用流程如下：

```text
POST /api/vehicles                          -> 201，创建车辆成功
POST /api/work-orders                       -> 201，状态 CREATED
再次 POST 同一车辆的活跃工单                 -> 409，业务规则生效
PATCH /api/work-orders/{id}/complete        -> 200，状态 COMPLETED
完成工单后再次 POST /api/work-orders         -> 201，允许创建下一次维修工单
GET /api/work-orders?vehicleId={vehicleId}  -> 200，返回 2 条工单
GET /actuator/health                        -> 200，status=UP
```

验证证明 Controller、DTO、Service、Repository 和数据库映射已经连通。

### 真实 MySQL 持久化验证

本机 MySQL 服务 `MySQL80` 已启动，实际使用版本为 MySQL `8.0.43`，数据库名为 `maintenance`。

通过 REST API 创建车辆和维修工单后，在 MySQL 中确认存在 `vehicles`、`work_orders` 两张表及对应记录。完成工单后状态保存为 `COMPLETED`。

随后停止并重新启动 Spring Boot 应用，再次查询接口：

```text
车辆数量：1
工单数量：1
工单状态：COMPLETED
健康检查：UP
```

重启后仍能读到相同车辆和工单，说明 JPA 数据已持久化到 MySQL，而不是保存在应用内存中。
