const fs = require('fs');
const path = require('path');
const http = require('http');

const SEED_DIR = path.resolve(__dirname, '../seed-md');

const articles = [
  // topic_id=1: 前端魔法
  { file: 'vue3-composition-api.md', title: 'Vue 3 组合式 API 深入实践', summary: '从 options API 到 composition API 的迁移实战，深入理解 ref、reactive、computed 等核心组合式 API 的用法与最佳实践。', topicId: 1, sort: 0 },
  { file: 'typescript-generics.md', title: 'TypeScript 泛型编程从入门到精通', summary: '全面解析 TypeScript 泛型的各种用法，包括泛型约束、条件类型、映射类型等高级特性。', topicId: 1, sort: 1 },
  { file: 'react18-features.md', title: 'React 18 新特性全面解析', summary: '探索 React 18 带来的并发渲染、自动批处理、Suspense 改进等重磅新特性。', topicId: 1, sort: 2 },
  { file: 'frontend-performance.md', title: '前端性能优化 checklist', summary: '一份全面的前端性能优化清单，涵盖加载优化、渲染优化、资源优化等各个方面。', topicId: 1, sort: 3 },
  // topic_id=2: 后端秘笈
  { file: 'springboot-mybatisplus.md', title: 'Spring Boot 3 整合 MyBatis-Plus 实战教程', summary: '手把手教你将 MyBatis-Plus 集成到 Spring Boot 3 项目中，实现高效的数据访问层开发。', topicId: 2, sort: 0 },
  { file: 'redis-cache.md', title: 'Redis 缓存策略与实战', summary: '深入分析 Redis 的缓存淘汰策略、穿透/击穿/雪崩解决方案以及在 Spring Boot 中的实战应用。', topicId: 2, sort: 1 },
  { file: 'mysql-index.md', title: 'MySQL 索引优化与查询性能调优', summary: '从执行计划分析到索引设计，系统性地讲解 MySQL 查询性能优化的方法与实战案例。', topicId: 2, sort: 2 },
  { file: 'jvm-memory.md', title: 'JVM 内存模型与垃圾回收机制详解', summary: '深入解析 JVM 的内存区域划分、垃圾回收算法以及常见的 GC 调优参数。', topicId: 2, sort: 3 },
  { file: 'spring-security-jwt.md', title: 'Spring Security + JWT 认证实战', summary: '在 Spring Boot 3 中集成 Spring Security 和 JWT 实现无状态认证。', topicId: 2, sort: 4 },
  // topic_id=3: 前端开发
  { file: 'css-grid.md', title: 'CSS Grid 布局完全指南', summary: '从基础概念到高级技巧，全面掌握 CSS Grid 布局的各种用法。', topicId: 3, sort: 0 },
  { file: 'js-async.md', title: 'JavaScript 异步编程进化史', summary: '从回调地狱到 Promise、async/await，回顾 JavaScript 异步编程的发展历程。', topicId: 3, sort: 1 },
  { file: 'vite-optimize.md', title: 'Vite 构建优化实战', summary: '从零开始优化 Vite 构建配置，包括代码分割、缓存策略、打包体积优化等。', topicId: 3, sort: 2 },
  // topic_id=4: 日常记录
  { file: 'dev-summary-april.md', title: '2026 年 4 月学习总结', summary: '总结本月在技术学习方面的收获与反思，包括新接触的技术栈和项目实践心得。', topicId: 4, sort: 0 },
  { file: 'production-debug.md', title: '一次 Spring Boot 生产环境问题排查记录', summary: '记录一次线上服务响应缓慢问题的排查过程，涉及线程池、数据库连接池和 GC 等多方面分析。', topicId: 4, sort: 1 },
  { file: 'tech-writing.md', title: '关于技术写作的一些思考', summary: '分享我在技术写作方面的经验和心得，包括如何选题、如何组织内容、如何持续输出。', topicId: 4, sort: 2 },
  { file: 'health-guide.md', title: '程序员健康指南：久坐族的自救手册', summary: '分享程序员常见的健康问题以及对应的改善方法，包括正确的坐姿、眼部保健和运动建议。', topicId: 4, sort: 3 },
  { file: 'build-blog.md', title: '从零搭建个人博客：技术选型与架构设计', summary: '分享从零开始搭建个人博客的技术选型思考、前后端架构设计和部署方案。', topicId: 4, sort: 4 },
  // topic_id=5: 工具配置
  { file: 'git-advanced.md', title: 'Git 高级操作技巧大全', summary: '全面整理 Git 的进阶用法，包括交互式 rebase、bisect 二分查找、submodule 管理等实用技巧。', topicId: 5, sort: 0 },
  { file: 'docker-deploy.md', title: 'Docker 容器化部署从入门到精通', summary: '详解 Docker 的核心概念、Dockerfile 编写、docker-compose 编排以及生产环境部署最佳实践。', topicId: 5, sort: 1 },
  { file: 'vscode-config.md', title: 'VS Code 高效配置指南', summary: '分享我的 VS Code 配置、常用插件和快捷键，打造高效的前端开发环境。', topicId: 5, sort: 2 },
  { file: 'linux-commands.md', title: 'Linux 常用命令速查手册', summary: '整理日常开发中常用的 Linux 命令，涵盖文件操作、进程管理、网络诊断、系统监控等方面。', topicId: 5, sort: 3 },
  { file: 'nginx-config.md', title: 'Nginx 配置从入门到精通', summary: '从基础反向代理到高级负载均衡策略，全面学习 Nginx 的配置与优化。', topicId: 5, sort: 4 },
];

