<template>
  <div class="home-content">
    <div class="hero">
      <div class="hero-copy">
        <p class="eyebrow">技术日志</p>
        <h1>在这里记录前端、后端与设计的幻想</h1>
        <p class="hero-description">
          这里是Asuka的技术小天地，兼具开发力。包含文章、工具、部署笔记与灵感合集。
        </p>
        <div class="hero-actions">
          <router-link to="/articles" class="btn btn-primary">浏览最新文章</router-link>
          <a href="#topics" class="btn btn-secondary">探索专题</a>
        </div>
      </div>

      <div class="hero-visual">
        <div class="floating-card">
          <div class="screen-header">
            <span></span><span></span><span></span>
          </div>
          <div class="screen-body">
            <div class="code-line line-1"></div>
            <div class="code-line line-2"></div>
            <div class="code-line line-3"></div>
            <div class="code-line line-4"></div>
          </div>
        </div>
        <div class="petal petal-1"></div>
        <div class="petal petal-2"></div>
        <div class="petal petal-3"></div>
        <div class="petal petal-4"></div>
      </div>
    </div>

    <section id="topics" class="features">
      <div class="section-header">
        <span>内容分类</span>
        <h2>技术专题</h2>
      </div>
      <el-scrollbar class="topic-scrollbar" wrap-style="overflow-x:auto; overflow-y:hidden;">
        <div class="topic-scroll-inner">
          <router-link v-for="topic in topics" :key="topic.id" :to="{ name: 'articles', query: { topicId: topic.id } }" class="feature-card">
            <h3>{{ topic.topicName }}</h3>
            <p>{{ topic.description }}</p>
          </router-link>
        </div>
      </el-scrollbar>
    </section>

    <section id="posts" class="posts">
      <div class="posts-heading">
        <div>
          <span>最新更新</span>
          <h2>最近发布的文章</h2>
        </div>
        <p>技术日志，带你阅读技术与创作心得。</p>
      </div>

      <el-scrollbar class="post-scrollbar" wrap-style="overflow-x:auto; overflow-y:hidden;">
        <div class="post-scroll-inner">
          <router-link v-for="post in posts" :key="post.id" :to="{ name: 'articleDetail', params: { id: post.id } }" class="post-card">
            <span class="post-tag">{{ post.topicName }}</span>
            <h3>{{ post.title }}</h3>
            <p>{{ post.summary }}</p>
            <span class="read-link">阅读详情 →</span>
          </router-link>
        </div>
      </el-scrollbar>
    </section>

    <section class="subscribe">
      <div class="subscribe-card">
        <div>
          <p>加入友链</p>
          <h2>共享你的技术笔记</h2>
        </div>
        <button type="button" @click="showQR = true">联系我</button>
      </div>
    </section>

    <el-dialog v-model="showQR" title="联系我" width="320px" align-center>
      <div class="qrcode-dialog-body">
        <div class="qrcode-placeholder">
          <div class="qrcode-grid">
            <span></span><span></span><span></span><span></span>
            <span></span><span></span><span></span><span></span>
            <span></span><span></span><span></span><span></span>
            <span></span><span></span><span></span><span></span>
          </div>
        </div>
        <p class="qrcode-dialog-text">扫码添加微信</p>
        <p class="qrcode-dialog-hint">（待填写）</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { topics as sharedTopics} from '@/data/topics'
import { loadTopics, topics as trueTopics } from '@/composables/useTopics'
import request from '@/utils/request'
import { getArticles } from '@/composables/useArticle'

onMounted(async () => {
    const tempTopics = await loadTopics()
})

const topics = trueTopics

const posts = ref([])
const showQR = ref(false)
onMounted(async () => {
  let query = {
    pageNum: 1,
    pageSize: 10,
  }
  let queryResult = await getArticles(query.pageNum, query.pageSize, null, null)
  if (!queryResult || !queryResult.records) {
    console.error('Failed to load articles')
    posts.value = []
    return
  }
  posts.value = queryResult.records
})



</script>

<style scoped>
.home-content {
  width: 100%;
}

.hero {
  display: grid;
  gap: 2rem;
  align-items: center;
  width: 100%;
  padding: 2rem 1.5rem;
  margin-bottom: 2rem;
}

