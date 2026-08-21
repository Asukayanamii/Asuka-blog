<template>
  <div class="login-container">
    <button
      class="theme-toggle"
      type="button"
      :title="isDark ? '切换日间模式' : '切换夜间模式'"
      :aria-label="isDark ? '切换日间模式' : '切换夜间模式'"
      @click="toggleTheme"
    >{{ isDark ? '☀' : '☾' }}</button>
    <el-card class="login-card">
      <template #header>
        <h2 style="text-align:center;margin:0;">AsukaBlog 管理登录</h2>
      </template>
      <el-form :model="form" label-width="0" @submit.prevent="handleLogin">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" show-password :prefix-icon="Lock" size="large" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width:100%;" size="large">
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
        <p v-if="errorMsg" style="color:#f56c6c;text-align:center;margin:0;">{{ errorMsg }}</p>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { adminLogin } from '@/composables/useAdmin'
import { useTheme } from '@/composables/useTheme'

const router = useRouter()
const loading = ref(false)
const errorMsg = ref('')
const form = reactive({ username: '', password: '' })
const { isDark, toggleTheme } = useTheme()

async function handleLogin() {
  if (!form.username || !form.password) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await adminLogin(form.username, form.password)
    if (res.code === 1) {
      localStorage.setItem('loginUser', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.push('/admin')
    } else {
      errorMsg.value = res.msg || '登录失败'
    }
  } catch (e) {
    errorMsg.value = '登录请求失败，请检查网络'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  box-sizing: border-box;
  background: linear-gradient(135deg, #f0f4ff 0%, #ffe8f5 50%, #f0f8ff 100%);
}
.login-card {
  width: 400px;
  max-width: 100%;
}
.theme-toggle {
  position: absolute;
  top: 20px;
  right: 20px;
  display: grid;
  width: 38px;
  height: 38px;
  padding: 0;
  color: #4c4948;
  background: rgba(255, 255, 255, 0.76);
  border: 0;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  font-size: 1.35rem;
  line-height: 1;
  place-items: center;
}
:global(html[data-theme='dark']) .theme-toggle {
  color: #d8e0e6;
  background: #2a3137;
}
</style>
