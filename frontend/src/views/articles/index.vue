<template>
  <div class="articles-page">
    <header class="page-banner">
      <div>
        <p>{{ currentTopic ? currentTopic.topicName : '最新文章' }}</p>
        <h1>{{ currentTopic ? currentTopic.topicName + ' 专题' : '文章总览' }}</h1>
        <p class="page-description">{{ currentTopic ? currentTopic.description || '浏览该专题下的所有技术文章。' : '浏览所有技术文章，涵盖前端、后端、工具与设计灵感。' }}</p>
        <router-link v-if="currentTopic" to="/articles" class="clear-filter">全部文章</router-link>
      </div>
    </header>

    <section class="archive-shell">
      <div v-if="loading" class="loading-state"><span></span><p>加载中...</p></div>

      <template v-else>
        <div v-if="articles.length === 0" class="empty-state"><p>暂无文章</p></div>

        <router-link
          v-for="article in articles"
          :key="article.id"
          :to="{ name: 'articleDetail', params: { id: article.id } }"
          class="archive-item"
        >
          <time>{{ formatDate(article.updateTime) }}</time>
          <div class="archive-dot"></div>
          <div class="archive-content">
            <span class="article-tag">{{ article.topicName }}</span>
            <h2>{{ article.title }}</h2>
            <p>{{ article.summary || '暂无摘要' }}</p>
            <div class="article-meta">
              <span>创建于 {{ formatDate(article.createTime) }}</span>
              <span>更新于 {{ formatDate(article.updateTime) }}</span>
            </div>
          </div>
        </router-link>

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
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getArticles } from '@/composables/useArticle'
import { topics as allTopics } from '@/composables/useTopics'

const route = useRoute()
const articles = ref([])
const loading = ref(true)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const currentTopic = computed(() => {
  const topicId = route.query.topicId
  return topicId ? allTopics.value.find((topic) => topic.id === Number(topicId)) || null : null
})

async function loadArticles() {
  loading.value = true
  const result = await getArticles(pageNum.value, pageSize.value, route.query.topicId || null, null)
  articles.value = result?.records || []
  total.value = result?.total || 0
  loading.value = false
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
  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}`
}

watch(() => route.query.topicId, () => {
  pageNum.value = 1
  loadArticles()
})

onMounted(loadArticles)
</script>

<style scoped>
.page-banner {
  position: relative;
  display: grid;
  min-height: 310px;
  padding: 7rem 1.5rem 3rem;
  color: #fff;
  text-align: center;
  place-items: center;
  background: linear-gradient(rgba(20, 30, 39, 0.42), rgba(20, 30, 39, 0.55)), url('/hero-banner.jpeg') center / cover;
}

.page-banner > div { max-width: 780px; }
.page-banner p { margin: 0 0 0.7rem; font-size: 0.94rem; }
.page-banner h1 { margin: 0; font-family: 'Noto Serif SC', serif; font-size: clamp(2rem, 4vw, 3.25rem); line-height: 1.3; }
.page-banner .page-description { margin-top: 1rem; line-height: 1.8; }

.clear-filter {
  display: inline-block;
  margin-top: 0.4rem;
  padding: 0.4rem 0.85rem;
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.75);
  border-radius: 3px;
  font-size: 0.85rem;
  text-decoration: none;
}

.archive-shell {
  width: min(900px, calc(100% - 2rem));
  margin: 2.8rem auto 4rem;
}

.archive-item {
  position: relative;
  display: grid;
  grid-template-columns: 144px 28px minmax(0, 1fr);
  padding-bottom: 1.7rem;
  color: var(--blog-ink);
  text-decoration: none;
}

.archive-item::before {
  position: absolute;
  top: 24px;
  bottom: 0;
  left: 157px;
  width: 2px;
  background: #dce5eb;
  content: '';
}

.archive-item:last-of-type::before { display: none; }
.archive-item time { padding-top: 0.36rem; color: var(--blog-muted); font-size: 0.85rem; text-align: right; }

.archive-dot {
  position: relative;
  z-index: 1;
  width: 12px;
  height: 12px;
  margin: 0.55rem auto 0;
  background: var(--blog-blue);
  border: 3px solid #eaf6fe;
  border-radius: 50%;
  transition: transform 0.2s ease, background 0.2s ease;
}

.archive-content {
  padding: 1.35rem 1.5rem;
  background: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.07);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.archive-item:hover .archive-content { transform: translateX(5px); box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12); }
.archive-item:hover .archive-dot { background: var(--blog-blue-deep); transform: scale(1.18); }
.article-tag { color: var(--blog-blue); font-size: 0.78rem; }
.archive-content h2 { margin: 0.35rem 0 0.7rem; color: #333; font-size: 1.2rem; line-height: 1.45; }
.archive-content p { display: -webkit-box; margin: 0; overflow: hidden; color: #666; font-size: 0.88rem; line-height: 1.75; -webkit-box-orient: vertical; -webkit-line-clamp: 2; }
.article-meta { display: flex; gap: 0.85rem; margin-top: 0.75rem; color: var(--blog-muted); font-size: 0.76rem; }

.loading-state, .empty-state { padding: 5rem 1rem; color: var(--blog-muted); text-align: center; }
.loading-state span { display: block; width: 28px; height: 28px; margin: 0 auto 0.75rem; border: 3px solid #dcecf8; border-top-color: var(--blog-blue); border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.pagination-wrapper { display: flex; justify-content: center; margin-top: 1rem; }
.pagination-wrapper :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) { background: var(--blog-blue); }
.pagination-wrapper :deep(.el-pagination.is-background .el-pager li:hover), .pagination-wrapper :deep(.el-pagination button:hover) { color: var(--blog-blue); }

@media (max-width: 600px) {
  .page-banner { min-height: 270px; padding-top: 6rem; }
  .archive-shell { width: min(100% - 1.25rem, 560px); margin-top: 1.7rem; }
  .archive-item { grid-template-columns: 22px minmax(0, 1fr); }
  .archive-item::before { left: 10px; }
  .archive-item time { grid-column: 2; grid-row: 1; padding: 0 0 0.45rem; text-align: left; }
  .archive-dot { grid-column: 1; grid-row: 1 / span 2; margin-top: 0.28rem; }
  .archive-content { grid-column: 2; grid-row: 2; padding: 1.1rem; }
  .article-meta { flex-direction: column; gap: 0.25rem; }
}
</style>
