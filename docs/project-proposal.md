# 汽车维修保养管理系统项目提案

## 项目名称

汽车维修保养管理系统

## 目标用户

- 车主：预约维修、查看进度和账单。
- 服务顾问：接车、创建工单、管理配件、生成账单。
- 维修技师：检测车辆、记录维修、提交完工。
- 学徒：执行分配任务、记录操作进度。
- 收银员：处理账单、收款和退款。

## 项目背景与范围

系统面向单店或小型连锁汽车维修门店，当前重点覆盖客户车辆档案、预约接车、维修工单、维修记录、费用结算和取车提醒。本周只在现有单体应用中实现一条完整、可验证的核心业务流程，不扩展多门店、真实支付和保险理赔。

## 第 04 周核心业务流程

本周实现“车辆建档并创建维修工单”：

1. 服务顾问为车主登记车辆信息。
2. 车辆到店后，服务顾问为车辆创建维修工单，填写故障描述并分配维修技师。
3. 维修工单进入 `CREATED` 状态。
4. 维修完成后，将工单状态更新为 `COMPLETED`，记录完成时间。
5. 同一车辆在已有未完成工单时，不允许再创建新的维修工单。

## 核心业务实体

### Vehicle 车辆

主要字段：

- `id`：车辆内部编号，UUID。
- `ownerName`：车主姓名。
- `plateNumber`：车牌号，全局唯一。
- `brand`：车辆品牌。
- `model`：车辆型号。
- `mileage`：当前里程。
- `createdAt`：建档时间。

### WorkOrder 维修工单

主要字段：

- `id`：工单编号，UUID。
- `vehicle`：所属车辆，多张工单可以对应同一辆车。
- `serviceAdvisorName`：服务顾问姓名。
- `technicianName`：维修技师姓名。
- `faultDescription`：故障描述。
- `status`：`CREATED`、`IN_PROGRESS`、`COMPLETED`。
- `createdAt`：创建时间。
- `completedAt`：完成时间。

### 实体关系

`Vehicle 1 -> N WorkOrder`。车辆是独立实体，维修工单通过外键 `vehicle_id` 关联车辆；工单不能脱离车辆存在。

## Service 业务规则

`WorkOrderService.create` 在创建工单前检查：

- 车辆必须存在，否则返回 404。
- 同一车辆不能同时存在 `CREATED` 或 `IN_PROGRESS` 状态的工单，否则返回 409。
- 工单完成后允许为该车辆创建下一次维修工单。
- `WorkOrderService.complete` 不允许重复完成同一张工单。

该规则涉及数据库查询和写入，必须在 Service 中执行，Controller 只负责 HTTP 输入输出。

## REST API

- `POST /api/vehicles`：创建车辆。
- `GET /api/vehicles`：查询车辆列表。
- `GET /api/vehicles/{id}`：查询车辆详情。
- `POST /api/work-orders`：创建维修工单。
- `GET /api/work-orders`：查询工单列表，可按 `vehicleId` 筛选。
- `GET /api/work-orders/{id}`：查询工单详情。
- `PATCH /api/work-orders/{id}/complete`：完成维修工单。

## 优先级

1. 车辆建档与车牌号唯一性。
2. 维修工单创建与活跃工单唯一业务规则。
3. 工单完成状态流转。
4. JPA 持久化、重复测试和数据库约束。
