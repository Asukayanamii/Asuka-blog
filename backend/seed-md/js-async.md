# JavaScript 异步编程进化史

## 引言

异步编程是 JavaScript 的核心特性之一。本文将回顾其发展历程，从回调函数到 Promise，再到优雅的 async/await。

## 第一阶段：回调函数

```javascript
// 典型的回调地狱
getUserData(userId, (err, user) => {
  if (err) {
    console.error('Failed to get user', err)
    return
  }
  getOrders(user.id, (err, orders) => {
    if (err) {
      console.error('Failed to get orders', err)
      return
    }
    getOrderDetails(orders[0].id, (err, details) => {
      if (err) {
        console.error('Failed to get details', err)
        return
      }
      console.log(details)
    })
  })
})
```

## 第二阶段：Promise

```javascript
// Promise 链式调用
getUserData(userId)
  .then(user => getOrders(user.id))
  .then(orders => getOrderDetails(orders[0].id))
  .then(details => console.log(details))
  .catch(err => console.error(err))
  .finally(() => console.log('完成'))
```

### Promise 静态方法

```javascript
// 并行执行
Promise.all([fetchUsers(), fetchPosts(), fetchComments()])
  .then(([users, posts, comments]) => {
    console.log({ users, posts, comments })
  })

// 竞速执行
Promise.race([fetchFromServer(), fetchFromCache()])
  .then(result => console.log('最快的结果', result))

// 全部完成（不关心结果）
Promise.allSettled([syncA(), syncB(), syncC()])
  .then(results => {
    results.forEach(r => {
      if (r.status === 'fulfilled') console.log(r.value)
      if (r.status === 'rejected') console.log(r.reason)
    })
  })
```

## 第三阶段：async/await

```javascript
async function getUserOrders(userId) {
  try {
    const user = await getUserData(userId)
    const orders = await getOrders(user.id)
    const details = await getOrderDetails(orders[0].id)
    return details
  } catch (error) {
    console.error('操作失败', error)
    throw error
  }
}
```

### 并发与串行

```javascript
// ❌ 串行执行（慢）
async function loadSerial() {
  const a = await fetchA()
  const b = await fetchB()  // 等 fetchA 完成才执行
  const c = await fetchC()  // 等 fetchB 完成才执行
}

// ✅ 并发执行（快）
async function loadParallel() {
  const [a, b, c] = await Promise.all([
    fetchA(),
    fetchB(),
    fetchC()
  ])
}
```

## 性能对比

| 方式 | 可读性 | 错误处理 | 并发控制 |
|------|--------|----------|----------|
| 回调 | ❌ | ❌ | ❌ |
| Promise | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| async/await | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

> 理解异步编程的演进有助于写出更优雅、更健壮的 JavaScript 代码。

![JavaScript 异步编程](https://picsum.photos/seed/javascript/800/400)
