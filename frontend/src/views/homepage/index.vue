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
        <section class="aside-card profile-card" aria-label="个人信息">
          <div class="profile-heading">
            <a
              :href="contactGithub"
              target="_blank"
              rel="noopener noreferrer"
              class="profile-avatar-link"
              title="访问 GitHub 主页"
            >
              <img src="/avatar.jpg" alt="Asuka 的头像" class="profile-avatar" />
            </a>
            <h2>Asuka</h2>
          </div>
          <p class="profile-bio">一名热爱技术的开发者，记录技术学习、项目实践与日常思考。</p>
          <a
            :href="contactGithub"
            target="_blank"
            rel="noopener noreferrer"
            class="github-profile-link"
          >
            <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z" /></svg>
            <span>github.com/Asukayanamii</span>
            <span class="external-mark" aria-hidden="true">↗</span>
          </a>
          <div class="profile-stats" aria-label="博客统计">
            <router-link to="/articles" class="profile-stat" title="查看全部文章">
              <strong>{{ articleTotal }}</strong>
              <span>文章</span>
            </router-link>
            <a href="#topics" class="profile-stat" title="查看全部分类">
              <strong>{{ topics.length }}</strong>
              <span>分类</span>
            </a>
          </div>
        </section>

        <section id="topics" class="aside-card topic-card">
          <div class="topic-heading">
            <div>
              <p class="aside-kicker">内容分类</p>
              <h2>技术专题</h2>
            </div>
            <router-link to="/articles" class="topic-total" title="查看全部文章">{{ articleTotal }} 篇</router-link>
          </div>
          <router-link
            v-for="topic in topics"
            :key="topic.id"
            :to="{ name: 'articles', query: { topicId: topic.id } }"
            class="topic-link"
          >
            <span class="topic-name">{{ topic.topicName }}</span>
            <b class="topic-count">{{ topicCounts[topic.id] ?? 0 }}</b>
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
import { loadTopics, topics } from '@/composables/useTopics'
import { getArticles } from '@/composables/useArticle'

const posts = ref([])
const loading = ref(true)
const showQR = ref(false)
const articleTotal = ref(0)
const topicCounts = ref({})
const contactGithub = import.meta.env.VITE_CONTACT_GITHUB || 'https://github.com/Asukayanamii'

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

onMounted(async () => {
  const [result] = await Promise.all([
    getArticles(1, 10, null, null),
    loadTopics(),
  ])

  posts.value = result?.records || []
  articleTotal.value = result?.total || 0

  const countResults = await Promise.all(
    topics.value.map(async (topic) => {
      const topicResult = await getArticles(1, 1, topic.id, null)
      return [topic.id, topicResult?.total || 0]
    }),
  )
  topicCounts.value = Object.fromEntries(countResults)
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

.profile-card {
  padding: 1.55rem 1.35rem 1.2rem;
  overflow: hidden;
  text-align: center;
}

.profile-heading {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 0.7rem;
}

.profile-heading h2 {
  margin: 0;
  color: var(--blog-ink);
  font-family: 'Noto Sans SC', 'Microsoft YaHei', sans-serif;
  font-size: 1.08rem;
  font-weight: 600;
  letter-spacing: 0;
}

.profile-avatar-link {
  display: block;
  flex: 0 0 auto;
  width: 82px;
  height: 82px;
  border-radius: 50%;
}

.profile-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border: 3px solid #fff;
  border-radius: inherit;
  box-shadow: 0 4px 14px rgba(40, 105, 143, 0.2);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.profile-avatar-link:hover .profile-avatar {
  transform: translateY(-3px) scale(1.04);
  box-shadow: 0 7px 18px rgba(40, 105, 143, 0.25);
}

.profile-bio {
  max-width: 220px;
  margin: 0.8rem auto 1.05rem;
  color: var(--blog-muted);
  font-size: 0.8rem;
  line-height: 1.75;
}

.github-profile-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  justify-content: center;
  min-height: 34px;
  padding: 0.35rem 0.25rem;
  color: var(--blog-muted);
  background: transparent;
  border: 0;
  border-radius: 0;
  font-size: 0.76rem;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s ease;
}

.github-profile-link:hover {
  color: var(--blog-blue);
}

.github-profile-link svg {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

.external-mark {
  font-size: 0.9rem;
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  margin: 0.65rem -1.35rem 0;
  border-top: 1px solid var(--blog-line);
}

.profile-stat {
  display: grid;
  gap: 0.12rem;
  justify-items: center;
  padding: 0.85rem 0 0.05rem;
  color: var(--blog-muted);
  font-size: 0.74rem;
  text-decoration: none;
}

.profile-stat + .profile-stat {
  border-left: 1px solid var(--blog-line);
}

.profile-stat strong {
  color: var(--blog-ink);
  font-size: 1.15rem;
  line-height: 1.25;
  transition: color 0.2s ease, transform 0.2s ease;
}

.profile-stat:hover strong {
  color: var(--blog-blue);
  transform: translateY(-2px);
}

.topic-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0.75rem;
}

.topic-heading h2 {
  margin-bottom: 0.85rem;
}

.topic-total {
  padding-top: 0.15rem;
  color: var(--blog-muted);
  font-size: 0.75rem;
  text-decoration: none;
}

.topic-total:hover {
  color: var(--blog-blue);
}

.topic-link {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  column-gap: 0.5rem;
  padding: 0.8rem 0;
  color: var(--blog-ink);
  border-top: 1px solid var(--blog-line);
  text-decoration: none;
}

.topic-link:hover .topic-name,
.topic-link:hover .topic-count {
  color: var(--blog-blue);
}

.topic-link .topic-name,
.topic-link small {
  display: block;
}

.topic-name {
  font-size: 0.9rem;
  font-weight: 600;
}

.topic-count {
  align-self: start;
  color: var(--blog-muted);
  font-size: 0.76rem;
  font-weight: 500;
  line-height: 1.7;
  transition: color 0.2s ease;
}

.topic-link small {
  grid-column: 1 / -1;
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