.eyebrow {
  display: inline-block;
  margin-bottom: 1rem;
  padding: 0.45rem 1rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.15), rgba(255, 120, 198, 0.15));
  color: #5b7cff;
  font-size: 0.95rem;
  font-weight: 700;
}

h1 {
  font-size: clamp(2.5rem, 5vw, 4rem);
  line-height: 1.02;
  letter-spacing: -0.04em;
  margin-bottom: 1rem;
}

.hero-description {
  max-width: 640px;
  margin-bottom: 1.8rem;
  color: #4a5a7f;
  font-size: 1.05rem;
  line-height: 1.8;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 170px;
  padding: 0.95rem 1.5rem;
  border-radius: 999px;
  border: none;
  font-weight: 700;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  text-decoration: none;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 40px rgba(91, 124, 255, 0.25);
}

.btn-primary {
  color: #fff;
  background: linear-gradient(135deg, #5b7cff 0%, #ff78c6 100%);
}

.btn-secondary {
  color: #5b7cff;
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #5b7cff;
}

.hero-visual {
  position: relative;
  display: flex;
  justify-content: center;
}

.floating-card {
  position: relative;
  width: min(100%, 360px);
  padding: 1.7rem;
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.94);
  box-shadow: 0 28px 80px rgba(91, 124, 255, 0.12);
  border: 1px solid rgba(91, 124, 255, 0.2);
  backdrop-filter: blur(8px);
  overflow: hidden;
}

.screen-header {
  display: flex;
  gap: 0.6rem;
  margin-bottom: 1.5rem;
}

.screen-header span {
  width: 0.8rem;
  height: 0.8rem;
  border-radius: 999px;
  background: linear-gradient(135deg, #5b7cff, #ff78c6);
}

.screen-body {
  padding: 1rem 0;
  display: grid;
  gap: 0.95rem;
}

.code-line {
  height: 0.9rem;
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(91, 124, 255, 0.7), rgba(255, 120, 198, 0.7));
}

.line-1 { width: 90%; }
.line-2 { width: 70%; }
.line-3 { width: 80%; }
.line-4 { width: 60%; }

.petal {
  position: absolute;
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.65);
  border-radius: 50% 50% 50% 25%;
  filter: blur(0.35px);
  animation: float 8s ease-in-out infinite;
}

.petal::before,
.petal::after {
  content: "";
  position: absolute;
  width: 22px;
  height: 22px;
  background: rgba(255, 202, 230, 0.95);
  border-radius: 50%;
}

.petal::before {
  top: 10px;
  left: 8px;
}

.petal::after {
  bottom: 10px;
  right: 8px;
}

.petal-1 {
  top: -18%;
  left: 12%;
  animation-duration: 10s;
}

.petal-2 {
  top: 15%;
  right: 18%;
  animation-duration: 12s;
}

.petal-3 {
  bottom: 5%;
  left: 14%;
  animation-duration: 9s;
}

.petal-4 {
  bottom: 18%;
  right: 10%;
  animation-duration: 11s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  50% {
    transform: translate(10px, -18px) rotate(6deg);
  }
}

.features,
.posts,
.subscribe {
  width: 100%;
  padding: 2rem 1.5rem;
  box-sizing: border-box;
}

.section-header {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.section-header span,
.posts-heading span {
  display: inline-flex;
  padding: 0.45rem 0.9rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.12), rgba(255, 120, 198, 0.12));
  color: #5b7cff;
  font-weight: 700;
  font-size: 0.95rem;
}

.section-header h2,
.posts-heading h2 {
  font-size: clamp(1.8rem, 3vw, 2.4rem);
  margin: 0;
}

.topic-scroll-inner,
.post-scroll-inner {
  display: flex;
  gap: 1.4rem;
  padding: 1rem 0;
  align-items: stretch;
}

.feature-card,
.post-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 1.5rem;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 24px 60px rgba(91, 124, 255, 0.08);
  border: 1px solid rgba(91, 124, 255, 0.15);
  min-width: 260px;
  max-width: 320px;
  flex: 0 0 auto;
}

.feature-card h3,
.post-card h3 {
  margin-bottom: 0.85rem;
  color: #5b7cff;
}

