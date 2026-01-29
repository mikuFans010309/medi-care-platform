## *🎯 一、阶段目标**

本阶段完成 `user-service` 微服务的完整开发，实现以下核心功能：

- 用户注册（手机号 + 密码）
- 用户登录（JWT 认证）
- 用户信息查询（需登录）
- 全局异常处理
- 接口文档（Swagger）
- JWT Token 验证过滤器
- 数据库集成（MyBatis-Plus + MySQL）

所有接口均通过 RESTful 设计，遵循统一返回格式和错误码规范，为后续微服务调用打下基础。

------

## **🧱 二、项目结构说明**

```
user-service/
├── src/main/java/com/mediacare/user
│   ├── annotation/          # 自定义校验注解
│   ├── config/              # 配置类（MyBatis、Swagger、WebMvc）
│   ├── controller/          # 控制器层（接收请求）
│   ├── dto/                 # 数据传输对象（Request/Response）
│   ├── entity/              # 实体类（对应数据库表）
│   ├── exception/           # 异常处理（自定义异常 + 全局捕获）
│   ├── filter/              # JWT 认证过滤器
│   ├── mapper/              # MyBatis Mapper 接口
│   ├── service/             # 业务逻辑层
│   │   └── impl/            # 实现类
│   └── util/                # 工具类（JwtUtil、UserContext）
│
├── src/main/resources
│   ├── application.yml      # 主配置文件
│   ├── bootstrap.yml        # Nacos 配置优先加载
│   └── mapper/UserMapper.xml # MyBatis XML 映射文件
│
└── pom.xml                  # Maven 依赖管理
```

> ✅ 结构清晰，分层明确，符合 Spring Boot 企业级最佳实践。

------

## **🔧 三、核心功能说明**

### **1. 用户注册（POST** `/auth/register`**）**

#### **请求参数（JSON）**

```
{
  "phone": "13800138000",
  "password": "Abc123456!",
  "realName": "张三",
  "userType": 1,
  "idCard": "110101199001011234"  #医生必须要
}
```

#### **响应示例（成功）**

```
{
  "code": 200,
  "msg": "注册成功",
  "data": null
}
```

#### **响应示例（失败）**

```
{
  "code": 10001,
  "msg": "手机号已存在"
}
```

> ⚠️ 注：`userType` 取值：
>
> - `1`: 患者
> - `2`: 医生
> - `3`: 管理员

------

### **2. 用户登录（POST** `/auth/login`**）**

#### **请求参数（JSON）**

```
{
  "phone": "13800138000",
  "password": "Abc123456!"
}
```

#### **响应示例（成功）**

```
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "userInfo": {
            "id": 2,
            "username": "13800138000",
            "realName": "张三",
            "userType": 1,
            "avatarUrl": null,
            "phone": "13800138000",
            "status": 1
        },
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyVHlwZSI6MSwidXNlcklkIjoyLCJleHAiOjE3NzY4NzQzNjV9.x_zHI_sDIP9Arw06y8nHIVpJ6rOTmnOkRjMwkX-zRJA"
    }
}
```

#### **响应示例（失败）**

```
{
    "code": 10003,
    "msg": "密码错误",
    "data": null
}
```

------

### **3. 获取用户信息（GET** `/user/info`**）**

