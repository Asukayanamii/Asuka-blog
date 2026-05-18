# Docker 容器化部署从入门到精通

## 核心概念

### 镜像 vs 容器

- **镜像（Image）**：只读的模板，包含运行应用所需的全部文件
- **容器（Container）**：镜像的运行实例，可读写

### Dockerfile 指令

```dockerfile
# 基础镜像
FROM openjdk:17-jdk-slim

# 维护者
LABEL maintainer="asuka@example.com"

# 工作目录
WORKDIR /app

# 复制文件
COPY target/app.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 多阶段构建

```dockerfile
# 构建阶段
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Docker Compose

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: myblog
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"
    networks:
      - blog_network

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks:
      - blog_network

  app:
    build: .
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/myblog
    ports:
      - "8080:8080"
    networks:
      - blog_network

volumes:
  mysql_data:

networks:
  blog_network:
    driver: bridge
```

## 优化建议

| 优化项 | 说明 | 效果 |
|--------|------|------|
| 多阶段构建 | 分离构建和运行环境 | 减少镜像体积 80% |
| 使用 Alpine | 更小的基础镜像 | 镜像体积减少 50% |
| 层级缓存 | 不经常变动的指令放前面 | 构建速度提升 5x |

> Docker 已经成为了现代软件部署的标准方式。

![Docker 架构](https://picsum.photos/seed/docker/800/400)
