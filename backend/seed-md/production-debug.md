# 一次 Spring Boot 生产环境问题排查记录

## 问题现象

服务响应时间从 50ms 飙升到 5s+，部分请求超时。

## 排查过程

### 第一步：查看日志

```bash
# 查看应用日志
tail -500f /var/log/app/app.log

# 发现大量超时日志
2026-04-20 14:23:15 ERROR [http-nio-8080-exec-48] -
Timeout waiting for connection from pool
```

### 第二步：检查数据库连接池

```yaml
# 原配置
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
```

连接池只有 10 个连接，显然不够。

### 第三步：分析慢查询

```sql
-- 发现一条慢查询执行了 12 秒
SELECT * FROM orders
WHERE status = 0
  AND create_time > DATE_SUB(NOW(), INTERVAL 30 DAY)
ORDER BY create_time DESC;
```

### 第四步：检查线程状态

```bash
# 查看 Java 线程
jstack <pid> > thread_dump.txt

# 大量线程处于 BLOCKED 状态
"http-nio-8080-exec-45" #45 prio=5 os_prio=0 tid=0x...
    java.lang.Thread.State: BLOCKED
        at com.zaxxer.hikari.pool.HikariPool.getConnection
```

## 问题根因

| 问题 | 原因 | 影响 |
|------|------|------|
| 连接池过小 | maximum-pool-size=10 | 请求排队等待连接 |
| 慢查询 | 缺少索引，全表扫描 | 单个查询耗时 12s |
| 线程阻塞 | SQL 慢 → 连接被占满 | 服务不可用 |

## 解决方案

### 1. 增加连接池

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 50
      minimum-idle: 10
      connection-timeout: 5000
```

### 2. 优化慢查询

```sql
CREATE INDEX idx_status_create ON orders(status, create_time);
```

## 事后总结

> 排查线上问题的核心方法是：先止血（恢复服务），再找因（定位根因），最后根治（彻底解决）。

![排查过程流程图](https://picsum.photos/seed/debug/800/400)
