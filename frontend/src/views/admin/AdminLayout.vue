<template>
  <el-container style="min-height:100vh;">
    <!-- mobile backdrop -->
    <div v-if="mobileMenuOpen" class="mobile-backdrop" @click="mobileMenuOpen = false" />

    <!-- sidebar -->
    <el-aside :class="['admin-aside', { 'mobile-show': mobileMenuOpen }]">
      <div class="admin-logo">
        <span>AsukaBlog Admin</span>
        <button class="close-btn" @click="mobileMenuOpen = false">&times;</button>
      </div>
      <el-menu
        :router="true"
        :default-active="route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        @select="onMenuSelect"
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

    <el-main class="admin-main">
      <div class="mobile-header">
        <button class="hamburger" @click="mobileMenuOpen = true">
          <span></span><span></span><span></span>
        </button>
        <span class="mobile-title">AsukaBlog Admin</span>
      </div>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { DataBoard, Document, Collection } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const mobileMenuOpen = ref(false)

function onMenuSelect() {
  mobileMenuOpen.value = false
}

function handleLogout() {
  localStorage.removeItem('loginUser')
  router.push('/login')
}
</script>

<style scoped>
.admin-aside {
  width: 220px;
  background: #304156;
  display: flex;
  flex-direction: column;
  position: relative;
  transition: transform 0.3s ease;
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
  position: relative;
}
.close-btn {
  display: none;
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #bfcbd9;
  font-size: 1.5rem;
  cursor: pointer;
  line-height: 1;
}
.logout-area {
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
}
.mobile-header {
  display: none;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #304156;
  color: #fff;
}
.hamburger {
  display: flex;
  flex-direction: column;
  gap: 4px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}
.hamburger span {
  display: block;
  width: 20px;
  height: 2px;
  background: #bfcbd9;
  border-radius: 2px;
}
.mobile-title {
  font-weight: 700;
  font-size: 1rem;
  letter-spacing: 0.05em;
}
.mobile-backdrop {
  display: none;
}

.admin-main {
  padding: 24px;
}

/* tablet & mobile */
@media (max-width: 768px) {
  .admin-aside {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    z-index: 1000;
    transform: translateX(-100%);
  }
  .admin-aside.mobile-show {
    transform: translateX(0);
  }
  .close-btn {
    display: block;
  }
  .mobile-header {
    display: flex;
  }
  .mobile-backdrop {
    display: block;
    position: fixed;
    inset: 0;
    background: rgba(0,0,0,0.4);
    z-index: 999;
  }
  .admin-main {
    padding: 16px !important;
  }
}
</style>
