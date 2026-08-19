<template>
  <div class="home-content">
    <header class="hero" id="top">
      <div class="hero-shade"></div>
      <div class="hero-copy">
        <p class="eyebrow">技术笔记</p>
        <h1>在这里记录前端、后端与设计的幻想</h1>
        <p class="hero-description">这里是技术天地，兼具开发力。包含文章、工具、部署笔记与灵感合集。</p>
        <div class="hero-actions">
          <router-link to="/articles" class="hero-button">浏览最新文章</router-link>
          <a href="#topics" class="hero-button hero-button-quiet">探索专题</a>
        </div>
      </div>
      <a href="#posts" class="scroll-down" aria-label="向下滚动"><span></span></a>
    </header>

    <div class="home-layout">
      <section id="posts" class="recent-posts" aria-label="最近发布的文章">
        <div class="posts-heading">
          <span>最新更新</span>
          <h2>最近发布的文章</h2>
          <p>技术日志，带你阅读技术与创作心得。</p>
        </div>
        <div v-if="loading" class="loading-state"><span></span><p>加载中...</p></div>
        <template v-else>
          <router-link
            v-for="(post, index) in posts"
            :key="post.id"
            :to="{ name: 'articleDetail', params: { id: post.id } }"
            class="recent-post-item"
          >
            <div class="post-cover" :class="{ right: index % 2 === 1 }">
              <img src="/hero-banner.jpeg" :alt="post.title" />
            </div>
            <div class="recent-post-info">
              <h2>{{ post.title }}</h2>
              <div class="article-meta">
                <span>创建于 {{ formatDate(post.createTime) }}</span>
                <span class="meta-divider">|</span>
                <span>{{ post.topicName }}</span>
              </div>
              <p>{{ post.summary }}</p>
              <span class="read-link">阅读详情 →</span>
            </div>
          </router-link>
          <div v-if="posts.length === 0" class="empty-state"><p>暂无文章</p></div>
        </template>
      </section>

      <aside class="home-aside">
        <section id="topics" class="aside-card topic-card">
          <p class="aside-kicker">内容分类</p>
          <h2>技术专题</h2>
          <router-link
            v-for="topic in topics"
            :key="topic.id"
            :to="{ name: 'articles', query: { topicId: topic.id } }"
            class="topic-link"
          >
            <span>{{ topic.topicName }}</span>
            <small>{{ topic.description }}</small>
          </router-link>
          <p v-if="topics.length === 0" class="aside-empty">暂无栏目</p>
        </section>

        <section class="aside-card subscribe-card">
          <p>加入友链</p>
          <h2>共享你的技术笔记</h2>
          <button type="button" @click="showQR = true">联系我</button>
        </section>
      </aside>
    </div>

    <el-dialog v-model="showQR" title="联系我" width="320px" align-center>
      <div class="qrcode-dialog-body">
        <img src="/img/wx.png" alt="微信" class="wx-qrcode-dialog" />
        <p class="qrcode-dialog-text">扫码添加微信</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { topics } from '@/composables/useTopics'
import { getArticles } from '@/composables/useArticle'

const posts = ref([])
const loading = ref(true)
const showQR = ref(false)

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

onMounted(async () => {
  const result = await getArticles(1, 10, null, null)
  posts.value = result?.records || []
  loading.value = false
})
</script>

<style scoped>
.hero {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: min(100vh, 760px);
  padding: 8rem 1.5rem 6rem;
  overflow: hidden;
  color: #fff;
  text-align: center;
  background: url('/hero-banner.jpeg') center / cover no-repeat;
}

.hero-shade {
  position: absolute;
  inset: 0;
  background: rgba(20, 30, 39, 0.48);
}

.hero-copy {
  position: relative;
  z-index: 1;
  max-width: 790px;
}

.eyebrow {
  margin: 0 0 1rem;
  font-size: 0.96rem;
  font-weight: 500;
}

.hero h1 {
  margin: 0;
  font-family: 'Noto Serif SC', serif;
  font-size: clamp(2.15rem, 5vw, 4.3rem);
  font-weight: 700;
  line-height: 1.25;
  letter-spacing: 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.hero-description {
  max-width: 610px;
  margin: 1.5rem auto 2rem;
  font-size: 1rem;
  line-height: 1.9;
}

.hero-actions {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.hero-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 138px;
  padding: 0.7rem 1.25rem;
  color: #fff;
  background: var(--blog-blue);
  border: 1px solid var(--blog-blue);
  border-radius: 3px;
  font-size: 0.92rem;
  text-decoration: none;
  transition: background 0.2s ease, transform 0.2s ease;
}

.hero-button:hover {
  background: var(--blog-blue-deep);
  transform: translateY(-2px);
}

.hero-button-quiet {
  background: transparent;
  border-color: rgba(255, 255, 255, 0.7);
}

.hero-button-quiet:hover {
  background: rgba(255, 255, 255, 0.14);
}

.scroll-down {
  position: absolute;
  bottom: 1.5rem;
  left: 50%;
  z-index: 1;
  width: 26px;
  height: 38px;
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: 14px;
  transform: translateX(-50%);
}

.scroll-down span {
  display: block;
  width: 4px;
  height: 8px;
  margin: 7px auto 0;
  background: #fff;
  border-radius: 4px;
  animation: scroll-mark 1.8s ease-in-out infinite;
}

@keyframes scroll-mark {
  0%, 100% { opacity: 1; transform: translateY(0); }
  55% { opacity: 0.25; transform: translateY(13px); }
}

.home-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 290px;
  gap: 1.5rem;
  width: min(1160px, calc(100% - 2rem));
  margin: 2rem auto 4rem;
}

