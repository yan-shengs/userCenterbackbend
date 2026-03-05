# userCenterbackbend
The production is easy demo for practising the mode of usercenter.This is a demo project designed to imitate the Yupi User Center.


一个基于 **Spring Boot 3 + MyBatis-Plus + MySQL** 的入门级用户管理中心后端项目，提供：

- 登录
- 注册
- 获取当前登录用户
- 查询用户（管理员）
- 删除用户（管理员）
- 注销

> 说明：本项目使用 **Session（JSESSIONID）** 作为登录态承载方式；统一返回结构为 `Result<T>`；业务异常通过 `@RestControllerAdvice` 统一处理。

---

## 技术栈

- Java 17
- Spring Boot 3.0.2
- MyBatis-Plus 3.5.15
- MySQL
- Lombok

---

## 目录结构（核心）

- `src/main/java/com/qyzen/usercenterbackbend/controller`
  - `UserController`：用户相关 HTTP API
- `src/main/java/com/qyzen/usercenterbackbend/service`
  - `UserService`：业务接口
- `src/main/java/com/qyzen/usercenterbackbend/service/impl`
  - `UserServiceImpl`：业务实现
- `src/main/java/com/qyzen/usercenterbackbend/domain`
  - `User`：实体（MyBatis-Plus Entity）
- `src/main/java/com/qyzen/usercenterbackbend/dto`
  - `Result` / `ResultUtils` / `ResultCode`：统一返回与状态码
- `src/main/java/com/qyzen/usercenterbackbend/exception`
  - `BussinessException`：业务异常
  - `GlobalExceptionInterceptor`：全局异常处理（`@RestControllerAdvice`）
- `src/main/resources/application.yaml`
  - 应用配置

---

## 环境要求

- JDK 17
- Maven 3.8+
- MySQL 8.x（或兼容版本）

---

## 配置说明

项目默认读取：`src/main/resources/application.yaml`

关键配置：

- 服务端口：

```yaml
server:
  port: 8080
```

- Session 过期时间：

```yaml
server:
  servlet:
    session:
      timeout: 30m
```

- MySQL 数据源：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
    url: jdbc:mysql://127.0.0.1:3306/UserCenter-yupi?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false
```

---

## 数据库准备

1. 创建数据库（示例）：

```sql
CREATE DATABASE IF NOT EXISTS `UserCenter-yupi` DEFAULT CHARACTER SET utf8mb4;
```

2. 创建 `User` 表

请以你本地实际表结构为准。

实体 `User` 中涉及字段：

- `id`（自增主键）
- `username`
- `password`
- `nikename`
- `isAdmin`
- `createdBy`
- `updatedBy`
- `isDelete`（逻辑删除字段，MyBatis-Plus `@TableLogic`）

> 建议：为 `username` 建立唯一索引，避免重复账号导致 `selectOne` 异常。

---

## 启动项目

### 方式 1：IDEA 直接启动

运行：

- `com.qyzen.usercenterbackbend.BackbendApplication`

### 方式 2：Maven 启动

在 `backbend` 目录下执行：

```bash
mvn spring-boot:run
```

启动后默认地址：

- `http://localhost:8080`

---

## 统一返回结构

所有接口统一返回：

```json
{
  "code": 2000,
  "data": {},
  "message": "操作成功",
  "description": "业务逻辑操作成功"
}
```

- `code`：业务状态码（见 `ResultCode`）
- `data`：返回数据
- `message`：简要信息
- `description`：详细描述

---

## 业务异常与全局处理

项目通过抛出 `BussinessException`，交由 `GlobalExceptionInterceptor` 统一转成 `Result` 返回。

常见错误码（示例，见 `ResultCode`）：

- `ISParameter`：参数错误
- `ISTOKEN`：未登录/登录态失效
- `ISAUTH`：无权限
- `ISACCOUNTLOGIN`：账号/密码错误
- `SYSTEM_ERROE`：系统异常（建议后续更正拼写为 `SYSTEM_ERROR`）

---

## 登录态说明（Session）

- 登录/注册成功后，后端会把 `ResponseUserVo` 写入 Session：
  - key：`UserLoginStatus`
- 前端（或 Postman）需要在后续请求中携带同一个 Cookie（`JSESSIONID`），才能识别为已登录。

---

## API 文档

统一前缀：`/api/v1`

### 1) 登录

- **POST** `/api/v1/login`

请求体：

```json
{
  "username": "test123",
  "password": "12345678"
}
```

返回：`Result<ResponseUserVo>`

---

### 2) 注册

- **POST** `/api/v1/register`

请求体：

```json
{
  "username": "test123",
  "password": "12345678",
  "passwordDouble": "12345678"
}
```

返回：`Result<ResponseUserVo>`

---

### 3) 注销

- **GET** `/api/v1/logout`

返回：`Result<String>`

---

### 4) 获取当前登录用户

- **GET** `/api/v1/current`

返回：`Result<ResponseUserVo>`

---

### 5) 查询用户（管理员）

- **GET** `/api/v1/search?username=test123`

说明：
- 需要已登录
- 需要管理员权限（`isAdmin == 1`）

返回：`Result<List<ResponseUserVo>>`

---

### 6) 删除用户（管理员）

- **PUT** `/api/v1/delete`

参数：
- `username`：要删除的用户名（当前实现按 username 删除）

说明：
- 需要已登录
- 需要管理员权限（`isAdmin == 1`）

返回：`Result<String>`

> 建议：后续将删除接口改为 `DELETE /users/{id}`，避免按 username 误删。

---

## 常见问题（FAQ）

### 1. 为什么 `isDelete` 会出现 “property (isDelete) can not found setter method”？

通常与 JavaBean 命名规范、字段命名以 `is` 开头、以及 MyBatis 映射/反射设置属性有关。

建议做法：
- Java 字段命名用 `deleted` 或 `isDeleted`
- 数据库列仍可保持 `isDelete`，通过 `@TableField("isDelete")` 映射

### 2. `createdBy/updatedBy` 默认值在哪里设置？

当前项目 Entity 未配置 MyBatis-Plus 自动填充（`FieldFill + MetaObjectHandler`），如需默认值：

- 方案 A：数据库列设置默认值（`DEFAULT CURRENT_TIMESTAMP` / `ON UPDATE CURRENT_TIMESTAMP`）
- 方案 B：MyBatis-Plus 自动填充（推荐工程化）

---

## Roadmap（建议迭代）

- 使用 BCrypt 替代 MD5 存储密码
- username 唯一索引
- 将管理员鉴权提取为拦截器（统一拦截 `/admin/**`）
- 统一接口语义（logout 用 POST，delete 用 DELETE）
- 引入参数校验注解（`jakarta.validation`）

---

## License

学习/练手项目，暂无许可证声明。
