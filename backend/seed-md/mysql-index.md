# MySQL 索引优化与查询性能调优

## 索引基础

### B+ Tree 索引结构

MySQL 的 InnoDB 引擎使用 B+ Tree 作为索引结构：

- 非叶子节点只存储索引键
- 叶子节点存储完整数据或主键值
- 叶子节点之间通过双向链表连接

### 索引类型

```sql
-- 主键索引
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100)
);

-- 普通索引
CREATE INDEX idx_name ON users(name);

-- 联合索引
CREATE INDEX idx_name_age ON users(name, age);

-- 唯一索引
CREATE UNIQUE INDEX idx_email ON users(email);

-- 全文索引
CREATE FULLTEXT INDEX idx_content ON articles(content);
```

## 执行计划分析

```sql
EXPLAIN SELECT * FROM users WHERE name = 'Asuka'\G
```

重点关注以下字段：

| 字段 | 说明 | 优化目标 |
|------|------|----------|
| type | 访问类型 | const > ref > range > index > ALL |
| key | 实际使用索引 | 避免为 NULL |
| rows | 扫描行数 | 越小越好 |
| Extra | 额外信息 | 避免 Using filesort |

## 索引优化原则

### 最左前缀法则

> 联合索引 `(a, b, c)` 生效规则：

```sql
WHERE a = 1           -- ✅ 生效
WHERE a = 1 AND b = 2 -- ✅ 生效
WHERE b = 2           -- ❌ 不生效
WHERE a = 1 AND c = 3 -- ✅ a 生效，c 不生效
```

### 覆盖索引

```sql
-- 如果索引包含所有查询字段，无需回表
CREATE INDEX idx_cover ON orders(user_id, status, amount);

-- 以下查询只需扫描索引
SELECT user_id, status, amount FROM orders WHERE user_id = 100;
```

## 慢查询优化实战

### 案例一：分页偏移量过大

```sql
-- ❌ 大偏移量导致大量 IO
SELECT * FROM articles LIMIT 100000, 20;

-- ✅ 使用子查询优化
SELECT * FROM articles
WHERE id > (SELECT id FROM articles ORDER BY id LIMIT 100000, 1)
LIMIT 20;

-- ✅ 使用游标分页
SELECT * FROM articles WHERE id > :last_id ORDER BY id LIMIT 20;
```

### 案例二：ORDER BY 优化

```sql
-- ❌ 产生文件排序
SELECT * FROM articles ORDER BY create_time DESC;

-- ✅ 利用索引排序，需建立对应索引
CREATE INDEX idx_create_time ON articles(create_time);
```

## SQL 优化 checklist

- [x] 避免使用 SELECT *
- [x] 小表驱动大表
- [x] 合理使用 JOIN，避免超过三表关联
- [ ] 避免在 WHERE 条件中对字段进行函数操作
- [ ] 使用 EXISTS 代替 IN（子查询结果集较大时）

> 索引不是越多越好，每个索引都会增加写入开销。

![MySQL 索引结构](https://picsum.photos/seed/mysql/800/400)
