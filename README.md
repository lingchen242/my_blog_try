# My Blog - 个人博客系统

基于 Spring Boot 3 + Vue 3 的前后端分离个人博客系统，支持游客浏览、用户登录、博客管理、留言交互等功能。

## 技术栈

### 后端

| 技术 | 说明 |
|------|------|
| Spring Boot 3.2.0 | Web 框架 |
| MyBatis-Plus 3.5.5 | ORM 框架 |
| MySQL | 数据库 |
| JWT (jjwt 0.12.3) | 身份认证 |
| Maven | 项目构建 |

### 前端

| 技术 | 说明 |
|------|------|
| Vue 3 | 前端框架 |
| Vite 5 | 构建工具 |
| Element Plus | UI 组件库 |
| Axios | HTTP 请求库 |
| Pinia | 状态管理 |
| Vue Router | 路由管理 |

## 功能特性

- **用户角色**: 管理员、普通用户、游客
- **登录注册**: JWT 认证，支持账号登录和游客一键进入
- **博客管理**: 文章的增删改查，按角色展示不同视图
- **留言系统**: 针对单篇文章的留言，支持昵称自动填充
- **后台管理**: 角色动态菜单，管理员可管理用户、文章、留言
- **个人介绍**: 可编辑的前台个人介绍模块

## 项目结构

```
my_blog_try/
├── backend/                        # 后端（Spring Boot）
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/demo/
│       │   ├── common/             # 通用工具（Result、JwtUtil）
│       │   ├── config/             # 配置类（CORS、MyBatis-Plus、Web）
│       │   ├── controller/         # 控制器
│       │   ├── entity/             # 实体类
│       │   ├── interceptor/        # JWT 拦截器
│       │   ├── mapper/             # MyBatis-Plus Mapper
│       │   └── service/            # 业务层
│       └── resources/
│           ├── application.yml     # 应用配置
│           └── db/                 # SQL 脚本
├── frontend/                       # 前端（Vue 3）
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── api/                    # 接口封装
│       ├── router/                 # 路由配置
│       ├── stores/                 # Pinia 状态管理
│       ├── utils/                  # 工具（Axios 封装）
│       └── views/                  # 页面
│           ├── front/              # 前台页面
│           └── admin/              # 后台管理页面
└── README.md
```

## 快速开始

### 前置条件

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+

### 1. 初始化数据库

```sql
CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4;
```

### 2. 启动后端

```bash
cd backend
# 修改 application.yml 中的数据库连接信息
mvn spring-boot:run
```

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 4. 初始化示例数据

```bash
mysql -u root -p blog < backend/src/main/resources/db/init.sql
mysql -u root -p blog < backend/src/main/resources/db/init_data.sql
```

## 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | 123456 | 可访问后台管理所有功能 |
| 普通用户 | user | 123456 | 可管理自己的博客和个人介绍 |
| 普通用户 | zhangsan | 123456 | 示例用户 |
| 游客 | - | - | 点击"游客登录"一键进入 |

## 接口概览

| 接口 | 方法 | 权限 | 说明 |
|------|------|------|------|
| `/api/auth/login` | POST | 公开 | 登录 |
| `/api/auth/guest-login` | POST | 公开 | 游客登录 |
| `/api/blogs` | GET | 公开 | 获取所有博客 |
| `/api/blogs/my` | GET | 登录 | 获取自己的博客 |
| `/api/blogs/{id}` | GET | 公开 | 博客详情 |
| `/api/blogs` | POST | 登录 | 新增博客 |
| `/api/blogs/{id}` | PUT | 作者 | 修改博客 |
| `/api/blogs/{id}` | DELETE | 作者 | 删除博客 |
| `/api/blogs/admin/all` | GET | 管理员 | 搜索所有博客 |
| `/api/messages` | GET | 公开 | 获取留言（支持 blogId） |
| `/api/messages` | POST | 公开 | 提交留言 |
| `/api/messages/{id}` | DELETE | 管理员/文章作者 | 删除留言 |
| `/api/admin/users` | GET | 管理员 | 用户列表 |
| `/api/profile` | GET | 公开 | 获取个人介绍 |
| `/api/profile` | PUT | 登录 | 更新个人介绍 |

## 许可证

MIT