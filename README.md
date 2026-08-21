# AsukaBlog

基于 Vue 3 与 Spring Boot 的个人博客系统。前台提供文章阅读、专题筛选、响应式导航、夜间模式和文章目录浮窗；管理端提供文章与专题的登录后维护。

在线站点：[asukayanami.top](https://asukayanami.top/)

## 功能

- 文章列表按更新时间分页，并支持专题筛选
- Markdown 在服务端转换为 HTML，文章详情自动生成可拖拽的目录浮窗
- 首页、文章归档、关于页和管理端均支持日间与夜间模式，主题选择会保留
- 后台文章与专题的新增、编辑、删除，以及 Markdown 文件或 JSON 上传接口
- JWT 保护 `/admin/**` 接口，前端路由守卫保护管理页面
- Nginx 提供 SPA 路由回退与 `/api` 反向代理

> 当前文章数据结构没有封面字段。首页文章卡片使用统一 Hero 图片；要让每篇文章使用独立封面，需要同时扩展数据库、管理端和文章接口。

## 技术栈

| 前端 | 后端 | 部署 |
| --- | --- | --- |
| Vue 3、Vue Router、Vite | Spring Boot 4、MyBatis、PageHelper | Docker Compose、Nginx、MySQL 8 |
| Element Plus、Axios | Flexmark、JLaTeXMath、阿里云 OSS、JWT、BCrypt | Eclipse Temurin 21 JRE |

## 项目结构

```text
AsukaBlog/
├── frontend/                         # Vue 前端
│   ├── public/                       # Hero、头像和静态资源
│   └── src/
│       ├── composables/              # 接口与主题状态
│       ├── router/                   # 前台、后台路由和鉴权守卫
│       ├── views/articles/           # 文章列表、详情与目录浮窗
│       ├── views/homepage/           # 首页
│       └── views/admin/              # 登录、控制台、文章和专题管理
├── backend/                          # Spring Boot 后端
│   └── src/main/
│       ├── java/com/asuka/backend/   # controller、service、mapper、JWT
│       └── resources/                # MyBatis Mapper 与应用配置
└── deploy/                           # Docker、Nginx、数据库初始化和构建脚本
```

## 本地开发

前置条件：JDK 17+、Maven、Node.js 20+、MySQL 8。

1. 创建开发配置并填写本地数据库密码：

```bash
cp backend/src/main/resources/application-dev.yml.example backend/src/main/resources/application-dev.yml
```

2. 初始化数据库：

```bash
mysql -u root -p < deploy/sql/init.sql
```

3. 启动后端：

```bash
cd backend
mvn spring-boot:run
```

4. 在另一个终端启动前端：

```bash
cd frontend
npm install
npm run dev
```

前端默认地址为 `http://localhost:5173`，Vite 会把 `/api` 代理到 `http://localhost:8080`。Swagger UI 为 `http://localhost:8080/swagger-ui.html`。

## Docker 部署

前置条件：Docker Engine 与 Docker Compose Plugin。

1. 准备不提交到仓库的生产配置：

```bash
cp deploy/.env.example deploy/.env
cp deploy/application-prod.yml.example deploy/application-prod.yml
```

编辑 `deploy/.env` 中的 `MYSQL_ROOT_PASSWORD`，并在 `deploy/application-prod.yml` 设置高强度的 `admin-secret-key`。生产配置中的数据库密码通过 `${MYSQL_ROOT_PASSWORD}` 从容器环境读取。

2. 构建前后端产物：

```bash
cd deploy
chmod +x build.sh
./build.sh
```

3. 启动服务：

```bash
docker compose up -d
docker compose ps
```

站点通过 `http://服务器地址` 访问。MySQL 暴露在 `3306`，后端暴露在 `8080`，便于维护和访问 Swagger；公网部署时请按需在防火墙或 Compose 文件中限制这两个端口。

停止服务但保留数据库数据：

```bash
docker compose down
```

删除数据库卷会清空所有文章、专题与管理员数据：

```bash
docker compose down -v
```

## 默认管理员

数据库初始化脚本会创建以下账号。首次部署后应及时修改密码。

| 用户名 | 密码 |
| --- | --- |
| `admin` | `admin123` |

## 接口

前端请求使用 `/api` 前缀，Nginx 或 Vite 会转发到后端；下表展示后端实际路径。

| 范围 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 公开 | GET | `/user/topics/list` | 专题列表 |
| 公开 | GET | `/user/articles/list` | 分页文章列表，可传 `pageNum`、`pageSize`、`topicId`、`title` |
| 公开 | GET | `/user/articles/detail?id={id}` | 文章详情与 HTML 内容 |
| 管理 | POST | `/admin/login` | 管理员登录，返回 JWT |
| 管理 | GET/POST/PUT/DELETE | `/admin/articles/**` | 文章管理与 Markdown 上传 |
| 管理 | GET/POST/PUT/DELETE | `/admin/topics/**` | 专题管理 |

## 验证

前端生产构建：

```bash
cd frontend
npm run build
```

后端测试：

```bash
cd backend
./mvnw test
```

Windows 可使用 `mvnw.cmd test`。

## License

MIT
