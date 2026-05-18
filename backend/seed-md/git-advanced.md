# Git 高级操作技巧大全

## 交互式 Rebase

### 修改历史提交

```bash
# 修改最近 3 个提交
git rebase -i HEAD~3
```

在交互界面中，你可以：

- `pick` — 保留该提交
- `reword` — 修改提交信息
- `squash` — 合并到前一个提交
- `fixup` — 合并但不保留提交信息
- `drop` — 删除该提交

### 合并多个提交

```bash
# 将最近的 3 个提交合并为 1 个
git rebase -i HEAD~3

# 将除第一个外的所有提交标记为 squash
# pick abc123 第一个提交
# squash def456 第二个提交
# squash ghi789 第三个提交
```

## Git Bisect

二分查找引入 Bug 的提交：

```bash
# 开始二分查找
git bisect start

# 标记当前版本为 bad
git bisect bad

# 标记一个已知正常的版本
git bisect good v1.0

# Git 会二分切换到中间的提交
# 测试后标记
git bisect good  # 如果这个版本没问题
git bisect bad   # 如果这个版本有问题

# 重复直到找到引入 Bug 的提交
git bisect reset
```

## Stash 进阶用法

```bash
# 临时保存工作区
git stash push -m "WIP: 正在开发登录功能"

# 查看所有 stash
git stash list

# 应用指定 stash
git stash apply stash@{0}

# 应用并删除
git stash pop stash@{0}

# 基于 stash 创建新分支
git stash branch feature/new-feature stash@{0}
```

## 子模块管理

```bash
# 添加子模块
git submodule add https://github.com/example/lib.git libs/example

# 初始化子模块
git submodule init
git submodule update
```

## 常用配置

```bash
# 别名配置
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.st status
git config --global alias.lg "log --oneline --graph --all"

# 自动纠正
git config --global help.autocorrect 1
```

## 工作流对比

| 工作流 | 适用场景 | 复杂度 |
|--------|----------|--------|
| Git Flow | 大型项目，有固定发版周期 | ⭐⭐⭐⭐⭐ |
| GitHub Flow | 持续部署，快速迭代 | ⭐⭐ |
| GitLab Flow | 环境分支管理 | ⭐⭐⭐ |

> Git 是每个开发者的必修课，掌握高级技巧能大幅提升工作效率。

![Git 工作流示意图](https://picsum.photos/seed/git/800/400)
