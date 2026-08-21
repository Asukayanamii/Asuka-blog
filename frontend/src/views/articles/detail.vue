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

      <aside
        v-if="!tocHidden && headings.length > 1"
        class="toc-float"
        :style="tocStyle"
        ref="tocEl"
      >
        <div
          class="toc-header"
          @mousedown.prevent="startDrag"
          @touchstart.prevent="startDragTouch"
        >
          <span class="toc-title">目录</span>
          <div class="toc-actions">
            <button class="toc-btn" @click="tocHidden = true" title="关闭">&times;</button>
          </div>
        </div>
        <nav class="toc-body">
          <a
            v-for="(h, i) in headings"
            :key="i"
            :href="'#' + h.id"
            class="toc-link"
            :class="['toc-depth-' + h.level, { 'toc-active': activeHeadingId === h.id }]"
            @click.prevent="scrollTo(h.id)"
          >{{ h.text }}</a>
        </nav>
      </aside>
      <button
        v-if="tocHidden && headings.length > 1"
        class="toc-reopen"
        @click="tocHidden = false"
        title="打开目录"
      >目录</button>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticleDetail } from '@/composables/useArticle'

const route = useRoute()
const router = useRouter()

const article = ref(null)
const loading = ref(true)
const error = ref('')
const tocHidden = ref(false)
const tocPos = ref({ x: 0, y: 0 })
const dragging = ref(false)
const dragStart = ref({ x: 0, y: 0 })
const dragOrigin = ref({ x: 0, y: 0 })
const tocEl = ref(null)
const activeHeadingId = ref('')
const dragRect = ref({ left: 0, top: 0, width: 0, height: 0 })

const tocStyle = computed(() => ({
  transform: `translate(${tocPos.value.x}px, ${tocPos.value.y}px)`,
}))

function startDrag(e) {
  dragging.value = true
  dragStart.value = { x: e.clientX, y: e.clientY }
  dragOrigin.value = { x: tocPos.value.x, y: tocPos.value.y }
  const panel = tocEl.value
  if (panel) {
    const r = panel.getBoundingClientRect()
    dragRect.value = { left: r.left, top: r.top, width: r.width, height: r.height }
  }
  document.addEventListener('mousemove', onDrag)
  document.addEventListener('mouseup', stopDrag)
}

function startDragTouch(e) {
  const t = e.touches[0]
  dragging.value = true
  dragStart.value = { x: t.clientX, y: t.clientY }
  dragOrigin.value = { x: tocPos.value.x, y: tocPos.value.y }
  const panel = tocEl.value
  if (panel) {
    const r = panel.getBoundingClientRect()
    dragRect.value = { left: r.left, top: r.top, width: r.width, height: r.height }
  }
  document.addEventListener('touchmove', onDragTouch, { passive: false })
  document.addEventListener('touchend', stopDrag)
}

function clamp(v, min, max) {
  return Math.min(Math.max(v, min), max)
}

function onDrag(e) {
  if (!dragging.value) return
  const margin = 4
  const r = dragRect.value
  const dstX = clamp(
    r.left + (e.clientX - dragStart.value.x),
    margin,
    window.innerWidth - r.width - margin
  )
  const dstY = clamp(
    r.top + (e.clientY - dragStart.value.y),
    margin,
    window.innerHeight - r.height - margin
  )
  tocPos.value = {
    x: dragOrigin.value.x + dstX - r.left,
    y: dragOrigin.value.y + dstY - r.top,
  }
}

function onDragTouch(e) {
  if (!dragging.value) return
  const t = e.touches[0]
  const margin = 4
  const r = dragRect.value
  const dstX = clamp(
    r.left + (t.clientX - dragStart.value.x),
    margin,
    window.innerWidth - r.width - margin
  )
  const dstY = clamp(
    r.top + (t.clientY - dragStart.value.y),
    margin,
    window.innerHeight - r.height - margin
  )
  tocPos.value = {
    x: dragOrigin.value.x + dstX - r.left,
    y: dragOrigin.value.y + dstY - r.top,
  }
}

function stopDrag() {
  dragging.value = false
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
  document.removeEventListener('touchmove', onDragTouch)
  document.removeEventListener('touchend', stopDrag)
}

