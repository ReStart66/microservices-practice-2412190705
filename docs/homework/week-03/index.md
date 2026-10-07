# 第 3 周实践作业：创建并运行 Spring Boot 工程

## 本周计划

- 在仓库根目录创建 `monolith/` Maven 工程。
- 使用 Java 25、Spring Boot 4.0.8 和 Maven。
- 使用包名 `com.zjgsu.lby`。
- 使用 `application.yml` 完成基础配置。
- 提供 `/api/hello` 和 `/actuator/health` 接口。
- 保留 `@SpringBootTest` 启动测试。

## 工程配置

- 工程目录：`monolith/`
- 启动类：`MonolithApplication`
- 默认端口：`8080`
- 配置文件：`src/main/resources/application.yml`
- 测试类：`MonolithApplicationTests`

## 启动命令

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

启动成功后输出：

```text
Tomcat started on port 8080
Started MonolithApplication
```

## 接口验证

接口一：

```text
GET http://localhost:8080/api/hello
```

实际响应：

```json
{"message":"Spring Boot application is running","project":"汽车维修保养管理系统"}
```

接口二：

```text
GET http://localhost:8080/actuator/health
```

实际结果：

```text
status：UP
groups：liveness、readiness
```

## 自动化测试

测试命令：

```powershell
cd monolith
.\mvnw.cmd test
```

测试结果：

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 截图

截图保存到：

```text
docs/homework/week-03/screenshots/
```

已包含：

- `monolith-structure.png`
- `spring-boot-run.png`
- `application-started.png`
- `api-and-health.png`
- `mvnw-test.png`

## 本周完成内容

- 创建 `monolith/` Spring Boot Maven 工程。
- 配置 Java 25、Spring Boot 4.0.8 和 `application.yml`。
- 完成 `/api/hello` 问候接口和 `/actuator/health` 健康检查。
- `contextLoads` 启动测试通过。
- 启动、接口和测试截图已保存。
## 后续计划

下一周开始设计数据库和核心业务模型，优先实现车辆与维修工单模块。

