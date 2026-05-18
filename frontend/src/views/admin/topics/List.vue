<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:20px;">
      <h2 style="margin:0;">专题管理</h2>
      <el-button type="primary" @click="$router.push('/admin/topics/create')">新增专题</el-button>
    </div>

    <el-table :data="topics" v-loading="loading" stripe border style="width:100%;">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="topicName" label="名称" width="160" />
      <el-table-column prop="description" label="描述" min-width="300" show-overflow-tooltip />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editTopic(row.id)">编辑</el-button>
          <el-popconfirm title="确定删除此专题？" @confirm="deleteTopic(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTopics, deleteTopic as delTopic } from '@/composables/useAdmin'

const router = useRouter()
const topics = ref([])
const loading = ref(false)

async function loadTopics() {
  loading.value = true
  try {
    const res = await getTopics()
    if (res.code === 1) {
      topics.value = res.data || []
    }
  } catch (e) {
    ElMessage.error('加载专题列表失败')
  } finally {
    loading.value = false
  }
}

function editTopic(id) {
  router.push('/admin/topics/' + id + '/edit')
}

async function deleteTopic(id) {
  try {
    const res = await delTopic(id)
    if (res.code === 1) {
      ElMessage.success('删除成功')
      loadTopics()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

onMounted(loadTopics)
</script>
