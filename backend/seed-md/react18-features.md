# React 18 新特性全面解析

## 概述

React 18 引入了许多令人兴奋的新特性，标志着 React 进入了一个新的时代。从并发渲染到自动批处理，这些特性将极大地改善用户体验和开发效率。

## 并发模式

### useTransition

```jsx
function SearchPage() {
  const [isPending, startTransition] = useTransition()
  const [query, setQuery] = useState('')

  function handleChange(e) {
    startTransition(() => {
      setQuery(e.target.value)
    })
  }

  return (
    <div>
      {isPending && <Spinner />}
      <SearchResults query={query} />
    </div>
  )
}
```

### useDeferredValue

```jsx
function SearchPage({ query }) {
  const deferredQuery = useDeferredValue(query)
  const isPending = query !== deferredQuery

  return (
    <div>
      {isPending && <Spinner />}
      <SearchResults query={deferredQuery} />
    </div>
  )
}
```

## 自动批处理

React 18 默认启用自动批处理：

```jsx
function handleClick() {
  // React 18 中，这两次 setState 会被自动批处理
  setCount(c => c + 1)
  setFlag(f => !f)
  // 只会触发一次重渲染
}

// 在 Promise、setTimeout 中也生效
fetch('/api/data').then(() => {
  setData(result)    // 自动批处理
  setLoading(false)  // 自动批处理
})
```

## Suspense 改进

```jsx
<Suspense fallback={<Loading />}>
  <AsyncComponent />
  <AnotherAsyncComponent />
</Suspense>
```

## 性能对比

| 特性 | React 17 | React 18 |
|------|----------|----------|
| 渲染模式 | 同步 | 并发可选 |
| 批处理 | 仅事件处理 | 默认全部 |
| Suspense | 基础支持 | 完整支持 |
| SSR | 传统模式 | 流式 SSR |

## 迁移指南

> 从 React 17 升级到 18 通常是无痛的，只需将 `ReactDOM.render` 替换为 `createRoot`：

```jsx
// React 17
ReactDOM.render(<App />, document.getElementById('root'))

// React 18
const root = createRoot(document.getElementById('root'))
root.render(<App />)
```

![React 18 架构图](https://picsum.photos/seed/react18/800/400)
