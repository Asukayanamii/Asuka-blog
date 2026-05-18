# 前端性能优化 checklist

## 加载优化

### 资源加载策略

```html
<!-- 预加载关键资源 -->
<link rel="preload" href="font.woff2" as="font" crossorigin>

<!-- 预连接第三方域名 -->
<link rel="preconnect" href="https://api.example.com">

<!-- 预获取下一页 -->
<link rel="prefetch" href="/next-page">
```

### 图片优化

```html
<!-- 使用 WebP 格式 -->
<picture>
  <source srcset="image.webp" type="image/webp">
  <img src="image.jpg" alt="optimized">
</picture>

<!-- 懒加载 -->
<img loading="lazy" src="image.jpg" alt="lazy">

<!-- 响应式图片 -->
<img srcset="small.jpg 400w, medium.jpg 800w, large.jpg 1200w"
     sizes="(max-width: 600px) 400px, (max-width: 1200px) 800px, 1200px"
     src="fallback.jpg">
```

## 渲染优化

### 减少重排重绘

```css
/* 使用 transform 代替 top/left */
.element {
  transform: translateX(100px);  /* ✅ 只触发合成 */
  /* left: 100px; */             /* ❌ 触发重排 */
}

/* 使用 opacity 代替 visibility */
.fade {
  opacity: 0;  /* ✅ 只触发合成 */
}
```

## 性能指标

| 指标 | 好 | 一般 | 差 |
|------|-----|------|-----|
| FCP | < 1.8s | 1.8-3s | > 3s |
| LCP | < 2.5s | 2.5-4s | > 4s |
| FID | < 100ms | 100-300ms | > 300ms |
| CLS | < 0.1 | 0.1-0.25 | > 0.25 |

## 检测工具

- **Lighthouse** — 综合性能评分
- **Web Vitals** — 核心 Web 指标监测
- **Chrome DevTools Performance** — 性能分析
- **BundlePhobia** — 包体积分析

> 性能优化是一个持续的过程，不是一次性任务。

![性能优化](https://picsum.photos/seed/performance/800/400)
