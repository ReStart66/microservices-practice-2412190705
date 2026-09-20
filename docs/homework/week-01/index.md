# 作业01：开发环境与个人仓库

## 环境检查

检查日期：2026-09-20  
系统环境：Windows 10.0.26200.9457（Windows 10 Home China 25H2，64 位）

### Java

```text
> java --version

openjdk 21.0.12.1 2026-08-18 LTS
OpenJDK Runtime Environment Temurin-21.0.12.1+1 (build 21.0.12.1+1-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.12.1+1 (build 21.0.12.1+1-LTS, mixed mode, sharing)
```

![Java 版本检查](<screenshots/屏幕截图 2026-09-20 225628.png>)

### Maven

```text
> mvn --version

Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
Maven home: E:\Tools\apache-maven-3.9.16
Java version: 21.0.12.1, vendor: Eclipse Adoptium, runtime: E:\Tools\jdk-21
Default locale: zh_CN, platform encoding: UTF-8
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

![Maven 版本检查](<screenshots/屏幕截图 2026-09-20 225636.png>)

### Git

```text
> git --version

git version 2.55.0.windows.5
```

![Git 版本检查](<screenshots/屏幕截图 2026-09-20 225649.png>)

### Docker 和 Docker Compose

```text
> docker version

Client:
 Version:           29.8.0
 API version:       1.56
 Go version:        go1.26.8
 Git commit:        88096ef
 Built:             Thu Sep  3 21:53:38 2026
 OS/Arch:           windows/amd64
 Context:           desktop-linux

Server: Docker Desktop 4.91.0 (239619)
 Engine:
  Version:          29.8.0
  API version:      1.56 (minimum version 1.40)
  Go version:       go1.26.8
  Git commit:       3ce5872
  Built:            Thu Sep  3 21:51:20 2026
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          v2.3.4
 runc:
  Version:          1.4.3
 docker-init:
  Version:          0.19.0

> docker compose version

Docker Compose version v5.5.1
```

![Docker 版本检查](<screenshots/屏幕截图 2026-09-20 225717.png>)

WSL 版本为 2.7.14.0，默认版本为 2。

## 概念回答

### 1. 什么是微服务架构？

微服务架构是把一个较大的应用拆分为多个独立的小型服务。在每个服务之间独立部署，独立运行，专注完成单独的部分。特点有：服务拆分，分布式部署，独立数据存储，api通行，技术多样性。在大型开发当中适用。

### 2. 微服务和单体架构的主要区别是什么？


单体架构在许多部分有着显著的区别，单体架构更加适合小型项目使用，虽然单体架构在复杂性和开发速度方面有着明显的优势，但是其局限性也很大，比如说单一部署，集中式管理，共享数据库等。微服务架构的一些优势是单体架构无法比拟的，它可以独立部署，按需要扩展，还有就是技术的多样性和团队自洽。


### 3. 为什么本课程先实现单体系统，再逐步拆分为微服务？

单体开发简单，为后面打下基础，先实现单体系统是为了便于学习验证业务，再拆分微服务式是为了从简单的架构逐渐过渡到复杂的架构，先跑通业务，等业务扩张了再逐步细分。避免出现大量问题。

### 4. 为什么作业需要提供可重复运行的测试或验证脚本？

因为重复运行的测试或验证脚本可以帮助找到问题，也方便其他人独立验证其结果，并且可以为后续的开发打下基础。

## 问题记录

开发环境最初安装了 JDK 8，执行作业要求的 `java --version` 时不受支持；随后安装了 JDK 21，并配置 `JAVA_HOME` 与 Path，使 Java 和 Maven 都使用 JDK 21。

Docker 安装过程中，WSL 2.7.14 在线下载速度较慢。启用 WSL 和 VirtualMachinePlatform 系统功能后，通过 Microsoft 官方 MSI 安装 WSL，再安装 Docker Desktop 4.91.0。Docker Desktop 初次启动时 Engine 尚未运行，重新启动后 Client 和 Server 均正常，`docker version` 和 `docker compose version` 已可以执行。

当前未发现未解决的环境问题。Git 提交记录截图将在完成提交和推送后补充到 `screenshots/`。
