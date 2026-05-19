# AsukaBlog

基于 Vue 3 + Spring Boot 4 的个人博客系统。支持 Markdown 文章发布、专题分类、后台管理，通过 Docker Compose 一键部署。

## 技术栈

| 前端 | 后端 | 部署 |
|------|------|------|
| Vue 3.5 + Composition API | Spring Boot 4.0.6 | Docker Compose |
| Vite 8 | MyBatis + PageHelper | Nginx (静态资源 + API 反向代理) |
| Element Plus 2.14 | CommonMark 0.21 (Markdown → HTML) | MySQL 8.0 |
| Vue Router 5 | JWT (jjwt 0.12.6) 鉴权 | eclipse-temurin:21-jre |
| Axios | BCrypt (spring-security-crypto) | — |
| — | SpringDoc OpenAPI (Swagger UI) | — |

## 功能

- **文章浏览** — 首页最新文章展示、按专题筛选、分页查询
- **文章详情** — Markdown 服务端渲染为 HTML，支持 GFM 表格 / 代码块 / 引用
- **文件上传** — 支持 Markdown 文件上传发布和 JSON 方式发布
- **后台管理** — `/admin` 面板，文章与专题的增删改查
- **登录鉴权** — JWT Token，后端 `/admin/**` 接口受拦截器保护，前端路由守卫自动跳转登录页
- **配置隔离** — JWT 密钥、数据库连接等敏感配置通过 Spring Profile 分离管理，dev / prod 环境独立

## 配置管理

JWT 密钥通过 `@ConfigurationProperties` 绑定 `blog.jwt.admin-secret-key`，从环境配置文件中读取，不再硬编码于代码中：

```yaml
# application-dev.yml
blog:
  jwt:
    admin-secret-key: your-dev-secret-key

# application-prod.yml
blog:
  jwt:
    admin-secret-key: your-prod-secret-key
```

开发环境与生产环境使用独立的配置文件，通过 `spring.profiles.active` 切换。

## 快速开始

### 前置条件

- JDK 17+（Docker 部署需 JDK 21，因 PageHelper 4.0.0 编译目标为 Java 21）
- Node.js 20+
- MySQL 8.0
- Maven

### 启动开发环境

```bash
# 1. 初始化数据库
mysql -u root -p < deploy/sql/init.sql

# 2. 启动后端
cd backend
mvn package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# 3. 启动前端（新终端）
cd frontend
npm install
npm run dev
```

前端运行在 `http://localhost:5173`，后端在 `http://localhost:8080`。

Swagger UI 地址：`http://localhost:8080/swagger-ui.html`

### Docker 部署

源码根目录执行：

```bash
cd deploy
chmod +x build.sh
./build.sh           # 自动构建前后端并复制产物
docker compose up -d
```

或手动上传 `backend.jar` + `frontend-dist/` + `application-prod.yml` 到服务器 `deploy/` 目录后执行 `docker compose up -d`，访问 `http://服务器IP`。

#### 时区

Docker 容器默认 UTC 时间。若服务器位于东八区，需要在 `docker-compose.yml` 中配置：

```yaml
services:
  mysql:
    environment:
      TZ: Asia/Shanghai
  backend:
    environment:
      TZ: Asia/Shanghai
    command: java -jar -Duser.timezone=Asia/Shanghai backend.jar
```

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 见 `deploy/sql/init.sql` 中 BCrypt 加密值对应的明文 |
| 访客 | — | 无需登录 |

## 项目结构

```
AsukaBlog/
├── backend/                        # Spring Boot 后端
│   └── src/main/java/com/asuka/backend/
│       ├── controller/
│       │   ├── user/               # 公开接口（文章浏览、专题列表、文件上传）
│       │   └── admin/              # 管理接口（JWT 保护）
│       ├── service/impl/           # 业务逻辑层
│       ├── mapper/                 # MyBatis Mapper 接口
│       ├── pojo/
│       │   ├── entity/             # 数据库实体
│       │   ├── dto/                # 请求参数
│       │   └── vo/                 # 响应数据
│       ├── properties/             # @ConfigurationProperties 配置绑定（JWT 等）
│       ├── utils/                  # JWT 工具类
│       ├── interceptor/            # JWT Token 拦截器
│       ├── context/                # ThreadLocal 上下文（当前管理员 ID）
│       ├── config/                 # WebMVC 拦截器注册
│       ├── result/                 # 统一响应封装
│       ├── constant/               # 常量
│       └── handler/                # 全局异常处理器
├── frontend/                       # Vue 3 前端
│   └── src/
│       ├── views/
│       │   ├── homepage/           # 首页
│       │   ├── main/               # 主布局（导航栏、滚动隐藏）
│       │   ├── articles/           # 文章列表 & 详情
│       │   └── admin/              # 管理端页面（登录/仪表盘/文章CRUD/专题CRUD）
│       ├── router/                 # 路由定义 + 导航守卫
│       ├── composables/            # API 封装（useArticle, useTopics, useAdmin）
│       ├── utils/                  # Axios 实例 + 请求/响应拦截器
│       └── data/                   # 静态数据
└── deploy/                         # 部署文件
    ├── docker-compose.yml
    ├── build.sh                    # 自动构建脚本
    ├── nginx/default.conf          # Nginx 反向代理配置
    ├── sql/init.sql                # 数据库初始化（建表 + 默认管理员）
    ├── seed-md/                    # 测试文章（Markdown + 批量上传脚本）
    ├── application-prod.yml        # 生产环境配置
    ├── backend.jar                 # 构建产物
    └── frontend-dist/              # 构建产物
```

## API 概览

前端通过 Nginx 代理访问后端，请求路径前缀为 `/api`（如 `/api/user/articles/list`）。下方列出的是后端实际路径。

### 公开接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/articles/list` | 分页文章列表（支持按专题筛选、标题搜索） |
| GET | `/user/articles/detail?id=1` | 文章详情（HTML 渲染内容） |
| POST | `/user/articles/upload` | 上传 Markdown 文件发布文章 |
| POST | `/user/articles/upload/json` | JSON 方式发布文章 |
| GET | `/user/topics/list` | 所有专题列表 |

### 管理接口（需 Token）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/admin/login` | 管理员登录（返回 JWT Token） |
| GET | `/admin/articles/list` | 分页文章列表 |
| GET | `/admin/articles/{id}` | 文章详情（含 Markdown 原文） |
| POST | `/admin/articles` | 新增文章 |
| PUT | `/admin/articles/{id}` | 更新文章 |
| DELETE | `/admin/articles/{id}` | 删除文章 |
| GET | `/admin/topics/list` | 所有专题列表 |
| GET | `/admin/topics/{id}` | 专题详情 |
| POST | `/admin/topics` | 新增专题 |
| PUT | `/admin/topics/{id}` | 更新专题 |
| DELETE | `/admin/topics/{id}` | 删除专题 |

## 许可证

MIT
