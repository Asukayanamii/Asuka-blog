<template>
    <div class="common-layout">
        <el-container>
            <el-main>
                <section class="page-shell">
                    <nav :class="['top-nav', { hidden: isNavHidden }]">
                        <div class="nav-brand">Asuka's Blog</div>
                        <div class="nav-links">
                            <router-link to="/">首页</router-link>
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
                            <a href="#">最新</a>
                            <router-link to="/about">关于</router-link>
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
import { topics as sharedTopics } from '@/data/topics'
import { loadTopics, topics } from '@/composables/useTopics'
import { describe } from 'vitest'

onMounted(async () => {
    const tempTopics = await loadTopics()
    // console.log('loaded topics', topics.value[0])
    // sharedTopics.value = tempTopics
})

const isNavHidden = ref(false)
const dropdownOpen = ref(false)
const selectedCategory = []
// console.log('selectedCategory', selectedCategory)
const categories = computed(() => topics.value)

let lastScrollY = 0
let ticking = false

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

const selectCategory = (category) => {
  dropdownOpen.value = false
  // 这里可以添加跳转逻辑或分类筛选逻辑
  console.log('selected category', category)
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
    width: 100vw;
    position: relative;
    left: 50%;
    right: 50%;
    margin-left: -50vw;
    margin-right: -50vw;
}

.top-nav {
    position: fixed;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 100vw;
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
    transform: translate(-50%, -100%);
    opacity: 0;
}

.nav-brand {
    font-size: 1.35rem;
    font-weight: 800;
    color: #3b4d8f;
    letter-spacing: 0.18em;
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
</style>
