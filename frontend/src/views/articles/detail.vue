<template>
  <div class="detail-page">
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="error-state">
      <p>{{ error }}</p>
      <button class="btn btn-secondary" @click="goBack">返回文章列表</button>
    </div>

    <template v-else-if="article">
      <button class="back-btn" @click="goBack">← 返回文章列表</button>

      <article class="article-detail">
        <div class="detail-header">
          <span class="article-tag">{{ article.topicName }}</span>
          <h1 class="article-title">{{ article.title }}</h1>
          <p class="article-summary">{{ article.summary }}</p>
          <div class="detail-meta">
            <span>创建于 {{ formatDate(article.createTime) }}</span>
            <span class="meta-divider">·</span>
            <span>最后更新 {{ formatDate(article.updateTime) }}</span>
          </div>
        </div>

        <div class="article-body" v-html="article.contentHtml"></div>
      </article>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticleDetail } from '@/composables/useArticle'

const route = useRoute()
const router = useRouter()

const article = ref(null)
const loading = ref(true)
const error = ref('')

async function loadDetail() {
  loading.value = true
  error.value = ''
  try {
    const result = await getArticleDetail(route.params.id)
    if (result) {
      article.value = result
    } else {
      error.value = '文章不存在或已被删除'
    }
  } catch (e) {
    console.error('Failed to load article detail', e)
    error.value = '加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.push({ name: 'articles' })
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const h = String(d.getHours()).padStart(2, '0')
  const min = String(d.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${day} ${h}:${min}`
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.detail-page {
  width: 100%;
  padding: 2rem 1.5rem 4rem;
  box-sizing: border-box;
}

.loading-state,
.error-state {
  text-align: center;
  padding: 4rem 1rem;
  color: #4a5a7f;
}

.loading-spinner {
  width: 36px;
  height: 36px;
  margin: 0 auto 1rem;
  border: 3px solid rgba(91, 124, 255, 0.2);
  border-top-color: #5b7cff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.85);
  color: #3b4d8f;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s ease, background 0.2s ease;
  margin-bottom: 2rem;
}

.back-btn:hover {
  color: #5b7cff;
  background: rgba(255, 255, 255, 0.95);
}

.article-detail {
  background: rgba(255, 255, 255, 0.92);
  border-radius: 24px;
  padding: 2.5rem 1.5rem;
  box-shadow: 0 24px 60px rgba(91, 124, 255, 0.08);
  border: 1px solid rgba(91, 124, 255, 0.12);
}

.detail-header {
  margin-bottom: 2.5rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid rgba(91, 124, 255, 0.1);
}

.article-tag {
  display: inline-flex;
  padding: 0.4rem 0.9rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.12), rgba(255, 120, 198, 0.12));
  color: #5b7cff;
  font-size: 0.85rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.article-title {
  font-size: clamp(1.6rem, 3vw, 2.2rem);
  margin: 0 0 0.8rem;
  color: #2a3a5f;
  letter-spacing: -0.02em;
  line-height: 1.3;
}

.article-summary {
  color: #5a6a8a;
  font-size: 1.05rem;
  line-height: 1.7;
  margin: 0 0 1.2rem;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  flex-wrap: wrap;
  font-size: 0.85rem;
  color: #8a9bb8;
}

.meta-divider {
  color: #c0cfe0;
}

.article-body {
  line-height: 1.85;
  color: #2a3a5f;
  font-size: 1rem;
  overflow-wrap: break-word;
}

.article-body :deep(h1),
.article-body :deep(h2),
.article-body :deep(h3),
.article-body :deep(h4) {
  margin: 1.8em 0 0.8em;
  color: #1a2a4f;
  letter-spacing: -0.02em;
  scroll-margin-top: 80px;
}

.article-body :deep(h1) { font-size: 1.6rem; }
.article-body :deep(h2) { font-size: 1.35rem; }
.article-body :deep(h3) { font-size: 1.15rem; }

.article-body :deep(p) {
  margin: 0 0 1.2em;
}

.article-body :deep(img) {
  max-width: 100%;
  border-radius: 16px;
  margin: 1.5em 0;
}

.article-body :deep(a) {
  color: #5b7cff;
  text-decoration: underline;
}

.article-body :deep(h1),
.article-body :deep(h2),
.article-body :deep(h3),
.article-body :deep(h4) {
  scroll-margin-top: 80px;
}

.article-body :deep(code) {
  padding: 0.2em 0.4em;
  background: rgba(91, 124, 255, 0.08);
  border-radius: 6px;
  font-size: 0.9em;
  color: #5b7cff;
}

.article-body :deep(pre) {
  padding: 1.2rem;
  border-radius: 16px;
  background: rgba(42, 58, 95, 0.04);
  border: 1px solid rgba(91, 124, 255, 0.1);
  overflow-x: auto;
  margin: 1.2em 0;
}

.article-body :deep(pre code) {
  background: none;
  padding: 0;
  color: inherit;
}

.article-body :deep(blockquote) {
  margin: 1.2em 0;
  padding: 0.8rem 1.2rem;
  border-left: 3px solid #5b7cff;
  background: rgba(91, 124, 255, 0.05);
  border-radius: 0 12px 12px 0;
  color: #4a5a7f;
}

.article-body :deep(ul),
.article-body :deep(ol) {
  padding-left: 1.5em;
  margin: 0 0 1.2em;
}

.article-body :deep(li) {
  margin-bottom: 0.4em;
}

.article-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 1.2em 0;
}

.article-body :deep(th),
.article-body :deep(td) {
  padding: 0.6rem 1rem;
  border: 1px solid rgba(91, 124, 255, 0.15);
  text-align: left;
}

.article-body :deep(th) {
  background: rgba(91, 124, 255, 0.06);
  font-weight: 700;
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
  cursor: pointer;
}

.btn-secondary {
  color: #5b7cff;
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #5b7cff;
}

.btn-secondary:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 40px rgba(91, 124, 255, 0.25);
}

@media (max-width: 640px) {
  .detail-page {
    padding: 1.5rem 1rem 3rem;
  }

  .article-detail {
    padding: 1.5rem 0.8rem;
    border-radius: 16px;
  }

  .article-title {
    font-size: 1.3rem;
  }
}
</style>
