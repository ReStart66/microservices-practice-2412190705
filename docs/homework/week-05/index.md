# 第 05 周：容器化与数据持久化

本周继续使用 `monolith/` 单体工程，实现了多阶段镜像构建、Docker Compose 编排、容器化业务 API 验证和 MySQL 命名卷持久化验证。

## Dockerfile 与镜像构建

`monolith/Dockerfile` 使用两阶段构建：

- 构建阶段：`maven:3.9.16-eclipse-temurin-25`
- 运行阶段：`eclipse-temurin:25.0.4.1_1-jre`

构建阶段执行 `mvn -B -ntp clean package`，运行阶段只复制可执行 JAR，因此最终镜像不包含 Maven、源码和 JDK 编译工具。

`.dockerignore` 排除了 `target/`、IDE 配置、`.env`、Git 目录和脚本等无关内容。

构建命令：

```powershell
docker build -t maintenance-monolith:week05 ./monolith
```

结果：

```text
maintenance-monolith:week05
BUILD_EXIT=0
```

最终镜像只包含 `/app/app.jar` 和 JRE 运行环境。

![镜像构建与镜像列表](<screenshots/01-image-build.png>)

## Compose 编排与启动

根目录 `compose.yaml` 包含：

- `app`：端口 `8080:8080`，构建上下文 `./monolith`
- `mysql`：`mysql:8.4.11`
- 命名卷：`mysql-data`
- 共享网络：`maintenance-network`
- MySQL 健康检查
- `depends_on.mysql.condition: service_healthy`

应用通过服务名连接数据库：

```text
jdbc:mysql://mysql:3306/maintenance
```

实际启动结果：

```text
app    Up    0.0.0.0:8080->8080
mysql  Up    healthy
```

![Compose 服务状态和日志](<screenshots/02-compose-running.png>)

## 个人业务 API 验证

应用容器启动后，验证了健康接口和车辆、维修工单业务接口。

已验证：

```text
GET  /actuator/health                    -> 200
POST /api/vehicles                       -> 201
POST /api/work-orders                    -> 201
POST /api/work-orders（重复活跃工单）     -> 409
GET  /api/work-orders?vehicleId=...      -> 200
```

本次截图验证使用的车辆：

```text
vehicleId  = bc711340-ef99-46a7-9595-7de5b93139b9
plateNumber = W05-5EBF67
```

工单：

```text
workOrderId = f585002a-86dc-4275-9475-1e22c5748ca7
status      = CREATED
```

重复创建同一车辆的活跃工单返回 `409`，说明 Service 中的业务规则在容器环境中仍然生效。

![容器化业务 API 验证](<screenshots/03-api-validation.png>)

## 数据持久化验证

使用业务 API 创建一条独立验证数据：

```text
vehicleId   = a26cca0f-a44a-41ad-bd2e-70d8af83273b
workOrderId = 1d06585c-b5f9-4dc5-9d20-d93d06bcf7f7
plateNumber = PERSIST-152C4C
status      = CREATED
```

删除并重建容器：

```powershell
docker compose down
docker compose up -d
```

没有使用 `docker compose down -v`，因此命名卷 `mysql-data` 被保留。

重建后查询同一 ID：

```text
vehicleId   不变
workOrderId 不变
plateNumber 不变
status      不变
PERSISTED=True
```

同时通过 MySQL 容器查询确认对应记录仍然存在。

重建前：

![持久化验证重建前](<screenshots/04-persistence-before.png>)

重建后：

![持久化验证重建后](<screenshots/05-persistence-after.png>)

## 自动化测试

在 `monolith/` 目录执行：

```powershell
.\mvnw.cmd test
```

结果：

```text
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
TEST_EXIT=0
```

![自动化测试结果](<screenshots/06-tests-success.png>)

## 完成检查

- 多阶段 Dockerfile 构建成功。
- `.dockerignore` 忽略本地构建产物、IDE 配置和密码文件。
- Compose 包含 app、MySQL、共享网络和命名数据卷。
- MySQL 健康检查生效，应用等待 MySQL 健康后启动。
- 容器化后的健康接口和业务 API 可访问。
- `docker compose down/up` 后数据仍存在。
- 自动化测试全部通过。

## 常用命令

```powershell
# 启动
docker compose up -d --build

# 查看状态和日志
docker compose ps
docker compose logs --tail=100 app

# 停止容器，保留数据卷
docker compose down
```