.feature-card {
  text-decoration: none;
  color: inherit;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.feature-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 28px 60px rgba(91, 124, 255, 0.15);
}

.feature-card p,
.post-card p {
  color: #5a6a8a;
  line-height: 1.75;
}

.post-tag {
  display: inline-flex;
  align-items: center;
  padding: 0.45rem 0.75rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.15), rgba(255, 120, 198, 0.15));
  color: #5b7cff;
  font-size: 0.88rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.post-card {
  text-decoration: none;
  color: inherit;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.post-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 28px 60px rgba(91, 124, 255, 0.15);
}

.read-link {
  display: inline-flex;
  margin-top: auto;
  color: #3b4d8f;
  font-weight: 700;
  transition: color 0.2s ease, transform 0.2s ease;
}

.post-card:hover .read-link {
  color: #5b7cff;
  transform: translateX(4px);
}

.subscribe {
  margin-top: 0;
  padding-bottom: 3rem;
}

.subscribe-card {
  display: flex;
  flex-direction: column;
  gap: 1.3rem;
  padding: 2rem;
  border-radius: 32px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.08), rgba(255, 120, 198, 0.08));
  box-shadow: 0 30px 80px rgba(91, 124, 255, 0.1);
  border: 1px solid rgba(91, 124, 255, 0.2);
}

.subscribe-card h2 {
  margin: 0;
  font-size: clamp(1.8rem, 3vw, 2.6rem);
}

.subscribe-card button {
  align-self: flex-start;
  padding: 1rem 1.8rem;
  border-radius: 999px;
  border: none;
  font-size: 1rem;
  font-weight: 700;
  background: linear-gradient(135deg, #5b7cff 0%, #ff78c6 100%);
  color: #fff;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.subscribe-card button:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 40px rgba(91, 124, 255, 0.3);
}

@media (max-width: 640px) {
  .hero,
  .features,
  .posts,
  .subscribe {
    padding: 1.5rem 1rem;
  }

  h1 {
    font-size: clamp(1.8rem, 4vw, 2.5rem);
  }

  .hero-actions {
    flex-direction: column;
    gap: 0.8rem;
  }

  .btn {
    min-width: auto;
    width: 100%;
  }

  .topic-scroll-inner,
  .post-scroll-inner {
    gap: 1rem;
  }

  .feature-card,
  .post-card {
    min-width: 240px;
    max-width: 280px;
  }
}

@media (min-width: 641px) and (max-width: 768px) {
  .hero,
  .features,
  .posts,
  .subscribe {
    padding: 1.8rem 1.5rem;
  }

  .hero {
    gap: 1.5rem;
    margin-bottom: 2rem;
  }
}

@media (min-width: 769px) and (max-width: 1023px) {
  .hero,
  .features,
  .posts,
  .subscribe {
    padding: 2rem 2rem;
  }
}

@media (min-width: 900px) {
  .hero {
    grid-template-columns: 1.1fr 0.9fr;
    gap: 3rem;
  }
}

@media (min-width: 1024px) {
  .hero,
  .features,
  .posts,
  .subscribe {
    padding: 2.5rem 2rem;
  }
}

@media (min-width: 1200px) {
  .hero,
  .features,
  .posts,
  .subscribe {
    padding: 3rem 2rem;
  }

  .hero .hero-copy {
    max-width: 620px;
  }
}
</style>

<style>
.qrcode-dialog-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem 0;
}

.qrcode-dialog-body .qrcode-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qrcode-dialog-body .qrcode-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 4px;
  width: 140px;
  height: 140px;
  padding: 10px;
  background: #fff;
  border: 2px solid #e0e8f0;
  border-radius: 12px;
}

.qrcode-dialog-body .qrcode-grid span {
  border-radius: 2px;
}

.qrcode-dialog-body .qrcode-grid span:nth-child(odd) {
  background: #2a3a5f;
}

.qrcode-dialog-body .qrcode-grid span:nth-child(even) {
  background: #e8ecf4;
}

.qrcode-dialog-text {
  margin: 1rem 0 0.3rem;
  font-size: 1rem;
  font-weight: 600;
  color: #2a3a5f;
}

.qrcode-dialog-hint {
  margin: 0;
  font-size: 0.85rem;
  color: #b0c0d8;
}
</style>
