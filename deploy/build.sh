#!/bin/bash
# 构建前后端并复制产物到 deploy 目录

set -e

echo "=== 构建后端 ==="
cd ../backend
mvn package -DskipTests -q
cp target/backend-0.0.1-SNAPSHOT.jar ../deploy/backend.jar
echo "✅ 后端构建完成"

echo "=== 构建前端 ==="
cd ../frontend
npm run build > /dev/null 2>&1
rm -rf ../deploy/frontend-dist
cp -r dist ../deploy/frontend-dist
echo "✅ 前端构建完成"

echo "=== 构建完成，可以执行 docker compose up -d ==="
