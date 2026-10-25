# 汽车维修保养管理系统

面向汽车维修保养门店的预约、工单、配件库存和收费管理系统。


## 项目简介

本系统用于管理汽车维修保养门店的主要业务，包括客户和车辆档案、预约接车、维修工单、配件使用、费用结算、收款和取车提醒。

系统面向单店或小型连锁维修门店，帮助门店提高维修工单流转、配件库存管理和收费结算效率，同时为车主提供预约、维修进度查询和售后提醒服务。

## 项目背景

中小型汽车维修保养门店通常依赖纸质工单、微信和 Excel 管理业务，存在预约冲突、维修进度难追踪、配件库存不准和收费对账困难等问题。
本项目旨在建设一套统一的汽车维修保养管理系统，覆盖客户车辆、预约接车、维修工单、配件库存、费用结算和售后提醒，提高门店运营效率和客户体验，并支持后续微服务、认证、消息、监控和部署等课程扩展。

## 目标用户

车主： 维护车辆信息、预约维修、查看维修进度、查看账单、接收取车和保养提醒 
服务顾问： 接车登记、确认预约、创建工单、管理配件库存、生成账单、联系车主 
维修技师： 查看工单、检测车辆、填写维修记录、申请配件、提交完工结果 
学徒： 查看分配给自己的任务、记录操作进度、申请技师协助、学习维修流程 
收银员：查看待结算账单、记录收款、处理退款、查询收费记录 

### 选择原因

1. 业务场景明确，具有真实门店需求。
2. 包含车主、服务顾问、维修技师、学徒和收银员多个角色。
3. 覆盖预约、维修、配件、结算和通知的完整流程。
4. 适合练习事务、库存并发、消息、权限、监控和部署。
5. 模块清晰，适合持续演进。

### 项目边界

当前只设计单店场景，暂不实现多门店、真实支付、保险理赔、车联网和 AI 诊断。

### 功能模块

- 账号与权限
- 客户与车辆
- 预约与接车
- 维修工单
- 技师与学徒协作
- 配件与库存
- 报价与结算
- 消息与提醒
- 经营统计

### 核心流程

车主预约，服务顾问接车并创建工单，维修技师检测维修，学徒协助，服务顾问管理配件出库，维修完成后生成账单，收银员收款，系统通知车主取车。

### 核心业务流程

**1. 预约与接车**

车主选择车辆、服务项目和时间，服务顾问确认预约。车辆到店后，服务顾问完成接车检查并创建维修工单。

**2. 检测与维修**

维修技师检测车辆并确定维修项目，需要时分配学徒协助。维修过程中持续更新工单状态和维修记录。

**3. 配件与库存**

如果需要配件，由服务顾问办理配件出库并扣减库存。库存不足时，工单进入“待配件”状态。

**4. 完工与结算**

维修完成后，技师提交完工验收，服务顾问生成费用账单，收银员完成收款。

**5. 交车与提醒**

系统通知车主取车，保存维修历史，并生成下次保养或年检提醒。

## 项目范围

当前支持单店汽修门店的客户、车辆、预约、维修工单、技师学徒协作、配件库存、报价结算、消息提醒和经营统计。

## 暂不包含的服务

暂不实现多门店平台、保险理赔、车联网、手机客户端和完整供应链。


# 微服务课程作业仓库

- 课程：微服务
- 姓名：陆兵尧
- 学号：2412190705
- 班级：计科2403
- GitHub：ReStart66
- 仓库用途：用于完成微服务课程的需求分析、项目实现、测试、部署和文档整理。
## 第 3 周：Spring Boot 工程运行说明

### Java 与 Maven 要求

- Java：25
- Spring Boot：4.0.8
- Maven Wrapper：项目内置
- Maven 工程目录：`monolith/`

### 启动命令

Windows PowerShell：

```powershell
cd monolith
.\mvnw.cmd spring-boot:run
```

Git Bash：

```bash
cd monolith
./mvnw spring-boot:run
```

默认端口：`8080`。

### 测试命令

Windows PowerShell：

```powershell
cd monolith
.\mvnw.cmd test
```

Git Bash：

```bash
cd monolith
./mvnw test
```

### 接口地址

问候接口：