async function uploadArticle(article) {
  const filePath = path.join(SEED_DIR, article.file);
  const contentMd = fs.readFileSync(filePath, 'utf-8');

  return new Promise((resolve, reject) => {
    const boundary = '----FormBoundary' + Math.random().toString(36).slice(2);
    const bodyParts = [];

    function addField(name, value) {
      bodyParts.push(
        `--${boundary}\r\n` +
        `Content-Disposition: form-data; name="${name}"\r\n\r\n` +
        `${value}\r\n`
      );
    }

    addField('title', article.title);
    addField('summary', article.summary);
    addField('topicId', article.topicId);
    addField('sort', article.sort);

    bodyParts.push(
      `--${boundary}\r\n` +
      `Content-Disposition: form-data; name="file"; filename="${article.file}"\r\n` +
      `Content-Type: text/markdown\r\n\r\n` +
      contentMd + '\r\n'
    );

    bodyParts.push(`--${boundary}--\r\n`);
    const body = Buffer.from(bodyParts.join(''), 'utf-8');

    const options = {
      hostname: 'localhost',
      port: 8080,
      path: '/user/articles/upload',
      method: 'POST',
      headers: {
        'Content-Type': `multipart/form-data; boundary=${boundary}`,
        'Content-Length': Buffer.byteLength(body, 'utf-8'),
      },
    };

    const req = http.request(options, (res) => {
      let data = '';
      res.on('data', (chunk) => (data += chunk));
      res.on('end', () => {
        try {
          const result = JSON.parse(data);
          if (result.code === 1) {
            console.log(`  ✅ ID=${result.data} ${article.title}`);
          } else {
            console.log(`  ❌ ${article.title}: ${result.msg}`);
          }
        } catch (e) {
          console.log(`  ❌ ${article.title}: parse error`);
        }
        resolve();
      });
    });

    req.on('error', (err) => {
      console.log(`  ❌ ${article.title}: ${err.message}`);
      resolve();
    });

    req.write(body);
    req.end();
  });
}

async function main() {
  console.log(`开始上传 ${articles.length} 篇文章...\n`);

  for (const article of articles) {
    process.stdout.write(`上传: ${article.title} ... `);
    await uploadArticle(article);
  }

  console.log('\n全部完成！');
}

main().catch(console.error);