<template>
  <div class="articles-page">
    <div class="page-header">
      <span class="eyebrow">{{ currentTopic ? currentTopic.topicName : '最新文章' }}</span>
      <h1>
        {{ currentTopic ? currentTopic.topicName + ' 专题' : '文章总览' }}
        <router-link v-if="currentTopic" to="/articles" class="clear-filter">全部文章</router-link>
      </h1>
      <p class="page-description">{{ currentTopic ? currentTopic.description || '浏览该专题下的所有技术文章。' : '浏览所有技术文章，涵盖前端、后端、工具与设计灵感。' }}</p>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <template v-else>
      <div v-if="articles.length === 0" class="empty-state">
        <p>暂无文章</p>
      </div>

      <div v-for="article in articles" :key="article.id" class="article-card" @click="goToDetail(article.id)">
        <div class="card-header">
          <span class="article-tag">{{ article.topicName }}</span>
          <span class="article-date">{{ formatDate(article.updateTime) }}</span>
        </div>
        <h2 class="article-title">{{ article.title }}</h2>
        <p class="article-summary">{{ article.summary || '暂无摘要' }}</p>
        <div class="card-footer">
          <div class="meta">
            <span class="meta-item">创建于 {{ formatDate(article.createTime) }}</span>
            <span class="meta-divider">·</span>
            <span class="meta-item">更新于 {{ formatDate(article.updateTime) }}</span>
          </div>
          <span class="read-more">阅读详情 →</span>
        </div>
      </div>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[5, 10, 15, 20, 30, 40, 50]"
          layout="total, sizes, prev, pager, next"
          @current-change="onPageChange"
          @size-change="onSizeChange"
          background
        />
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getArticles } from '@/composables/useArticle'
import { topics as allTopics } from '@/composables/useTopics'

const router = useRouter()
const route = useRoute()

const articles = ref([])
const loading = ref(true)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const currentTopic = computed(() => {
  const topicId = route.query.topicId
  if (!topicId) return null
  return allTopics.value.find(t => t.id === Number(topicId)) || null
})

async function loadArticles() {
  loading.value = true
  try {
    const topicId = route.query.topicId || null
    const result = await getArticles(pageNum.value, pageSize.value, topicId, null)
    if (result && result.records) {
      articles.value = result.records
      total.value = result.total || 0
    } else {
      articles.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('Failed to load articles', error)
    articles.value = []
  } finally {
    loading.value = false
  }
}

function goToDetail(id) {
  router.push({ name: 'articleDetail', params: { id } })
}

function onPageChange(page) {
  pageNum.value = page
  loadArticles()
}

function onSizeChange(size) {
  pageSize.value = size
  pageNum.value = 1
  loadArticles()
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

watch(() => route.query.topicId, () => {
  pageNum.value = 1
  loadArticles()
})

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.articles-page {
  width: 100%;
  padding: 2rem 1.5rem 4rem;
  box-sizing: border-box;
}

.page-header {
  margin-bottom: 2.5rem;
}

.eyebrow {
  display: inline-block;
  margin-bottom: 0.8rem;
  padding: 0.45rem 1rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.15), rgba(255, 120, 198, 0.15));
  color: #5b7cff;
  font-size: 0.95rem;
  font-weight: 700;
}

.page-header h1 {
  font-size: clamp(2rem, 4vw, 2.8rem);
  margin: 0 0 0.6rem;
  letter-spacing: -0.03em;
  color: #2a3a5f;
}

.page-description {
  color: #4a5a7f;
  font-size: 1.05rem;
  line-height: 1.7;
  margin: 0;
}

.clear-filter {
  display: inline-flex;
  align-items: center;
  vertical-align: middle;
  margin-left: 0.6rem;
  font-size: 0.75rem;
  font-weight: 600;
  color: #5b7cff;
  text-decoration: none;
  padding: 0.2rem 0.7rem;
  border-radius: 999px;
  border: 1px solid rgba(91, 124, 255, 0.25);
  transition: background 0.2s ease, border-color 0.2s ease;
}

.clear-filter:hover {
  background: rgba(91, 124, 255, 0.08);
  border-color: #5b7cff;
}

.loading-state,
.empty-state {
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

.article-card {
  padding: 1.8rem 1.2rem;
  margin-bottom: 1.5rem;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 20px 50px rgba(91, 124, 255, 0.08);
  border: 1px solid rgba(91, 124, 255, 0.12);
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.article-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 28px 60px rgba(91, 124, 255, 0.15);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.8rem;
}

.article-tag {
  display: inline-flex;
  padding: 0.4rem 0.8rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(91, 124, 255, 0.12), rgba(255, 120, 198, 0.12));
  color: #5b7cff;
  font-size: 0.85rem;
  font-weight: 700;
}

.article-date {
  color: #8a9bb8;
  font-size: 0.85rem;
}

.article-title {
  font-size: 1.35rem;
  margin: 0 0 0.6rem;
  color: #2a3a5f;
  letter-spacing: -0.02em;
}

.article-summary {
  color: #5a6a8a;
  line-height: 1.7;
  margin: 0 0 1.2rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.meta {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  flex-wrap: wrap;
}

.meta-item {
  font-size: 0.82rem;
  color: #8a9bb8;
}

.meta-divider {
  color: #c0cfe0;
}

.read-more {
  color: #5b7cff;
  font-weight: 700;
  font-size: 0.9rem;
  transition: transform 0.2s ease;
}

.article-card:hover .read-more {
  transform: translateX(4px);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 2.5rem;
}

.pagination-wrapper :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background: #5b7cff;
  border: none;
  color: #fff;
}

.pagination-wrapper :deep(.el-pagination.is-background .el-pager li:hover) {
  color: #5b7cff;
}

.pagination-wrapper :deep(.el-pagination.is-background .btn-prev:not(.is-disabled):hover),
.pagination-wrapper :deep(.el-pagination.is-background .btn-next:not(.is-disabled):hover) {
  color: #5b7cff;
}

.pagination-wrapper :deep(.el-pagination .el-select .el-input .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #5b7cff inset;
}

.pagination-wrapper :deep(.el-pagination__total),
.pagination-wrapper :deep(.el-pagination__sizes .el-input .el-input__inner) {
  color: #4a5a7f;
  font-weight: 500;
}

@media (max-width: 640px) {
  .articles-page {
    padding: 1.5rem 1rem 3rem;
  }

  .article-card {
    padding: 1.3rem;
  }

  .article-title {
    font-size: 1.15rem;
  }

  .card-footer {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
