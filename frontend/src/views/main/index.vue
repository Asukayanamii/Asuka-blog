<template>
    <div class="common-layout">
        <el-container>
            <el-main>
                <section class="page-shell">
                    <nav :class="['top-nav', { hidden: isNavHidden }]">
                        <router-link to="/" class="nav-brand">Asuka's Blog</router-link>
                        <div class="nav-links">
                            <router-link to="/" class="home-link">首页</router-link>
                            <div class="nav-dropdown">
                                <button class="nav-dropdown-btn" @click="toggleDropdown" type="button">
                                    专题
                                    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" :class="{ open: dropdownOpen }">
                                        <path d="M1 5L7 11L13 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                                    </svg>
                                </button>
                                <div v-if="dropdownOpen" class="nav-dropdown-menu">
                                    <div class="categories-list">
                                        <a v-for="cat in categories" :key="cat.id" href="#" class="category-link" @click.prevent="selectCategory(cat)">
                                            {{ cat.topicName }}
                                        </a>
                                        <div v-if="categories.length === 0" class="empty-hint">暂无栏目</div>
                                    </div>
                                </div>
                            </div>
                            <router-link to="/articles">最新</router-link>
                            <router-link to="/about">关于</router-link>
                            <a href="https://github.com/Asukayanamii/Asuka-blog" target="_blank" rel="noopener noreferrer" class="nav-github" title="GitHub 仓库">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"/>
                                </svg>
                            </a>
                        </div>
                    </nav>
                    <router-view />
                </section>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { topics as sharedTopics } from '@/data/topics'
import { loadTopics, topics } from '@/composables/useTopics'

onMounted(async () => {
    const tempTopics = await loadTopics()
    // console.log('loaded topics', topics.value[0])
    // sharedTopics.value = tempTopics
})

const isNavHidden = ref(false)
const dropdownOpen = ref(false)
const selectedCategory = []
const router = useRouter()
// console.log('selectedCategory', selectedCategory)
const categories = computed(() => topics.value)

let lastScrollY = 0
let ticking = false

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

const selectCategory = (category) => {
  dropdownOpen.value = false
  router.push({ name: 'articles', query: { topicId: category.id } })
}

const handleScroll = () => {
  const currentScroll = window.scrollY
  if (Math.abs(currentScroll - lastScrollY) < 10) {
    ticking = false
    return
  }

  isNavHidden.value = currentScroll > lastScrollY && currentScroll > 120
  lastScrollY = currentScroll <= 0 ? 0 : currentScroll
  ticking = false
}

const onScroll = () => {
  if (!ticking) {
    window.requestAnimationFrame(handleScroll)
    ticking = true
  }
}

onMounted(() => {
  lastScrollY = window.scrollY
  window.addEventListener('scroll', onScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style>
.page-shell {
    min-height: 100vh;
    padding: 6rem 0 0;
    background: radial-gradient(circle at top left, rgba(91, 124, 255, 0.12), transparent 28%),
        radial-gradient(circle at bottom right, rgba(255, 120, 198, 0.12), transparent 30%),
        linear-gradient(135deg, #f0f4ff 0%, #ffe8f5 50%, #f0f8ff 100%);
    color: #2a3a5f;
    overflow-x: hidden;
}

.top-nav {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    box-sizing: border-box;
    z-index: 50;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.85rem 2rem;
    background: rgba(255, 255, 255, 0.22);
    backdrop-filter: blur(16px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.45);
    transition: transform 0.3s ease, opacity 0.3s ease;
}

.top-nav.hidden {
    transform: translateY(-100%);
    opacity: 0;
}

.nav-brand {
    font-size: 1.35rem;
    font-weight: 800;
    color: #3b4d8f;
    letter-spacing: 0.18em;
    text-decoration: none;
}

.nav-links {
    display: flex;
    align-items: center;
    gap: 1.4rem;
}

.nav-links a {
    text-decoration: none;
    color: #3b4d8f;
    font-weight: 600;
    background: transparent;
    outline: none;
    border-radius: 999px;
    transition: color 0.2s ease, transform 0.2s ease, background 0.2s ease;
}

.nav-links a:hover,
.nav-links a:focus {
    color: #5b7cff;
    transform: translateY(-1px);
    background: transparent;
}

.nav-links a:focus-visible {
    outline: 2px solid rgba(91, 124, 255, 0.3);
    outline-offset: 3px;
}

.nav-dropdown {
    position: relative;
}

.nav-dropdown-btn {
    background: transparent;
    border: none;
    color: #3b4d8f;
    font-weight: 600;
    font-size: 1rem;
    padding: 0.4rem 0.6rem;
    cursor: pointer;
    display: flex;
    align-items: center;
    transition: color 0.2s ease, transform 0.2s ease;
    border-radius: 999px;
}

.nav-dropdown-btn svg {
    transition: transform 0.2s ease;
}

.nav-dropdown-btn svg.open {
    transform: rotate(180deg);
}

.nav-dropdown-btn:hover {
    color: #5b7cff;
    transform: translateY(-1px);
}

.nav-dropdown-btn:focus-visible {
    outline: 2px solid rgba(91, 124, 255, 0.3);
    outline-offset: 3px;
}

.nav-dropdown-menu {
    position: absolute;
    top: calc(100% + 0.85rem);
    right: 0;
    width: 260px;
    background: transparent;
    border-radius: 12px;
    box-shadow: 0 12px 40px rgba(91, 124, 255, 0.15);
    border: 1px solid rgba(91, 124, 255, 0.1);
    z-index: 100;
    animation: slideDown 0.2s ease;
}

.common-layout .el-main {
    padding: 0;
}

@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-8px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.categories-list {
    max-height: 240px;
    overflow-y: auto;
    overflow-x: hidden;
    padding: 0.5rem 0;
}

.categories-list::-webkit-scrollbar {
    width: 0;
    height: 0;
}

.categories-list {
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.category-link {
    display: block;
    padding: 0.85rem 1rem;
    color: #5a6a8a;
    text-decoration: none;
    border-bottom: 1px solid rgba(91, 124, 255, 0.05);
    transition: background-color 0.2s ease, color 0.2s ease;
}

.category-link:last-child {
    border-bottom: none;
}

.category-link:hover,
.category-link:focus {
    background-color: rgba(91, 124, 255, 0.08);
    color: #3b4d8f;
}

.empty-hint {
    padding: 1rem;
    text-align: center;
    color: #9a9faa;
    font-size: 0.9rem;
    font-style: italic;
}

.nav-github {
    display: flex;
    align-items: center;
    color: #3b4d8f;
    transition: color 0.2s ease, transform 0.2s ease;
}

.nav-github:hover {
    color: #5b7cff;
    transform: translateY(-1px);
}
</style>
