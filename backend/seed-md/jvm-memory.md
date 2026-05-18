# JVM 内存模型与垃圾回收机制详解

## JVM 内存区域

### 堆（Heap）

堆是 JVM 管理的最大一块内存区域，所有对象实例都在这里分配：

- **新生代**（Young Generation）：Eden、Survivor From、Survivor To
- **老年代**（Old Generation）：长期存活的对象
- **元空间**（Metaspace）：类元数据（JDK 8+ 替代永久代）

```bash
# JVM 常用参数
-Xms2g -Xmx2g                    # 堆大小
-Xmn512m                         # 新生代大小
-XX:MetaspaceSize=256m           # 元空间大小
-XX:+UseG1GC                     # 使用 G1 垃圾回收器
-XX:MaxGCPauseMillis=200         # GC 最大停顿时间
```

### 栈（Stack）

每个线程创建时都会创建一个虚拟机栈：

```java
public class StackExample {
    public static void main(String[] args) {
        System.out.println(factorial(10000));
    }

    static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
}
```

## 垃圾回收算法

| 算法 | 原理 | 优缺点 |
|------|------|--------|
| 标记-清除 | 标记存活对象，清除未标记对象 | 产生内存碎片 |
| 标记-复制 | 将存活对象复制到另一块区域 | 浪费空间，但无碎片 |
| 标记-整理 | 标记存活对象，向一端移动 | 效率较低，无碎片 |

### 分代回收

```text
┌─────────────────────────────────────────┐
│                 堆空间                    │
│  ┌──────┬────┬────┐  ┌──────────────┐   │
│  │ Eden │ S0 │ S1 │  │   Old Gen    │   │
│  └──────┴────┴────┘  └──────────────┘   │
│      新生代               老年代          │
└─────────────────────────────────────────┘
```

## GC 日志分析

```bash
# GC 日志参数
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/path/to/gc.log
```

> GC 日志中 YGC 表示新生代 GC，FGC 表示 Full GC。

## 调优经验

1. **避免手动调用 System.gc()** — 这会触发 Full GC，影响性能
2. **合理设置堆大小** — 过大会增加 GC 暂停时间，过小会导致频繁 GC
3. **选择合适的 GC 收集器** — 响应优先用 G1，吞吐优先用 Parallel

## 内存泄漏排查

```bash
# 使用 jmap 查看堆内存
jmap -heap <pid>

# 生成堆转储文件
jmap -dump:live,format=b,file=heap.hprof <pid>

# 使用 jstat 监控 GC
jstat -gcutil <pid> 1000 10
```

![JVM 架构图](https://picsum.photos/seed/jvm/800/400)
