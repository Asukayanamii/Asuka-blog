import { ref } from 'vue'

const isDark = ref(false)
let initialized = false

function applyTheme() {
  document.documentElement.dataset.theme = isDark.value ? 'dark' : 'light'
}

function initializeTheme() {
  if (initialized) return
  isDark.value = localStorage.getItem('theme') === 'dark'
  applyTheme()
  initialized = true
}

function toggleTheme() {
  isDark.value = !isDark.value
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
  applyTheme()
}

export function useTheme() {
  return { isDark, initializeTheme, toggleTheme }
}
