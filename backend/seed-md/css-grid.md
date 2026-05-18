# CSS Grid 布局完全指南

## 什么是 Grid

CSS Grid 是一个二维布局系统，可以同时控制行和列。它让复杂的页面布局变得简单直观。

## 基础概念

### Grid 容器

```css
.container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: auto;
  gap: 20px;
}
```

### Grid 项目

```css
.item {
  grid-column: 1 / 3;   /* 跨越两列 */
  grid-row: 1 / 2;       /* 占据一行 */
}
```

## 布局模板

### 三列等宽布局

```css
.three-col {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 24px;
}
```

### 圣杯布局

```css
.holy-grail {
  display: grid;
  grid-template-areas:
    "header header header"
    "nav    main   aside"
    "footer footer footer";
  grid-template-columns: 200px 1fr 200px;
  grid-template-rows: auto 1fr auto;
  min-height: 100vh;
}

.header { grid-area: header; }
.nav    { grid-area: nav; }
.main   { grid-area: main; }
.aside  { grid-area: aside; }
.footer { grid-area: footer; }
```

## 对齐方式

```css
.container {
  /* 主轴对齐 */
  justify-items: center;
  justify-content: center;

  /* 交叉轴对齐 */
  align-items: center;
  align-content: center;

  /* 单个项目对齐 */
  justify-self: end;
  align-self: start;
}
```

## 响应式设计

```css
/* 自动填充，自适应列数 */
.responsive-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

/* 媒体查询断点 */
@media (max-width: 768px) {
  .responsive-grid {
    grid-template-columns: 1fr;
  }
}
```

## Grid vs Flexbox

| 特性 | Grid | Flexbox |
|------|------|---------|
| 维度 | 二维 | 一维 |
| 适用场景 | 整体页面布局 | 组件内部排列 |
| 对齐控制 | 行列双轴 | 主轴 + 交叉轴 |

![CSS Grid 示意图](https://picsum.photos/seed/cssgrid/800/400)