.recent-posts {
  display: grid;
  gap: 1.5rem;
}

.posts-heading {
  padding: 0.25rem 0 0.35rem;
}

.posts-heading span,
.aside-kicker {
  display: block;
  margin: 0 0 0.35rem;
  color: var(--blog-blue);
  font-size: 0.78rem;
}

.posts-heading h2 {
  margin: 0;
  color: #333;
  font-size: 1.38rem;
}

.posts-heading p {
  margin: 0.45rem 0 0;
  color: var(--blog-muted);
  font-size: 0.86rem;
}

.recent-post-item {
  display: grid;
  grid-template-columns: minmax(180px, 44%) minmax(0, 1fr);
  min-height: 236px;
  overflow: hidden;
  color: var(--blog-ink);
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
  text-decoration: none;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.recent-post-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.14);
}

.post-cover {
  grid-column: 1;
  overflow: hidden;
}

.post-cover.right {
  grid-column: 2;
  grid-row: 1;
}

.post-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.recent-post-item:hover .post-cover img {
  transform: scale(1.08);
}

.recent-post-info {
  display: flex;
  grid-column: 2;
  grid-row: 1;
  flex-direction: column;
  justify-content: center;
  padding: 1.8rem 1.9rem;
}

.post-cover.right + .recent-post-info {
  grid-column: 1;
  text-align: right;
}

.recent-post-info h2 {
  margin: 0;
  color: #333;
  font-size: clamp(1.1rem, 2vw, 1.45rem);
  line-height: 1.45;
}

.article-meta {
  margin: 0.6rem 0 0.8rem;
  color: var(--blog-muted);
  font-size: 0.78rem;
}

.meta-divider {
  padding: 0 0.4rem;
}

.recent-post-info p {
  display: -webkit-box;
  margin: 0;
  overflow: hidden;
  color: #666;
  font-size: 0.88rem;
  line-height: 1.75;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

.read-link {
  margin-top: 1rem;
  color: var(--blog-blue);
  font-size: 0.86rem;
  font-weight: 600;
}

.home-aside {
  display: grid;
  align-content: start;
  gap: 1.5rem;
}

.aside-card {
  padding: 1.35rem;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
}

.aside-card h2 {
  margin: 0 0 0.85rem;
  color: #333;
  font-size: 1rem;
}

.aside-kicker {
  margin-bottom: 0.25rem;
}

.topic-link {
  display: block;
  padding: 0.8rem 0;
  color: var(--blog-ink);
  border-top: 1px solid var(--blog-line);
  text-decoration: none;
}

.topic-link:hover span {
  color: var(--blog-blue);
}

.topic-link span,
.topic-link small {
  display: block;
}

.topic-link span {
  font-size: 0.9rem;
  font-weight: 600;
}

.topic-link small {
  display: -webkit-box;
  margin-top: 0.3rem;
  overflow: hidden;
  color: var(--blog-muted);
  font-size: 0.76rem;
  line-height: 1.55;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.aside-empty,
.empty-state {
  margin: 0;
  padding: 2rem 1rem;
  color: var(--blog-muted);
  text-align: center;
}

.subscribe-card {
  color: #fff;
  background: var(--blog-blue);
}

.subscribe-card p {
  margin: 0 0 0.55rem;
  font-size: 0.82rem;
}

.subscribe-card h2 {
  color: inherit;
  line-height: 1.5;
}

.subscribe-card button {
  padding: 0.55rem 0.95rem;
  color: var(--blog-blue-deep);
  background: #fff;
  border: 0;
  border-radius: 3px;
  font-size: 0.86rem;
}

.loading-state {
  padding: 4rem 1rem;
  color: var(--blog-muted);
  text-align: center;
}

.loading-state span {
  display: block;
  width: 28px;
  height: 28px;
  margin: 0 auto 0.75rem;
  border: 3px solid #dcecf8;
  border-top-color: var(--blog-blue);
  border-radius: 50%;
  animation: loading-spin 0.8s linear infinite;
}

@keyframes loading-spin { to { transform: rotate(360deg); } }

@media (max-width: 900px) {
  .home-layout {
    grid-template-columns: 1fr;
  }

  .home-aside {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 620px) {
  .hero { min-height: 620px; }

  .home-layout {
    width: min(100% - 1.25rem, 560px);
    margin-top: 1rem;
  }

  .recent-post-item,
  .recent-post-item:has(.post-cover.right) {
    grid-template-columns: 1fr;
  }

  .post-cover,
  .post-cover.right {
    grid-column: 1;
    grid-row: 1;
    height: 190px;
  }

  .recent-post-info,
  .post-cover.right + .recent-post-info {
    grid-column: 1;
    grid-row: 2;
    padding: 1.3rem;
    text-align: left;
  }

  .home-aside { grid-template-columns: 1fr; }
}
</style>

<style>
.qrcode-dialog-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem 0;
}

.wx-qrcode-dialog {
  width: 180px;
  border: 1px solid var(--blog-line);
}

.qrcode-dialog-text {
  margin: 1rem 0 0.3rem;
  color: var(--blog-ink);
  font-size: 0.95rem;
}
</style>
