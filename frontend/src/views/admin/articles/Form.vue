<template>
  <div>
    <h2 style="margin-top:0;">{{ isEdit ? '编辑文章' : '新增文章' }}</h2>
    <el-form :model="form" label-width="100px" class="responsive-form" v-loading="loading">
      <el-form-item label="标题" required>
        <el-input v-model="form.title" placeholder="文章标题" />
      </el-form-item>
      <el-form-item label="摘要">
        <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="文章摘要" />
      </el-form-item>
      <el-form-item label="Markdown 内容" required>
        <MdEditor
          v-model="form.contentMd"
          :mdHeadingId="headingId"
          :theme="editorTheme"
          style="width: 100%;"
        />
      </el-form-item>
      <el-form-item label="专题">
        <el-select v-model="form.topicId" placeholder="选择专题" clearable>
          <el-option v-for="t in topics" :key="t.id" :label="t.topicName" :value="t.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="form.sort" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save" :loading="saving">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
        <el-button @click="$router.push('/admin/articles')">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { MdEditor, config } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import katex from 'katex'
import { createArticle, updateArticle, getArticleDetail, getTopics } from '@/composables/useAdmin'
import { renderMarkdown } from '@/utils/markdown'
import { useTheme } from '@/composables/useTheme'

// 数学公式使用本地 katex 实例渲染，避免依赖 CDN。
config({
  editorExtensions: {
    katex: { instance: katex },
  },
})

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const { isDark } = useTheme()
// 编辑器随站点明暗主题切换；预览代码高亮会自动跟随 theme。
const editorTheme = computed(() => (isDark.value ? 'dark' : 'light'))

const topics = ref([])
const loading = ref(false)
const saving = ref(false)

const form = reactive({
  title: '',
  summary: '',
  contentMd: '',
  contentHtml: '',
  topicId: null,
  sort: 0,
})

// 标题锚点以原文作为 id（与后端一键刷新渲染规则保持一致），保证目录浮窗可跳转。
function headingId({ text }) {
  return text
}

async function loadTopics() {
  try {
    const res = await getTopics()
    if (res.code === 1) topics.value = res.data || []
  } catch (e) { /* ignore */ }
}

async function loadArticle() {
  if (!isEdit.value) return
  loading.value = true
  try {
    const res = await getArticleDetail(route.params.id)
    if (res.code === 1 && res.data) {
      const d = res.data
      form.title = d.title || ''
      form.summary = d.summary || ''
      form.contentMd = d.contentMd || ''
      form.topicId = d.topicId ?? null
      form.sort = d.sort ?? 0
    } else {
      ElMessage.error('文章不存在')
    }
  } catch (e) {
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

async function save() {
  if (!form.title) {
    ElMessage.warning('请输入文章标题')
    return
  }
  // 保存时统一用 renderMarkdown 生成 HTML：产物为干净的标准代码块（<pre><code>），
  // 与“一键刷新”产物完全一致，公开详情页无需 md-editor-v3 组件样式即可正常展示。
  form.contentHtml = renderMarkdown(form.contentMd)
  saving.value = true
  try {
    let res
    if (isEdit.value) {
      res = await updateArticle(route.params.id, form)
    } else {
      res = await createArticle(form)
    }
    if (res.code === 1) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      router.push('/admin/articles')
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadTopics()
  loadArticle()
})
</script>

<style scoped>
@media (max-width: 768px) {
  .responsive-form :deep(.el-form-item__label) {
    float: none;
    display: block;
    text-align: left;
    padding: 0 0 8px;
  }
  .responsive-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }
}
</style>
