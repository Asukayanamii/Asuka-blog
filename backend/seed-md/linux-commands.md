# Linux 常用命令速查手册

## 文件操作

```bash
# 查看文件结构
tree -L 2                    # 显示 2 层目录结构
du -sh *                     # 查看当前目录下各文件/目录大小

# 查找文件
find . -name "*.log"         # 查找所有 log 文件
find . -size +100M           # 查找大于 100MB 的文件
find . -mtime -7             # 查找 7 天内修改过的文件

# 文件内容操作
grep -r "ERROR" --include="*.log" .   # 递归查找日志中的 ERROR
sed -i 's/old/new/g' file.txt       # 替换文件中的字符串
awk '{print $1, $3}' data.txt       # 打印第 1 和第 3 列
```

## 进程管理

```bash
# 查看进程
ps auxf                     # 显示进程树
ps -eo pid,ppid,%cpu,%mem,cmd --sort=-%cpu  # 按 CPU 排序
top -o %MEM                 # 按内存排序查看进程

# 后台运行
nohup java -jar app.jar > app.log 2>&1 &
screen -S myapp             # 创建会话
tmux new -s myapp           # 使用 tmux

# 终止进程
kill -9 PID                 # 强制终止
pkill -f "java -jar"        # 模糊匹配终止
```

## 网络诊断

```bash
# 端口和连接
netstat -tlnp               # 查看监听端口
ss -tlnp                    # 更现代的方式
lsof -i :8080               # 查看 8080 端口占用

# 网络请求
curl -v http://localhost:8080/api/health  # 调试请求
curl -X POST -H "Content-Type: application/json" \
  -d '{"key":"value"}' http://localhost:8080/api

# DNS 和路由
dig example.com             # DNS 查询
nslookup example.com
traceroute example.com      # 路由追踪
```

## 系统监控

```bash
# 系统资源
free -h                     # 内存使用
df -h                       # 磁盘使用
uptime                      # 系统运行时间

# 实时监控
htop                        # 增强版 top
iostat -x 1                 # 磁盘 IO 监控（每秒刷新）

# 日志查看
journalctl -u nginx -f     # 查看 nginx 服务日志（持续输出）
tail -f app.log | grep ERROR  # 实时过滤错误日志
```

## 实用技巧

| 命令 | 技巧 | 说明 |
|------|------|------|
| !! | `sudo !!` | 用 sudo 重新执行上一条命令 |
| cd | `cd -` | 回到上一个目录 |
| history | `!123` | 执行历史中第 123 条命令 |
| grep | `grep -C 5` | 显示匹配行前后各 5 行 |

> 掌握这些命令能让你的 Linux 运维效率翻倍。

![Linux 终端](https://picsum.photos/seed/linux/800/400)
