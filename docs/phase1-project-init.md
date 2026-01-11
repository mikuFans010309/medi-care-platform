# **🚧 第一阶段：项目初始化与基础架构搭建**

> **目标**：建立完整的项目骨架、Git 仓库结构、数据库初始化方案、微服务划分、注册中心与网关基础配置。



## **1. 项目结构**

```
medi-care-platform/
├── docs/                     # 所有设计文档存放处
│   ├── phase1-project-init.md  # 当前文档
│   └── diagrams/             # 架构图、流程图等（可用 draw.io 导出 PNG/SVG）
├── sql/                      # 所有 SQL 脚本
│   ├── v1.0_init_schema.sql  # 初始建表脚本
│   └── README.md             # 说明 SQL 版本与执行顺序
├── docker/                   # Docker 相关配置（本阶段可空）
├── infra/                    # 基础设施脚本（如 Nacos 配置，本阶段可空）
├── services/                 # 微服务模块
│   ├── user-service/         # 用户服务
│   ├── department-service/   # 科室服务
│   ├── appointment-service/  # 预约服务
│   └── gateway-service/      # API 网关
├── pom.xml                   # Maven 父工程（若用 Maven）
└── README.md                 # 项目总览、启动指南、阶段说明
```



##  2. 微服务拆分（初步设计）

根据业务领域进行垂直拆分，遵循“单一职责”原则。每个服务独立部署、独立演进。

| 服务名                | 职责                                 | 技术栈建议                     |
| :-------------------- | :----------------------------------- | :----------------------------- |
| `user-service`        | 患者/医生/管理员注册、登录、信息管理 | Spring Boot + MyBatis-Plus     |
| `department-service`  | 科室管理、医生排班                   | Spring Boot + MyBatis-Plus     |
| `appointment-service` | 挂号预约、订单生成                   | Spring Boot + RocketMQ（预留） |
| `gateway-service`     | API 网关，路由、鉴权                 | Spring Cloud Gateway           |
| `auth-service`        | （后续阶段）统一认证中心             | Spring Security OAuth2 / JWT   |

> ⚠️ 注意：本阶段仅创建服务目录结构，不实现任何业务逻辑。



##  3. 数据库设计（v1.0 初始版本)

### **3.1 数据库命名规范**

- 数据库名：`medi_care_platform`
- 字符集：`utf8mb4`
- 排序规则：`utf8mb4_unicode_ci`
- 表名：小写加下划线，如 `sys_user`
- 主键：`BIGINT AUTO_INCREMENT`
- 时间字段：`DATETIME`，默认值为当前时间，更新时自动更新



## **4. 基础中间件部署（本地开发环境）**

你需要在本地运行以下组件以支撑微服务开发：

| 组件       | 用途               | 启动方式                                                     |
| :--------- | :----------------- | :----------------------------------------------------------- |
| MySQL 8.0  | 主数据库           | `docker run --name medi-mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 mysql:8.0` |
| Nacos 2.2+ | 服务注册与配置中心 | `docker run --name nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 nacos/nacos-server:2.2.0` |
| Redis 7    | 缓存（预留）       | `docker run --name medi-redis -p 6379:6379 redis:7`          |