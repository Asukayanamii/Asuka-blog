# VS Code 高效配置指南

## 必备插件

### 前端开发

| 插件名 | 用途 | 推荐指数 |
|--------|------|----------|
| ESLint | 代码规范检查 | ⭐⭐⭐⭐⭐ |
| Prettier | 代码格式化 | ⭐⭐⭐⭐⭐ |
| Vue Language Features | Vue 3 支持 | ⭐⭐⭐⭐⭐ |
| Tailwind CSS IntelliSense | Tailwind 智能提示 | ⭐⭐⭐⭐ |
| Path Intellisense | 路径补全 | ⭐⭐⭐⭐ |

### 通用工具

- **GitLens** — Git 历史可视化
- **Error Lens** — 行内错误提示
- **Code Spell Checker** — 拼写检查
- **Bracket Pair Colorizer** — 括号颜色匹配
- **Material Icon Theme** — 图标主题

## 用户配置

```json
{
  "editor.fontSize": 14,
  "editor.fontFamily": "'Cascadia Code', 'Fira Code', monospace",
  "editor.fontLigatures": true,
  "editor.minimap.enabled": false,
  "editor.formatOnSave": true,
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "always"
  },
  "editor.bracketPairColorization.enabled": true,
  "editor.guides.bracketPairs": true,
  "workbench.colorTheme": "One Dark Pro",
  "workbench.iconTheme": "material-icon-theme",
  "terminal.integrated.fontSize": 13,
  "files.autoSave": "onFocusChange"
}
```

## 快捷键

### 常用快捷键

```text
Ctrl+P          — 快速打开文件
Ctrl+Shift+P    — 命令面板
Ctrl+D          — 选中下一个相同词
Ctrl+Shift+L    — 选中所有相同词
Alt+Up/Down     — 移动行
Ctrl+Shift+K    — 删除行
Ctrl+/          — 注释/取消注释
Ctrl+`          — 打开终端
Ctrl+B          — 切换侧边栏
F12             — 跳转到定义
Alt+F12         — 查看定义（浮窗）
```

## 主题推荐

1. **One Dark Pro** — 经典暗色主题
2. **GitHub Theme** — GitHub 官方主题
3. **Catppuccin** — 柔和色彩主题
4. **Nord** — 北欧风格主题

> 工欲善其事，必先利其器。

![VS Code 界面](https://picsum.photos/seed/vscode/800/400)
