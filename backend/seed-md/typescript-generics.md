# TypeScript 泛型编程从入门到精通

## 什么是泛型

泛型允许我们在定义函数、接口或类时，不预先指定具体的类型，而是在使用时再确定。这大大提高了代码的复用性和类型安全性。

## 基础用法

### 泛型函数

```typescript
function identity<T>(arg: T): T {
  return arg
}

// 显式指定类型
const result = identity<string>('Hello')

// 类型推断
const result2 = identity(42)
```

### 泛型接口

```typescript
interface Repository<T> {
  findById(id: string): T | undefined
  findAll(): T[]
  save(entity: T): void
  delete(id: string): void
}

// 实现泛型接口
class UserRepository implements Repository<User> {
  private users: User[] = []
  
  findById(id: string): User | undefined {
    return this.users.find(u => u.id === id)
  }
  
  findAll(): User[] {
    return this.users
  }
  
  save(entity: User): void {
    this.users.push(entity)
  }
  
  delete(id: string): void {
    this.users = this.users.filter(u => u.id !== id)
  }
}
```

## 高级技巧

### 泛型约束

```typescript
interface HasLength {
  length: number
}

function logLength<T extends HasLength>(arg: T): T {
  console.log(arg.length)
  return arg
}

logLength('hello')     // ✅ 5
logLength([1, 2, 3])   // ✅ 3
// logLength(42)       // ❌ 类型错误
```

### 条件类型

```typescript
type IsString<T> = T extends string ? true : false

type A = IsString<'hello'>  // true
type B = IsString<42>         // false

// 高级条件类型
type ArrayElement<T> = T extends (infer U)[] ? U : never
type C = ArrayElement<string[]>  // string
type D = ArrayElement<number[]>  // number
```

### 映射类型

```typescript
type Readonly<T> = {
  readonly [P in keyof T]: T[P]
}

type Optional<T> = {
  [P in keyof T]?: T[P]
}

// 实际应用
interface User {
  id: number
  name: string
  email: string
}

type ReadonlyUser = Readonly<User>
// { readonly id: number; readonly name: string; readonly email: string }
```

## 实战案例

> 以下是一个完整的 API 响应封装示例：

```typescript
interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: number
}

async function fetchApi<T>(url: string): Promise<ApiResponse<T>> {
  const response = await fetch(url)
  return response.json()
}

// 使用示例
interface Article {
  id: number
  title: string
  content: string
}

const article = await fetchApi<Article>('/api/articles/1')
console.log(article.data.title)
```

## 总结

| 特性 | 用途 | 难度 |
|------|------|------|
| 泛型函数 | 函数级别复用 | ⭐⭐ |
| 泛型接口 | 接口级别抽象 | ⭐⭐⭐ |
| 条件类型 | 类型级编程 | ⭐⭐⭐⭐⭐ |

![TypeScript 泛型图示](https://picsum.photos/seed/typescript/800/400)
