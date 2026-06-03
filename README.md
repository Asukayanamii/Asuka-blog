# AsukaBlog

基于 Vue 3 + Spring Boot 4 的个人博客系统。支持 Markdown 文章发布、专题分类、后台管理，通过 Docker Compose 一键部署。

## 技术栈

| 前端 | 后端 | 部署 |
|------|------|------|
| Vue 3.5 + Composition API | Spring Boot 4.0.6 | Docker Compose |
| Vite 8 | MyBatis + PageHelper | Nginx（静态资源 + API 反向代理） |
| Element Plus 2.14 | CommonMark 0.21（Markdown → HTML） | MySQL 8.0 |
| Vue Router 5 | JWT（jjwt 0.12.6）鉴权 | eclipse-temurin:21-jre |
| Axios | BCrypt（spring-security-crypto） | — |

## 特性

- **文章系统** — 支持 Markdown 发布、服务端渲染 HTML、按专题筛选、分页查询
- **文件上传** — 支持 .md 文件上传发布及 JSON 方式发布
- **后台管理** — `/admin` 面板，文章与专题的增删改查，响应式适配手机端
- **登录鉴权** — JWT Token 保护管理端接口，前端路由守卫自动跳转登录页
- **配置隔离** — JWT 密钥、数据库连接等通过 Spring Profile 分离管理，dev / prod 环境独立
- **响应式前端** — 前台首页、文章详情、关于页及管理端均适配手机与平板

## 快速开始

### 前置条件

- JDK 17+（Docker 部署需 JDK 21）
- Node.js 20+
- MySQL 8.0
- Maven

### 本地开发

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

Swagger UI：`http://localhost:8080/swagger-ui.html`

### Docker 部署

```bash
cd deploy
chmod +x build.sh
./build.sh                  # 自动构建前后端并复制产物
docker compose up -d        # 启动所有服务
```

或手动上传 `backend.jar` + `frontend-dist/` + `application-prod.yml` 到服务器 `deploy/` 目录后执行 `docker compose up -d`，访问 `http://服务器IP`。

> **时区**：Docker 容器默认 UTC。若在东八区请在 `docker-compose.yml` 中配置 `TZ: Asia/Shanghai` 及 `-Duser.timezone=Asia/Shanghai`。

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 见 `deploy/sql/init.sql` BCrypt 值对应明文 |
| 访客 | — | 无需登录 |

## 项目结构

```
AsukaBlog/
├── backend/                          # Spring Boot 后端
│   └── src/main/java/com/asuka/backend/
│       ├── controller/
│       │   ├── user/                 # 公开接口（文章浏览、专题列表、文件上传）
│       │   └── admin/                # 管理接口（JWT 保护）
│       ├── service/impl/             # 业务逻辑层
│       ├── mapper/                   # MyBatis Mapper 接口
│       ├── pojo/
│       │   ├── entity/               # 数据库实体
│       │   ├── dto/                  # 请求参数
│       │   └── vo/                   # 响应数据
│       ├── properties/               # @ConfigurationProperties 配置绑定
│       ├── utils/                    # JWT 工具类
│       ├── interceptor/              # JWT Token 拦截器
│       ├── context/                  # ThreadLocal 上下文
│       ├── config/                   # WebMVC 拦截器注册
│       ├── result/                   # 统一响应封装
│       ├── constant/                 # 常量
│       └── handler/                  # 全局异常处理器
├── frontend/                         # Vue 3 前端
│   └── src/
│       ├── views/
│       │   ├── homepage/             # 首页（Hero、专题、最新文章、联系我）
│       │   ├── main/                 # 主布局（导航栏、滚动隐藏）
│       │   ├── articles/             # 文章列表与详情
│       │   ├── about/                # 关于页（联系方式、社交平台）
│       │   └── admin/                # 管理端（登录、控制台、文章/专题 CRUD）
│       ├── router/                   # 路由定义 + 导航守卫
│       ├── composables/              # API 封装
│       ├── utils/                    # Axios 实例 + 拦截器
│       └── data/                     # 静态数据
└── deploy/                           # 部署文件
    ├── docker-compose.yml
    ├── build.sh                      # 自动构建脚本
    ├── nginx/default.conf            # Nginx 反向代理配置
    ├── sql/init.sql                  # 数据库初始化（建表 + 默认管理员）
    ├── seed-md/                      # 测试文章
    ├── application-prod.yml          # 生产环境配置
    ├── backend.jar                   # 构建产物
    └── frontend-dist/                # 构建产物
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
