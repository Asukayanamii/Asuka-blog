# AsukaBlog 个人博客系统

基于 Vue 3 + Spring Boot 3 的个人博客，支持 Markdown 文章发布、专题分类、后台管理。

## 技术栈

| 前端 | 后端 |
|------|------|
| Vue 3 (Composition API) | Spring Boot 3 |
| Vite | MyBatis-Plus |
| Element Plus | MySQL + Redis |
| Vue Router | CommonMark (Markdown 渲染) |
| Axios | JWT 鉴权 |

## 功能

- **文章浏览**：首页展示最新文章，支持按专题筛选、分页
- **文章详情**：Markdown 转 HTML 渲染，代码高亮，响应式布局
- **后台管理**：登录后可进行文章和专题的增删改查
- **JWT 鉴权**：后端 `/admin/**` 接口受 JWT Token 保护

## 快速启动

### 方式一：Docker 一键部署

```bash
# 1. 进入部署目录
cd deploy

# 2. 确保以下文件就位
#    - backend.jar （打包后端）
#    - frontend-dist/ （打包前端）
#    - nginx/default.conf
#    - sql/init.sql

# 3. 启动所有服务
docker compose up -d

# 4. 访问 http://localhost
```

### 方式二：手动启动

**后端：**

```bash
cd backend
mvn package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

**前端：**

```bash
cd frontend
npm install
npm run dev
```

**数据库：**

```bash
mysql -u root -p < deploy/sql/init.sql
```

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |

## 项目结构

```
AsukaBlog/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/asuka/backend/
│   │   ├── controller/         # 控制器
│   │   │   ├── user/           # 公开接口
│   │   │   └── admin/          # 管理接口（需登录）
│   │   ├── service/            # 业务逻辑
│   │   ├── mapper/             # 数据访问
│   │   ├── pojo/               # 实体/DTO/VO
│   │   ├── utils/              # 工具类 (JWT)
│   │   ├── interceptor/        # 拦截器 (JWT 验证)
│   │   ├── config/             # 配置
│   │   └── context/            # 线程上下文
│   └── src/main/resources/
│       ├── mapper/             # MyBatis XML
│       └── application.yml     # 配置
├── frontend/                   # Vue 3 前端
│   └── src/
│       ├── views/              # 页面
│       ├── router/             # 路由
│       ├── composables/        # API 封装
│       └── utils/              # Axios 实例
└── deploy/                     # 部署文件
    ├── docker-compose.yml
    ├── nginx/default.conf
    └── sql/init.sql
```
