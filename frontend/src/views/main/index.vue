<template>
  <div class="common-layout">
    <nav :class="['top-nav', { 'top-nav-solid': !isHome || isScrolled, 'menu-open': mobileOpen }]">
      <router-link to="/" class="nav-brand" @click="mobileOpen = false">Asuka's Blog</router-link>

      <button class="menu-toggle" type="button" aria-label="打开导航" @click="mobileOpen = !mobileOpen">
        <span></span><span></span><span></span>
      </button>

      <div class="nav-links" :class="{ open: mobileOpen }">
        <router-link to="/" class="home-link" @click="mobileOpen = false">首页</router-link>
        <div class="nav-dropdown">
          <button class="nav-dropdown-btn" type="button" @click="toggleDropdown">
            专题
            <span class="dropdown-arrow" :class="{ open: dropdownOpen }"></span>
          </button>
          <div v-if="dropdownOpen" class="nav-dropdown-menu">
            <a
              v-for="cat in categories"
              :key="cat.id"
              href="#"
              class="category-link"
              @click.prevent="selectCategory(cat)"
            >{{ cat.topicName }}</a>
            <div v-if="categories.length === 0" class="empty-hint">暂无栏目</div>
          </div>
        </div>
        <router-link to="/articles" @click="mobileOpen = false">最新</router-link>
        <router-link to="/about" @click="mobileOpen = false">关于</router-link>
        <button
          class="theme-toggle"
          type="button"
          :title="isDark ? '切换日间模式' : '切换夜间模式'"
          :aria-label="isDark ? '切换日间模式' : '切换夜间模式'"
          @click="toggleTheme"
        ><span aria-hidden="true">{{ isDark ? '☀' : '☾' }}</span></button>
        <a
          href="https://github.com/Asukayanamii/Asuka-blog"
          target="_blank"
          rel="noopener noreferrer"
          class="nav-github"
          title="GitHub 仓库"
          aria-label="GitHub 仓库"
        >
          <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z" /></svg>
        </a>
      </div>
    </nav>

    <main class="page-shell">
      <router-view />
    </main>
    <div id="beian-container"></div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { loadTopics, topics } from '@/composables/useTopics'
import { useTheme } from '@/composables/useTheme'

const route = useRoute()
const router = useRouter()
const dropdownOpen = ref(false)
const mobileOpen = ref(false)
const isScrolled = ref(false)
const { isDark, initializeTheme, toggleTheme } = useTheme()
const categories = computed(() => topics.value)
const isHome = computed(() => route.name === 'home')

function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value
}

function selectCategory(category) {
  dropdownOpen.value = false
  mobileOpen.value = false
  router.push({ name: 'articles', query: { topicId: category.id } })
}

function updateNav() {
  isScrolled.value = window.scrollY > 32
}

watch(() => route.fullPath, () => {
  dropdownOpen.value = false
  mobileOpen.value = false
  updateNav()
})

onMounted(async () => {
  initializeTheme()
  await loadTopics()
  updateNav()
  window.addEventListener('scroll', updateNav, { passive: true })

  fetch('/beian.local.html')
    .then((response) => response.text())
    .then((html) => {
      const el = document.getElementById('beian-container')
      if (el) {
        el.innerHTML = html
        el.classList.add('show')
      }
    })
    .catch(() => {})
})

onUnmounted(() => window.removeEventListener('scroll', updateNav))
</script>

<style>
.common-layout {
  min-height: 100vh;
  color: var(--blog-ink);
}

.page-shell {
  min-height: calc(100vh - 48px);
  overflow: hidden;
}

.top-nav {
  position: fixed;
  inset: 0 0 auto;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  min-height: 64px;
  padding: 0 5vw;
  color: #fff;
  background: rgba(27, 33, 38, 0.06);
  border-bottom: 1px solid rgba(255, 255, 255, 0.24);
  transition: background 0.25s ease, color 0.25s ease, box-shadow 0.25s ease;
}

.top-nav-solid {
  color: var(--blog-ink);
  background: rgba(255, 255, 255, 0.92);
  border-bottom-color: rgba(0, 0, 0, 0.05);
  box-shadow: 0 2px 12px rgba(27, 31, 35, 0.08);
  backdrop-filter: blur(12px);
}

.nav-brand {
  color: inherit;
  font-size: 1.1rem;
  font-weight: 700;
  letter-spacing: 0;
  text-decoration: none;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 1.6rem;
}

