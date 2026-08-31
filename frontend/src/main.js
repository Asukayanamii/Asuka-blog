import './assets/main.css'
import 'katex/dist/katex.min.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { useTheme } from './composables/useTheme'

const app = createApp(App)

useTheme().initializeTheme()
app.use(router)
app.use(ElementPlus)
app.mount('#app')
