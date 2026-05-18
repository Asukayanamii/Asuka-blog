# Vue 3 组合式 API 深入实践

## 前言

Vue 3 的 Composition API 为我们提供了一种全新的逻辑组织方式。本文将深入探讨其核心概念和最佳实践。

## 核心 API 详解

### ref 与 reactive

```javascript
import { ref, reactive, computed } from 'vue'

// ref 用于基本类型
const count = ref(0)
console.log(count.value) // 0

// reactive 用于对象
const state = reactive({
  name: 'Asuka',
  age: 25
})
```

### computed 计算属性

```javascript
const doubled = computed(() => count.value * 2)

// 可写计算属性
const fullName = computed({
  get: () => `${firstName.value} ${lastName.value}`,
  set: (val) => {
    [firstName.value, lastName.value] = val.split(' ')
  }
})
```

## 生命周期对比

| Options API | Composition API |
|-------------|-----------------|
| `beforeCreate` | `setup()` |
| `created` | `setup()` |
| `mounted` | `onMounted()` |
| `updated` | `onUpdated()` |
| `unmounted` | `onUnmounted()` |

## 最佳实践

1. **逻辑拆分**：将相关逻辑封装到独立的 composable 函数中
2. **类型安全**：优先使用 TypeScript
3. **副作用管理**：使用 `watchEffect` 自动追踪依赖

## watchEffect 自动追踪

```javascript
const searchQuery = ref('')
const results = ref([])

// 自动追踪所有用到的响应式依赖
watchEffect(async () => {
  if (searchQuery.value) {
    results.value = await fetchResults(searchQuery.value)
  }
})
```

## 自定义 Hook 示例

```javascript
// useDebounce.js
export function useDebounce(value, delay = 300) {
  const debouncedValue = ref(value.value)
  
  let timer
  watch(value, (newVal) => {
    clearTimeout(timer)
    timer = setTimeout(() => {
      debouncedValue.value = newVal
    }, delay)
  })
  
  return debouncedValue
}
```

> Composition API 不是用来替代 Options API 的，而是提供更灵活的逻辑复用方式。在大型项目中，它能让代码组织更加清晰。

## 总结

Composition API 让代码组织更加灵活，特别是在大型项目中优势更为明显。配合 TypeScript 使用，能获得更好的类型推导和开发体验。

![Vue 3 架构图](https://picsum.photos/seed/vue3/800/400)
