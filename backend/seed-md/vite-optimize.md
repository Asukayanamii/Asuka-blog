# Vite 构建优化实战

## 为什么选择 Vite

Vite 基于原生 ES 模块，开发服务器启动速度极快，热更新几乎是即时的。

## 基础配置

```javascript
// vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['vue', 'vue-router', 'pinia'],
          ui: ['element-plus']
        }
      }
    }
  }
})
```

## 代码分割策略

### 路由懒加载

```javascript
const routes = [
  {
    path: '/home',
    component: () => import('./views/Home.vue')
  },
  {
    path: '/about',
    component: () => import('./views/About.vue')
  }
]
```

### 第三方库分离

```javascript
build: {
  rollupOptions: {
    output: {
      manualChunks(id) {
        if (id.includes('node_modules')) {
          if (id.includes('vue')) return 'vue'
          if (id.includes('axios')) return 'axios'
          if (id.includes('lodash')) return 'lodash'
          return 'vendor'
        }
      }
    }
  }
}
```

## 压缩优化

```bash
# 安装压缩插件
npm install -D vite-plugin-compression

# 配置
import viteCompression from 'vite-plugin-compression'

plugins: [
  viteCompression({
    algorithm: 'gzip',
    threshold: 10240,    // 10KB 以上才压缩
    deleteOriginFile: false
  })
]
```

## 构建分析

```bash
# 使用 rollup-plugin-visualizer 分析包体积
npm install -D rollup-plugin-visualizer
```

```javascript
import { visualizer } from 'rollup-plugin-visualizer'

plugins: [
  visualizer({
    open: true,
    filename: 'stats.html'
  })
]
```

## 优化前后对比

| 指标 | 优化前 | 优化后 |
|------|--------|--------|
| 初始体积 | 1.2MB | 380KB |
| 首屏加载 | 3.2s | 0.8s |
| 构建时间 | 45s | 28s |

> 构建优化是一个持续的过程，需要根据项目实际情况进行调整。

![Vite 构建优化](https://picsum.photos/seed/vite/800/400)
