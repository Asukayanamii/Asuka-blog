# Nginx 配置从入门到精通

## 基础概念

### 正向代理 vs 反向代理

- **正向代理**：代理客户端，隐藏客户端身份
- **反向代理**：代理服务端，隐藏服务器信息

## 核心配置

```nginx
# 全局配置
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

# 事件配置
events {
    worker_connections 1024;
    multi_accept on;
    use epoll;
}

# HTTP 配置
http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    # 日志格式
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

    access_log /var/log/nginx/access.log main;

    # 基础优化
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    types_hash_max_size 2048;
}
```

## 反向代理配置

```nginx
server {
    listen 80;
    server_name blog.example.com;

    # 前端静态资源
    location / {
        root /var/www/blog/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # 超时设置
        proxy_connect_timeout 60s;
        proxy_read_timeout 60s;
        proxy_send_timeout 60s;
    }

    # WebSocket 支持
    location /ws/ {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

## 负载均衡

```nginx
# 上游服务器集群
upstream blog_servers {
    # 轮询（默认）
    server 192.168.1.10:8080 weight=5;
    server 192.168.1.11:8080 weight=3;
    server 192.168.1.12:8080 backup;  # 备用服务器

    # 最少连接
    least_conn;

    # 健康检查
    health_check interval=30s fails=3 passes=2;
}

server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://blog_servers;
    }
}
```

## 性能优化参数

| 参数 | 推荐值 | 说明 |
|------|--------|------|
| worker_processes | auto | CPU 核心数 |
| worker_connections | 10240 | 每个 worker 最大连接数 |
| keepalive_timeout | 65 | 长连接超时 |
| gzip on | on | 开启压缩 |
| client_max_body_size | 10M | 上传文件大小限制 |

> Nginx 是后端开发必备的技能，掌握它能让你的服务更稳定、更高效。

![Nginx 架构](https://picsum.photos/seed/nginx/800/400)