#### **请求头（Authorization）**

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxxxx   #Bearer一定要带空格
```

#### **响应示例（成功）**

```
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": 2,
        "username": "13800138000",
        "realName": "张三",
        "userType": 1,
        "avatarUrl": null,
        "phone": "13800138000",
        "status": 1
    }
}
```

------

## **🔐 四、JWT 认证机制**

### **1. Token 生成规则**

- 使用 **HS256 算法**

- Secret Key 从 `application.yml` 中读取（不可硬编码）

- Payload 内容：

  ```
  {
    "userId": 1001,
    "userType": 1,
  }
  ```

- 有效期：**2 小时**

### **2. JWT 过滤器（**`JwtAuthenticationFilter`**）**

- 位于 Spring MVC 过滤链中
- 执行流程：
  1. 检查请求头是否存在 `Authorization`
  2. 提取 `Bearer token`
  3. 解析 Token 并验证签名
  4. 若有效，将用户 ID 和类型存入 `UserContext`
  5. 放行请求
  6. 若无效或过期，抛出 `BusinessException(10004)`
- **不拦截 `/auth/** 路径**（免鉴权）

------

## **🧩 五、核心组件说明**

### **1.** `Result<T>` **统一响应格式**

```
public class Result<T> {
    private Integer code; //编码：1成功，0为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success(T data) { ... }
    public static <T> Result<T> error(int code, String msg) { ... }
    public static <T> Result<T> success() { ... }
}
```

所有 Controller 返回 `Result<?>`，确保前后端交互一致。

------

### **2.** `ErrorCode` **枚举（错误码定义）**

```
public enum ErrorCode {
     //定义用户模块功能错误码
    PHONE_EXISTS(10001, "手机号已注册"),
    USER_NOT_FOUND(10002, "用户不存在"),
    PASSWORD_ERROR(10003, "密码错误"),
    INVALID_TOKEN(10004, "无效的Token"),
    USER_TYPE_NOT_ALLOWED(10005, "用户类型不允许");
}
```

> ✅ 所有异常都使用此枚举，便于前端统一处理。

------

### **3.** `GlobalExceptionHandler` **全局异常处理**

- 捕获 `BusinessException` 和 `RuntimeException`
- 返回标准 `Result` 格式
- 日志记录（避免敏感信息泄露）

------

### **4.** `PasswordEncodeConfig` **密码加密配置**

- 使用 `BCryptPasswordEncoder` 加密密码
- 加密后存储在数据库中
- 登录时比对原始密码与加密值

------

### **5.** `MyBatisPlusConfig` **数据库配置**

- 启用自动填充（如 `created_at`, `updated_at`）
- 设置主键策略（AUTO）
- 配置 HikariCP 连接池参数

------

## **📚 六、接口文档（Swagger）**

访问地址：
👉 `http://localhost:8081/api/swagger-ui.html`

### **接口列表**

| 方法 | URL              | 描述             |
| :--- | :--------------- | :--------------- |
| POST | `/auth/register` | 用户注册         |
| POST | `/auth/login`    | 用户登录         |
| GET  | `/user/info`     | 获取当前用户信息 |

> ✅ 所有接口均带有参数说明和响应示例。

------

## **💾 七、初始数据脚本**

执行 SQL 文件：`sql/v1.1_init_admin.sql`

sql

```
INSERT INTO `sys_user` (
    `username`, `password`, `real_name`, `phone`, `user_type`, `status`
) VALUES (
    'admin', 
    ' $ 2a $ 10 $ DfYxZqJvJ7KQvW6bH6x7eO8u1X9Y0Z1a2B3c4D5e6F7g8H9i0J',
    '系统管理员',
    '13800000000',
    3,
    1
);
```

> 💡 此账号用于后台管理，仅在首次部署时插入。

------

## **🚀 八、如何启动服务**

### **1. 依赖中间件**

| 组件  | 地址           | 版本        |
| :---- | :------------- | :---------- |
| MySQL | localhost:3307 | 8.0+        |
| Nacos | localhost:8848 | 2.2+        |
| Redis | localhost:6379 | 7+ （可选） |

### **2. 启动步骤**

1. 确保 MySQL 和 Nacos 已运行
2. 执行 `sql/v1.0_init_schema.sql` 创建表
3. 执行 `sql/v1.1_init_admin.sql` 插入管理员
4. 启动 `user-service` 应用
5. 访问 http://127.0.0.1:8848/nacos 查看服务是否注册成功
6. 访问 http://localhost:8081/api/swagger-ui测试接口

------

## **📌 九、注意事项**

1. **禁止明文存储密码**，必须使用 BCrypt 加密

2. **JWT Token 不要暴露在日志中**，避免泄露

3. **生产环境禁用 Swagger UI**，可通过配置关闭

4. **所有敏感字段（如密码）不参与日志打印**

5. Git 提交信息规范

   ：

   - `feat(user): 实现用户注册`
   - `fix(auth): 修复登录异常处理`
   - `docs(user): 更新接口文档`

------

## **✅ 十、交付成果清单**

| 项                                       | 是否完成 |
| :--------------------------------------- | :------- |
| ✅ `user-service` 工程结构完整            | ✔️        |
| ✅ 用户注册接口实现                       | ✔️        |
| ✅ 用户登录接口实现                       | ✔️        |
| ✅ 用户信息接口实现                       | ✔️        |
| ✅ JWT 认证过滤器实现                     | ✔️        |
| ✅ 全局异常处理                           | ✔️        |
| ✅ Swagger 文档可用                       | ✔️        |
| ✅ 初始数据脚本提交                       | ✔️        |
| ✅ `docs/phase2-user-service.md` 文档完成 | ✔️        |