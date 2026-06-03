<template>
  <div>
    <h2 style="margin-top:0;">控制台</h2>
    <p>欢迎回来, {{ adminName }}</p>
    <el-row :gutter="20" style="margin-top:20px;">
      <el-col :xs="24" :sm="12" :lg="8" style="margin-bottom:16px;">
        <el-card>
          <p style="color:#8a9bb8;margin:0 0 8px;">文章总数</p>
          <p style="font-size:2rem;font-weight:700;margin:0;color:#2a3a5f;">{{ articleCount }}</p>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="8" style="margin-bottom:16px;">
        <el-card>
          <p style="color:#8a9bb8;margin:0 0 8px;">专题总数</p>
          <p style="font-size:2rem;font-weight:700;margin:0;color:#2a3a5f;">{{ topicCount }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticles, getTopics } from '@/composables/useAdmin'

const adminName = ref('')
const articleCount = ref(0)
const topicCount = ref(0)

onMounted(async () => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser') || '{}')
  adminName.value = loginUser?.username || 'Admin'

  try {
    const articleRes = await getArticles(1, 1)
    if (articleRes.code === 1) {
      articleCount.value = articleRes.data.total || 0
    }
  } catch (e) { /* ignore */ }

  try {
    const topicRes = await getTopics()
    if (topicRes.code === 1) {
      topicCount.value = topicRes.data?.length || 0
    }
  } catch (e) { /* ignore */ }
})
</script>
