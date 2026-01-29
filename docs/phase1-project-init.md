

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



## 4. 基础中间件部署（本地开发环境）

你需要本地运行以下组件基于用 Docker：

#### 4.1.MySql部署

**🐳 Docker 命令：**

```txt
|-------------MySQL----------------|

| 组件 | 用途 | 启动方式建议 |
|------|------|-------------|
| MySQL 8.0 
| 主数据库 
| `docker run \
	--name medi-mysql \
	-e MYSQL_ROOT_PASSWORD=123456 \
	-p 3306:3306 \
	mysql:8.0`
	
| 注意: 如果本地下载了mysql则 -p 3306:3306 左边的3306要替换成3307否则会占用端口引发冲突 
```

### **✅ 解释：**

| 参数                            | 说明                                      |
| :------------------------------ | :---------------------------------------- |
| `--name medi-mysql`             | 给容器起个名字，方便后续管理              |
| `-e MYSQL_ROOT_PASSWORD=123456` | 设置 root 用户密码为 `123456`             |
| `-p 3306:3306`                  | 将宿主机端口 3306 映射到容器内部端口 3306 |
| `mysql:8.0`                     | 使用官方镜像                              |

#### 4.2.Nacos部署

**🐳 Docker 命令：**

```txt
|-------------Nacos----------------|

| Nacos 2.2+ 
| 服务注册与配置中心 
| 官方 Docker 镜像，单机模式即可：
  `docker run \
  --name nacos \
  -e MODE=standalone \
  -e NACOS_AUTH_TOKEN=pND0K2q28yx6LXNzs0aMdc6se/ms+MMN21qS1bQ1PUI= \
  -e  NACOS_AUTH_IDENTITY_KEY=serverIdentity \
  -e NACOS_AUTH_IDENTITY_VALUE=1433223Yzh \
  -p 8848:8848 \
  -p 9848:9848 \
  -p 9849:9849 \
  --privileged=true \
  -v /tmp/nacos/logs:/home/nacos/logs \
  -v /tmp/nacos/data:/home/nacos/data \
  -v /tmp/nacos/conf:/home/nacos/conf \
  -v /tmp/nacos/plugins:/home/nacos/plugins \
  -v /tmp/nacos/init.d:/home/nacos/init.d \
  nacos/nacos-server:v2.2.3` 
  
| 注意:Nacos 2.2.0+ 版本开启了鉴权功能，必须设置 NACOS_AUTH_TOKEN 环境变量，且值需要是一个 Base64 编码的字符串。以及 -v 	 /tmp/nacos/logs:/home/nacos/logs \  此目录必须要要有nacos的application.properties等配置文件，如果没有则需要拷贝一份或者生成临时容易进行文件转移
```

启动一个临时容器，把镜像里的默认配置复制到你要挂载的 `/tmp/nacos/conf` 目录：

```
docker run -d --rm --name temp-nacos nacos/nacos-server:v2.2.3 tail -f /dev/null
docker cp temp-nacos:/home/nacos/conf /tmp/nacos/
docker stop temp-nacos
```

### **✅ 解释：**

| 参数                           | 说明                                   |
| :----------------------------- | :------------------------------------- |
| `MODE=standalone`              | 单机模式，适合开发                     |
| `-p 8848:8848`                 | Web 控制台端口（访问地址）             |
| `-p 9848:9848`, `-p 9849:9849` | Nacos 内部通信端口                     |
| `-v ...`                       | 挂载目录，保证数据持久化（重启不丢失） |
| `--privileged=true`            | 提升权限，避免某些文件权限错误         |

#### 4.3.Redis部署

**🐳 Docker 命令：**

```
docker run \
  --name medi-redis \
  -p 6379:6379 \
  redis:7
```

### **✅ 解释：**

| 参数                | 说明                   |
| :------------------ | :--------------------- |
| `--name medi-redis` | 容器名                 |
| `-p 6379:6379`      | 映射端口               |
| `redis:7`           | 官方镜像，Redis 7 版本 |