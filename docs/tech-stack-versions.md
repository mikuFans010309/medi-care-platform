## **🧰 一、核心开发技术栈（Java 生态）**



| 组件                     | 版本                          | 说明                                                 |
| :----------------------- | :---------------------------- | :--------------------------------------------------- |
| **Java JDK**             | **17**                        | 推荐使用 OpenJDK 17 或 Oracle JDK 17（LTS 长期支持） |
| **Maven**                | **3.9.11**                    | 用于依赖管理和构建（需支持 Java 17）                 |
| **Spring Boot**          | **3.2.0**                     | 主框架（注意：Spring Boot 3.x 要求 JDK 17+）         |
| **Spring Cloud**         | **2023.0.0**（代号 *Vivian*） | 微服务治理（兼容 Spring Boot 3.2）                   |
| **Spring Cloud Alibaba** | **2022.0.0.0**                | 提供 Nacos、Sentinel、Seata 等集成                   |
| **MyBatis-Plus**         | **3.5.5**                     | 增强 MyBatis，简化 CRUD（兼容 Spring Boot 3）        |
| **Lombok**               | **1.18.30**                   | 简化 POJO 编写（需 IDE 安装插件）                    |

> 💡 注意：Spring Boot 3.x **不再支持** `javax.*` 包，全面转向 `jakarta.*`，请确保所有依赖兼容。

------

## **🗄️ 二、数据库与缓存**

| 组件                          | 版本                       | 说明                          |
| :---------------------------- | :------------------------- | :---------------------------- |
| **MySQL**                     | **8.4.7**                  | 关系型数据库，主业务存储      |
| **Redis**                     | **7.2.4**（或 7.x 最新版） | 缓存、分布式锁、会话共享等    |
| **Driver: MySQL Connector/J** | **8.0.33+**                | JDBC 驱动（Maven 中自动引入） |

------

## **🌐 三、中间件（本地 Docker 部署）**

| 组件      | Docker 镜像标签            | 对应版本  | 访问地址                              |
| :-------- | :------------------------- | :-------- | :------------------------------------ |
| **Nacos** | `nacos/nacos-server:3.1.1` | **3.1.1** | http://192.168.74.112:8080/index.html |
| **MySQL** | `mysql:8.4`                | **8.4.7** | localhost:3306                        |
| **Redis** | `redis:7`                  | **7.2.4** | localhost:6379                        |

> ✅ 强烈建议使用上述**固定版本标签**，避免 `latest` 导致不可预期的变更。

------

## **🛠️ 四、开发与运维工具**

| 工具                     | 推荐版本                                                 | 用途                                        |
| :----------------------- | :------------------------------------------------------- | :------------------------------------------ |
| **Docker Desktop**       | **4.55.0**（含 Docker Engine 29.1.3）                    | 容器化运行中间件                            |
| **Git**                  | **2.52.0**                                               | 代码版本控制                                |
| **IDE**                  | **IntelliJ IDEA 2024.1**                                 | 开发环境                                    |
| **MySQL 客户端**         | **MySQL Workbench 8.4**                                  | 数据库管理                                  |
| **Redis 客户端**         | **RedisInsight v2** 或 **Another Redis Desktop Manager** | Redis 可视化                                |
| **Postman / Apifox**     | 最新版                                                   | API 测试（后续阶段用）                      |
| **Draw.io / Excalidraw** | 在线版即可                                               | 绘制架构图、流程图（存入 `docs/diagrams/`） |

------

## **📦 五、Maven 依赖关键版本（已在父 POM 中声明）**

```
<properties>
    <java.version>17</java.version>
    <spring-boot.version>3.2.0</spring-boot.version>
    <spring-cloud.version>2023.0.0</spring-cloud.version>
    <spring-cloud-alibaba.version>2022.0.0.0</spring-cloud-alibaba.version>
    <mybatis-plus.version>3.5.5</mybatis-plus.version>
    <lombok.version>1.18.30</lombok.version>
</properties>
```

------

## **🔒 六、安全与认证（预留，第二阶段启用）**

| 技术                | 版本                                     | 说明                                          |
| :------------------ | :--------------------------------------- | :-------------------------------------------- |
| **JWT (jjwt)**      | **0.11.5**                               | 用于 Token 生成与解析（Spring Security 集成） |
| **Spring Security** | **6.2.0**（随 Spring Boot 3.2 自动引入） | 权限控制                                      |

------

## **📁 七、项目结构规范（重申）**

- **包命名**：`com.mediacare.{service-name}`（如 `com.mediacare.user`）
- **分支策略**：`main`（主干），`dev`（开发），功能分支如 `feature/user-login`
- **SQL 文件**：按版本管理，如 `v1.0_init_schema.sql`