```text
GET http://localhost:8080/api/hello
```

健康检查：

```text
GET http://localhost:8080/actuator/health
```

健康检查正常时返回 `UP`。

### 当前尚未实现的业务能力

本周只完成 Spring Boot 工程基础框架、运行接口、健康检查和启动测试，暂未实现：

- 数据库持久化
- 业务模型和表结构
- Service 和 Repository
- 完整 REST API
- 用户认证和权限
- 预约、维修工单、配件库存和结算等业务功能


## 第 4 周：汽车维修工单单体应用

本周围绕“车辆建档并创建维修工单”实现模型、Service、REST API、JPA Repository 和 MySQL 持久化。工程位置仍为 `monolith/`。

### 运行环境

- Java 25
- Spring Boot 4.0.8
- Maven Wrapper
- MySQL 8.x（本机验证版本 8.0.43）
- H2（自动化测试）

### MySQL 初始化

以 MySQL 管理员身份执行 `monolith/scripts/mysql-init.sql`，并先修改脚本中的示例密码：

```powershell
mysql -u root -p < .\monolith\scripts\mysql-init.sql
```

启动应用前设置数据库密码：

```powershell
cd monolith
$env:DB_PASSWORD = '你的数据库密码'
.\mvnw.cmd spring-boot:run
```

默认连接：

```text
DB_URL=jdbc:mysql://localhost:3306/maintenance
DB_USER=maintenance
DB_PASSWORD=你的数据库密码
```

表结构由 JPA `ddl-auto: update` 创建。应用启动后可选执行样例数据：

```powershell
mysql -u maintenance -p maintenance < .\scripts\sample-data.sql
```

### REST API

| 方法 | 路径 | 说明 | 成功状态 |
|---|---|---|---|
| POST | `/api/vehicles` | 创建车辆 | 201 |
| GET | `/api/vehicles` | 查询车辆列表 | 200 |
| GET | `/api/vehicles/{id}` | 查询车辆详情 | 200 |
| POST | `/api/work-orders` | 创建维修工单 | 201 |
| GET | `/api/work-orders` | 查询工单列表，可传 `vehicleId` | 200 |
| GET | `/api/work-orders/{id}` | 查询工单详情 | 200 |
| PATCH | `/api/work-orders/{id}/complete` | 完成维修工单 | 200 |

业务规则：同一车辆存在 `CREATED` 或 `IN_PROGRESS` 工单时，拒绝创建新的活跃工单并返回 `409`；工单完成后允许创建下一次维修工单。

### 可直接运行的 curl 示例

创建车辆：

```powershell
curl.exe -i -X POST http://localhost:8080/api/vehicles -H "Content-Type: application/json" -d '{"ownerName":"陆兵尧","plateNumber":"浙A12345","brand":"大众","model":"迈腾","mileage":52000}'
```

查询车辆列表：

```powershell
curl.exe -i http://localhost:8080/api/vehicles
```

创建工单前，将 `VEHICLE_ID` 替换为创建车辆响应中的 `id`：

```powershell
curl.exe -i -X POST http://localhost:8080/api/work-orders -H "Content-Type: application/json" -d '{"vehicleId":"VEHICLE_ID","serviceAdvisorName":"王顾问","technicianName":"李技师","faultDescription":"发动机异响"}'
```

完成工单：

```powershell
curl.exe -i -X PATCH http://localhost:8080/api/work-orders/WORK_ORDER_ID/complete
```

### 测试

```powershell
cd monolith
.\mvnw.cmd test
```

测试覆盖 Repository 读写和唯一约束、Service 活跃工单规则、API 的 201/400/404/409 响应，使用 H2 并自动准备数据。

### 第 04 周文档

- 项目提案：`docs/project-proposal.md`
- 数据库设计：`docs/database.md`
- 每周记录：`docs/homework/week-04/index.md`

### 无本机 MySQL 时的本地演示

测试依赖中包含 H2，可使用仅用于本地演示的 `test` 配置启动：

```powershell
cd monolith
.\mvnw.cmd "-Dspring-boot.run.useTestClasspath=true" "-Dspring-boot.run.profiles=test" spring-boot:run
```

该方式只用于接口演示和截图；正式数据库持久化仍使用 MySQL 配置。

