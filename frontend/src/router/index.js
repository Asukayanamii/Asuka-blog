import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../views/main/index.vue'
import HomePage from '../views/homepage/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: MainLayout,
      children: [
        {
          path: '',
          name: 'home',
          component: HomePage,
        },
        {
          path: 'about',
          name: 'about',
          component: () => import('../views/AboutView.vue'),
        },
        {
          path: 'articles',
          name: 'articles',
          component: () => import('../views/articles/index.vue'),
        },
        {
          path: 'articles/:id',
          name: 'articleDetail',
          component: () => import('../views/articles/detail.vue'),
        },
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/admin/LoginView.vue'),
    },
    {
      path: '/admin',
      component: () => import('../views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'adminHome',
          component: () => import('../views/admin/AdminHome.vue'),
        },
        {
          path: 'articles',
          name: 'adminArticles',
          component: () => import('../views/admin/articles/List.vue'),
        },
        {
          path: 'articles/create',
          name: 'adminArticleCreate',
          component: () => import('../views/admin/articles/Form.vue'),
        },
        {
          path: 'articles/:id/edit',
          name: 'adminArticleEdit',
          component: () => import('../views/admin/articles/Form.vue'),
        },
        {
          path: 'topics',
          name: 'adminTopics',
          component: () => import('../views/admin/topics/List.vue'),
        },
        {
          path: 'topics/create',
          name: 'adminTopicCreate',
          component: () => import('../views/admin/topics/Form.vue'),
        },
        {
          path: 'topics/:id/edit',
          name: 'adminTopicEdit',
          component: () => import('../views/admin/topics/Form.vue'),
        },
      ],
    },
  ],
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    try {
      const loginUser = JSON.parse(localStorage.getItem('loginUser'))
      if (!loginUser || !loginUser.token) {
        next({ name: 'login' })
        return
      }
    } catch (e) {
      next({ name: 'login' })
      return
    }
  }
  next()
})

export default router