const headings = computed(() => {
  if (!article.value?.contentHtml) return []
  const regex = /<h([2-3])[^>]*id="([^"]+)"[^>]*>([\s\S]*?)<\/h\1>/gi
  const results = []
  let match
  while ((match = regex.exec(article.value.contentHtml)) !== null) {
    const text = match[3].replace(/<[^>]+>/g, '').trim()
    if (text) {
      results.push({ level: parseInt(match[1]), id: match[2], text })
    }
  }
  return results
})

function scrollTo(id) {
  const el = document.getElementById(id)
  if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

function updateActiveHeading() {
  const body = document.querySelector('.article-body')
  if (!body) return
  const headingEls = body.querySelectorAll('h2, h3')
  let current = ''
  for (const el of headingEls) {
    if ((el.getBoundingClientRect().top | 0) <= 100) {
      current = el.id
    } else {
      break
    }
  }
  if (current && current !== activeHeadingId.value) {
    activeHeadingId.value = current
  }
}

let scrollTick = false
function onScrollSpy() {
  if (scrollTick) return
  scrollTick = true
  window.requestAnimationFrame(() => {
    updateActiveHeading()
    scrollTick = false
  })
}

watch(activeHeadingId, (id) => {
  if (!id) return
  const tocBody = tocEl.value?.querySelector('.toc-body')
  if (!tocBody) return
  const link = tocBody.querySelector(`[href="#${id}"]`)
  if (!link) return
  const cr = tocBody.getBoundingClientRect()
  const lr = link.getBoundingClientRect()
  if (lr.top < cr.top) {
    tocBody.scrollTop -= cr.top - lr.top
  } else if (lr.bottom > cr.bottom) {
    tocBody.scrollTop += lr.bottom - cr.bottom
  }
})

async function loadDetail() {
  loading.value = true
  error.value = ''
  try {
    const result = await getArticleDetail(route.params.id)
    if (result) {
      article.value = result
      await nextTick()
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
  window.addEventListener('scroll', onScrollSpy, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScrollSpy)
})
</script>

<style scoped>
.detail-page {
  width: 100%;
  padding: 6.75rem 1.5rem 4rem;
  box-sizing: border-box;
  background: var(--blog-page);
}

.loading-state,
.error-state {
  text-align: center;
  padding: 4rem 1rem;
  color: var(--blog-muted);
}

.loading-spinner {
  width: 36px;
  height: 36px;
  margin: 0 auto 1rem;
  border: 3px solid #dcecf8;
  border-top-color: var(--blog-blue);
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
  border-radius: 3px;
  background: #fff;
  color: var(--blog-ink);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s ease, background 0.2s ease;
  margin-bottom: 2rem;
}

.back-btn:hover {
  color: var(--blog-blue);
  background: #fff;
}

.article-detail {
  background: #fff;
  border-radius: 6px;
  padding: 2.7rem clamp(1.25rem, 5vw, 4rem);
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
  border: 0;
}

.detail-header {
  margin-bottom: 2.5rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid var(--blog-line);
}

.article-tag {
  display: inline-flex;
  padding: 0.4rem 0.9rem;
  border-radius: 3px;
  background: #e8f6ff;
  color: var(--blog-blue-deep);
  font-size: 0.85rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.article-title {
  font-family: 'Noto Serif SC', serif;
  font-size: clamp(1.7rem, 3vw, 2.45rem);
  margin: 0 0 0.8rem;
  color: #333;
  letter-spacing: 0;
  line-height: 1.3;
}

.article-summary {
  color: #666;
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
  color: var(--blog-muted);
}

.meta-divider {
  color: #c5c5c5;
}

.article-body {
  line-height: 1.85;
  color: #4c4948;
  font-size: 1.02rem;
  overflow-wrap: break-word;
}

.article-body :deep(h1),
.article-body :deep(h2),
.article-body :deep(h3),
.article-body :deep(h4) {
  margin: 1.8em 0 0.8em;
  color: #333;
  letter-spacing: 0;
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

.article-body :deep(.formula-inline) {
  display: inline-block;
  max-width: 100%;
  height: auto;
  margin: 0;
  vertical-align: middle;
}

.article-body :deep(.formula-block) {
  display: block;
  max-width: 100%;
  height: auto;
  margin: 1.25rem auto;
}

.article-body :deep(a) {
  color: var(--blog-blue-deep);
  text-decoration: none;
  border-bottom: 1px solid transparent;
  transition: border-color 0.2s ease, color 0.2s ease;
}

.article-body :deep(a:hover) {
  border-bottom-color: var(--blog-blue);
}

.article-body :deep(h1),
.article-body :deep(h2),
.article-body :deep(h3),
.article-body :deep(h4) {
  scroll-margin-top: 80px;
}

.article-body :deep(code) {
  padding: 0.2em 0.4em;
  background: #f4f4f4;
  border-radius: 3px;
  font-size: 0.9em;
  color: #e96917;
}

.article-body :deep(pre) {
  padding: 1.2rem;
  border-radius: 4px;
  background: #f6f8fa;
  border: 1px solid #edf0f2;
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
  border-left: 4px solid var(--blog-blue);
  background: #f6fbfe;
  border-radius: 0;
  color: #666;
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
  border: 1px solid var(--blog-line);
  text-align: left;
}

.article-body :deep(th) {
  background: #f6f8fa;
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
  color: var(--blog-blue);
  background: #fff;
  border: 1px solid var(--blog-blue);
}

.btn-secondary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 14px rgba(73, 177, 245, 0.28);
}

@media (max-width: 640px) {
  .detail-page {
    padding: 5.3rem 0.625rem 3rem;
  }

  .article-detail {
    padding: 1.5rem 1rem;
    border-radius: 4px;
  }

  .article-title {
    font-size: 1.3rem;
  }

  .toc-float {
    display: none;
  }
}
</style>

<style>
.toc-float {
  position: fixed;
  top: 86px;
  right: 20px;
  width: 220px;
  max-height: 60vh;
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(12px);
  border-radius: 5px;
  border: 1px solid var(--blog-line);
  box-shadow: 0 5px 18px rgba(0, 0, 0, 0.12);
  z-index: 40;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  cursor: default;
}
.toc-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 14px;
  border-bottom: 1px solid var(--blog-line);
  cursor: grab;
  user-select: none;
}
.toc-header:active {
  cursor: grabbing;
}
.toc-title {
  font-size: 0.82rem;
  font-weight: 700;
  color: #333;
  letter-spacing: 0;
}
.toc-actions {
  display: flex;
  align-items: center;
  gap: 2px;
}
.toc-btn {
  background: none;
  border: none;
  font-size: 1.15rem;
  color: var(--blog-muted);
  cursor: pointer;
  padding: 2px 4px;
  line-height: 1;
  border-radius: 4px;
  transition: color 0.2s ease, background 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}
.toc-btn:hover {
  color: #333;
  background: #f2f7fa;
}
.toc-body {
  flex: 1;
  overflow-y: auto;
  padding: 6px 0;
  scrollbar-width: thin;
  scrollbar-color: #d6e5ed transparent;
}
.toc-body::-webkit-scrollbar {
  width: 4px;
}
.toc-body::-webkit-scrollbar-thumb {
  background: #d6e5ed;
  border-radius: 4px;
}
.toc-link {
  display: block;
  padding: 5px 14px 5px 18px;
  font-size: 0.82rem;
  color: #666;
  text-decoration: none;
  line-height: 1.5;
  transition: color 0.2s ease, background 0.2s ease;
  border-left: 2px solid transparent;
}
.toc-link:hover {
  color: var(--blog-blue);
  background: #f2f9fd;
  border-left-color: var(--blog-blue);
}
.toc-depth-3 {
  padding-left: 32px;
  font-size: 0.78rem;
}
.toc-link.toc-active {
  color: var(--blog-blue-deep);
  border-left-color: var(--blog-blue);
  background: #eaf7fe;
  font-weight: 700;
}
.toc-reopen {
  position: fixed;
  top: 86px;
  right: 0;
  border: none;
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(8px);
  color: var(--blog-blue);
  font-size: 0.78rem;
  font-weight: 700;
  padding: 10px 6px;
  border-radius: 5px 0 0 5px;
  border: 1px solid var(--blog-line);
  border-right: none;
  cursor: pointer;
  z-index: 40;
  writing-mode: vertical-lr;
  letter-spacing: 0.1em;
  transition: color 0.2s ease, background 0.2s ease;
  box-shadow: -3px 0 10px rgba(0, 0, 0, 0.08);
}
.toc-reopen:hover {
  color: #fff;
  background: var(--blog-blue);
}
</style>
