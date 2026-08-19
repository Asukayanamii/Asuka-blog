# 部署说明

此目录包含 Docker Compose 运行所需的 Nginx 配置、数据库初始化脚本和构建脚本。

## 首次部署

```bash
cp .env.example .env
cp application-prod.yml.example application-prod.yml
```

编辑 `.env` 的 `MYSQL_ROOT_PASSWORD`，并在 `application-prod.yml` 设置高强度 `admin-secret-key`。两个文件均已被 Git 忽略。

```bash
chmod +x build.sh
./build.sh
docker compose up -d
```

`build.sh` 会生成 `backend.jar` 和 `frontend-dist/`。Nginx 提供静态文件、Vue Router history 回退和 `/api` 到后端的代理。

## 日常操作

```bash
docker compose ps
docker compose logs -f backend
docker compose restart frontend
docker compose down
```

`docker compose down -v` 会删除 MySQL 数据卷和全部博客数据，执行前确认已备份。
