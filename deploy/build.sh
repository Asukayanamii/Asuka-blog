#!/usr/bin/env bash
# Build deployable backend and frontend artifacts from any working directory.

set -euo pipefail

DEPLOY_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
PROJECT_DIR="$(dirname "$DEPLOY_DIR")"
BACKEND_DIR="$PROJECT_DIR/backend"
FRONTEND_DIR="$PROJECT_DIR/frontend"

if [[ ! -f "$DEPLOY_DIR/application-prod.yml" ]]; then
  echo "Missing deploy/application-prod.yml. Copy application-prod.yml.example and set production values first." >&2
  exit 1
fi

echo "Building backend..."
(
  cd "$BACKEND_DIR"
  mvn package -DskipTests -q
)
cp "$BACKEND_DIR/target/backend-0.0.1-SNAPSHOT.jar" "$DEPLOY_DIR/backend.jar"

echo "Building frontend..."
(
  cd "$FRONTEND_DIR"
  npm ci --no-audit --no-fund
  npm run build
)
rm -rf "$DEPLOY_DIR/frontend-dist"
cp -R "$FRONTEND_DIR/dist" "$DEPLOY_DIR/frontend-dist"

echo "Build complete. Run: cd deploy && docker compose up -d"