.nav-links > a,
.nav-dropdown-btn {
  color: inherit;
  background: transparent;
  border: 0;
  font-size: 0.92rem;
  line-height: 64px;
  text-decoration: none;
  transition: color 0.2s ease;
}

.nav-links > a.router-link-exact-active,
.nav-links > a:hover,
.nav-dropdown-btn:hover {
  color: var(--blog-blue);
}

.nav-dropdown {
  position: relative;
}

.nav-dropdown-btn {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0;
}

.dropdown-arrow {
  width: 7px;
  height: 7px;
  border-right: 1px solid currentColor;
  border-bottom: 1px solid currentColor;
  transform: rotate(45deg) translateY(-2px);
  transition: transform 0.2s ease;
}

.dropdown-arrow.open {
  transform: rotate(225deg) translate(-2px, -2px);
}

.nav-dropdown-menu {
  position: absolute;
  top: calc(100% - 8px);
  right: -1rem;
  width: 210px;
  max-height: 260px;
  overflow-y: auto;
  padding: 0.45rem 0;
  color: var(--blog-ink);
  background: rgba(255, 255, 255, 0.98);
  border-radius: 4px;
  box-shadow: 0 8px 24px rgba(27, 31, 35, 0.16);
}

.category-link,
.empty-hint {
  display: block;
  padding: 0.65rem 1rem;
  color: var(--blog-ink);
  font-size: 0.88rem;
  text-decoration: none;
}

.category-link:hover {
  color: #fff;
  background: var(--blog-blue);
}

.empty-hint {
  color: var(--blog-muted);
  text-align: center;
}

.nav-github {
  display: flex;
  align-items: center;
  height: 64px;
}

.theme-toggle {
  display: grid;
  width: 30px;
  height: 30px;
  padding: 0;
  color: inherit;
  background: transparent;
  border: 0;
  border-radius: 50%;
  font-size: 1.25rem;
  line-height: 1;
  place-items: center;
  transition: color 0.2s ease, background 0.2s ease;
}

.theme-toggle:hover {
  color: var(--blog-blue);
  background: rgba(73, 177, 245, 0.12);
}

.nav-github svg {
  width: 18px;
  height: 18px;
  fill: currentColor;
}

.menu-toggle {
  display: none;
  width: 38px;
  height: 38px;
  padding: 8px;
  color: inherit;
  background: transparent;
  border: 0;
}

.menu-toggle span {
  display: block;
  width: 22px;
  height: 2px;
  margin: 4px auto;
  background: currentColor;
}

#beian-container {
  display: flex;
  justify-content: center;
  min-height: 48px;
  padding: 1rem;
  color: #858585;
  background: #fff;
  font-size: 0.78rem;
}

@media (max-width: 680px) {
  .top-nav {
    min-height: 56px;
    padding: 0 1.1rem;
  }

  .menu-toggle {
    display: block;
  }

  .nav-links {
    position: absolute;
    top: 56px;
    right: 0;
    left: 0;
    display: none;
    align-items: stretch;
    flex-direction: column;
    gap: 0;
    padding: 0.45rem 1.1rem 0.8rem;
    color: var(--blog-ink);
    background: rgba(255, 255, 255, 0.98);
    box-shadow: 0 8px 16px rgba(27, 31, 35, 0.12);
  }

  .nav-links.open {
    display: flex;
  }

  .nav-links > a,
  .nav-dropdown-btn,
  .nav-github,
  .theme-toggle {
    height: 42px;
    line-height: 42px;
  }

  .theme-toggle {
    width: 42px;
  }

  .nav-dropdown-menu {
    position: static;
    width: 100%;
    max-height: 190px;
    margin-bottom: 0.35rem;
    box-shadow: none;
    border: 1px solid var(--blog-line);
  }
}

html[data-theme='dark'] .top-nav-solid {
  color: #d8e0e6;
  background: rgba(27, 31, 35, 0.96);
  border-color: rgba(255, 255, 255, 0.08);
}

html[data-theme='dark'] .nav-dropdown-menu {
  color: #d8e0e6;
  background: #20262b;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.36);
}

html[data-theme='dark'] .category-link {
  color: #d8e0e6;
}

@media (max-width: 680px) {
  html[data-theme='dark'] .nav-links {
    color: #d8e0e6;
    background: rgba(27, 31, 35, 0.96);
    border-color: rgba(255, 255, 255, 0.08);
  }
}
</style>
