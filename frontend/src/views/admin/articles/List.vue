<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:20px;">
      <h2 style="margin:0;">文章管理</h2>
      <el-button type="primary" @click="$router.push('/admin/articles/create')">新增文章</el-button>
    </div>

    <el-table :data="articles" v-loading="loading" stripe border style="width:100%;">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="topicName" label="专题" width="120" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column prop="updateTime" label="更新时间" width="170" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editArticle(row.id)">编辑</el-button>
          <el-popconfirm title="确定删除此文章？" @confirm="deleteArticle(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:20px;">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @current-change="loadArticles"
        @size-change="onSizeChange"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getArticles, deleteArticle as delArticle } from '@/composables/useAdmin'

const router = useRouter()
const articles = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

async function loadArticles() {
  loading.value = true
  try {
    const res = await getArticles(pageNum.value, pageSize.value)
    if (res.code === 1) {
      articles.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('加载文章列表失败')
  } finally {
    loading.value = false
  }
}

function editArticle(id) {
  router.push('/admin/articles/' + id + '/edit')
}

async function deleteArticle(id) {
  try {
    const res = await delArticle(id)
    if (res.code === 1) {
      ElMessage.success('删除成功')
      loadArticles()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

function onSizeChange(size) {
  pageSize.value = size
  pageNum.value = 1
  loadArticles()
}

onMounted(loadArticles)
</script>
