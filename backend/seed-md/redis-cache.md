# Redis 缓存策略与实战

## 缓存基础

### Redis 数据结构

| 类型 | 说明 | 使用场景 |
|------|------|----------|
| String | 字符串 | 缓存单个值、计数器 |
| Hash | 哈希 | 存储对象属性 |
| List | 列表 | 消息队列、最新列表 |
| Set | 集合 | 标签、去重 |
| ZSet | 有序集合 | 排行榜、延时队列 |

## 缓存三大问题

### 缓存穿透

> 请求一个不存在的数据，缓存和数据库中都没有，导致请求直接打到数据库。

**解决方案：**

```java
// 1. 缓存空值
public Object getData(String key) {
    Object cache = redisTemplate.opsForValue().get(key);
    if (cache != null) {
        if (NULL_MARK.equals(cache)) return null;
        return cache;
    }

    Object data = database.query(key);
    if (data == null) {
        // 缓存空值，过期时间较短
        redisTemplate.opsForValue().set(key, NULL_MARK, 60, TimeUnit.SECONDS);
    } else {
        redisTemplate.opsForValue().set(key, data, 3600, TimeUnit.SECONDS);
    }
    return data;
}
```

### 缓存击穿

> 热点 key 在失效的瞬间，大量请求同时涌入数据库。

```java
// 2. 互斥锁
public Object getDataWithLock(String key) {
    Object cache = redisTemplate.opsForValue().get(key);
    if (cache != null) return cache;

    String lockKey = "lock:" + key;
    Boolean locked = redisTemplate.opsForValue()
        .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

    if (Boolean.TRUE.equals(locked)) {
        try {
            Object data = database.query(key);
            redisTemplate.opsForValue().set(key, data, 3600, TimeUnit.SECONDS);
            return data;
        } finally {
            redisTemplate.delete(lockKey);
        }
    } else {
        Thread.sleep(100);
        return getDataWithLock(key);
    }
}
```

### 缓存雪崩

> 大量缓存同时失效，导致数据库瞬间压力过大。

**解决方案：**

- 设置随机过期时间
- 使用集群或主从架构
- 限流降级

```java
// 3. 随机过期时间
long expireTime = baseTime + ThreadLocalRandom.current().nextLong(3600);
redisTemplate.opsForValue().set(key, data, expireTime, TimeUnit.SECONDS);
```

## Spring Boot 整合 Redis

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

## 总结

缓存是提升系统性能的重要手段，但需要注意缓存一致性、内存使用等问题。合理使用缓存可以让系统性能提升 10 倍以上。

![Redis 缓存架构](https://picsum.photos/seed/redis/800/400)
