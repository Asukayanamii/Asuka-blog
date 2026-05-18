<template>
  <el-container style="min-height:100vh;">
    <el-aside width="220px" class="admin-aside">
      <div class="admin-logo">AsukaBlog Admin</div>
      <el-menu
        :router="true"
        :default-active="route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/admin">
          <el-icon><DataBoard /></el-icon>
          <span>控制台</span>
        </el-menu-item>
        <el-menu-item index="/admin/articles">
          <el-icon><Document /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/topics">
          <el-icon><Collection /></el-icon>
          <span>专题管理</span>
        </el-menu-item>
      </el-menu>
      <div class="logout-area">
        <el-button type="danger" size="small" @click="handleLogout" plain>退出登录</el-button>
      </div>
    </el-aside>
    <el-main style="background:#f0f2f5;padding:24px;">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { DataBoard, Document, Collection } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

function handleLogout() {
  localStorage.removeItem('loginUser')
  router.push('/login')
}
</script>

<style scoped>
.admin-aside {
  background: #304156;
  display: flex;
  flex-direction: column;
  position: relative;
}
.admin-logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.1rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.logout-area {
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
}
</style>
