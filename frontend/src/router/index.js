import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/blog',
    name: 'Home',
    component: () => import('../views/front/Home.vue'),
    meta: { title: '首页' },
  },
  {
    path: '/admin',
    component: () => import('../views/admin/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/admin/blogs' },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../views/admin/UserManage.vue'),
        meta: { title: '用户管理', role: 'admin' },
      },
      {
        path: 'blogs',
        name: 'AdminBlogs',
        component: () => import('../views/admin/BlogManage.vue'),
        meta: { title: '博客文章管理' },
      },
      {
        path: 'messages',
        name: 'AdminMessages',
        component: () => import('../views/admin/MessageManage.vue'),
        meta: { title: '留言板管理' },
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('../views/admin/ProfileManage.vue'),
        meta: { title: '个人介绍管理' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()
  document.title = to.meta.title ? `${to.meta.title} - 个人博客` : '个人博客'

  if (to.path.startsWith('/admin') && auth.role === 'guest') {
    return next('/blog')
  }

  if (to.path === '/blog' && auth.isAdmin) {
    return next('/admin')
  }

  if (to.meta.requiresAuth) {
    if (!auth.isLoggedIn) {
      return next('/login')
    }
    if (to.meta.role && auth.role !== to.meta.role) {
      return next('/blog')
    }
  }

  next()
})

export default router