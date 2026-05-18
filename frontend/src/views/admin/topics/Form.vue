<template>
  <div>
    <h2 style="margin-top:0;">{{ isEdit ? '编辑专题' : '新增专题' }}</h2>
    <el-form :model="form" label-width="120px" v-loading="loading">
      <el-form-item label="专题名称" required>
        <el-input v-model="form.topicName" placeholder="专题名称" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="专题描述" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save" :loading="saving">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
        <el-button @click="$router.push('/admin/topics')">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createTopic, updateTopic, getTopicDetail } from '@/composables/useAdmin'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)

const loading = ref(false)
const saving = ref(false)
const form = reactive({ topicName: '', description: '' })

async function loadTopic() {
  if (!isEdit.value) return
  loading.value = true
  try {
    const res = await getTopicDetail(route.params.id)
    if (res.code === 1 && res.data) {
      form.topicName = res.data.topicName || ''
      form.description = res.data.description || ''
    } else {
      ElMessage.error('专题不存在')
    }
  } catch (e) {
    ElMessage.error('加载专题失败')
  } finally {
    loading.value = false
  }
}

async function save() {
  if (!form.topicName) {
    ElMessage.warning('请输入专题名称')
    return
  }
  saving.value = true
  try {
    let res
    if (isEdit.value) {
      res = await updateTopic(route.params.id, form)
    } else {
      res = await createTopic(form)
    }
    if (res.code === 1) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      router.push('/admin/topics')
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(loadTopic)
</script>
