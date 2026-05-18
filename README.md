# AsukaBlog

基于 Vue 3 + Spring Boot 3 的个人博客系统。支持 Markdown 文章发布、专题分类、后台管理。

## 技术栈

**前端：** Vue 3 (Composition API) + Vite + Element Plus + Vue Router + Axios

**后端：** Spring Boot 3 + MyBatis + MySQL + PageHelper + CommonMark + JWT

## 功能

- **文章浏览** — 首页最新文章展示、按专题筛选、分页查询
- **文章详情** — Markdown → HTML 服务端渲染，代码块/表格/引用完整支持
- **后台管理** — `/admin` 面板，文章与专题的增删改查
- **登录鉴权** — JWT Token，后端 `/admin/**` 接口受保护

## 快速开始

### 前置条件

- JDK 17+
- Node.js 18+
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

前端默认运行在 `http://localhost:5173`，后端在 `http://localhost:8080`。

### Docker 部署

```bash
cd deploy
chmod +x build.sh
./build.sh           # 自动构建前后端并复制产物
docker compose up -d
```

访问 `http://localhost`。

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 用户（浏览） | — | 无需登录 |

## 项目结构

```
AsukaBlog/
├── backend/                     # Spring Boot 后端
│   └── src/main/java/com/asuka/backend/
│       ├── controller/
│       │   ├── user/            # 公开接口
│       │   └── admin/           # 管理接口（JWT 保护）
│       ├── service/             # 业务逻辑层
│       ├── mapper/              # MyBatis 数据访问
│       ├── pojo/entity/         # 实体类
│       ├── pojo/dto/            # 请求 DTO
│       ├── pojo/vo/             # 响应 VO
│       ├── utils/               # JWT 工具类
│       ├── interceptor/         # JWT 拦截器
│       └── config/              # WebMVC 配置
├── frontend/                    # Vue 3 前端
│   └── src/
│       ├── views/               # 页面组件
│       │   ├── homepage/        # 首页
│       │   ├── articles/        # 文章列表 & 详情
│       │   └── admin/           # 管理端页面
│       ├── router/              # 路由配置
│       ├── composables/         # API 封装
│       └── utils/               # Axios 实例 + 拦截器
└── deploy/                      # 部署文件
    ├── docker-compose.yml
    ├── nginx/default.conf
    └── sql/init.sql
```

## API 概览

### 公开接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/articles/list` | 分页文章列表 |
| GET | `/user/articles/detail?id=1` | 文章详情（HTML） |
| GET | `/user/topics/list` | 所有专题 |

### 管理接口（需 Token）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/admin/login` | 登录获取 Token |
| GET | `/admin/articles/list` | 文章列表 |
| GET | `/admin/articles/{id}` | 文章详情（含MD） |
| POST | `/admin/articles` | 新增文章 |
| PUT | `/admin/articles/{id}` | 更新文章 |
| DELETE | `/admin/articles/{id}` | 删除文章 |
| GET | `/admin/topics/list` | 专题列表 |
| POST | `/admin/topics` | 新增专题 |
| PUT | `/admin/topics/{id}` | 更新专题 |
| DELETE | `/admin/topics/{id}` | 删除专题 |

## 许可证

MIT
