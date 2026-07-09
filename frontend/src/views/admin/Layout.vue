<template>
  <el-container class="admin-layout">
    <el-aside width="220px" class="admin-aside">
      <div class="aside-title">博客后台</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <template v-for="item in menuItems" :key="item.index">
          <el-menu-item :index="item.index">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="admin-header">
        <span class="header-welcome">欢迎，{{ authStore.nickname }}</span>
        <div class="header-right">
          <el-button type="primary" text @click="goHome">返回首页</el-button>
          <el-button type="danger" plain size="small" @click="handleLogout">
            退出登录
          </el-button>
        </div>
      </el-header>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { Document, ChatDotRound, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const activeMenu = computed(() => route.path)

const menuItems = computed(() => {
  if (authStore.isAdmin) {
    return [
      { index: '/admin/users', label: '用户管理', icon: 'User' },
      { index: '/admin/blogs', label: '博客文章管理', icon: 'Document' },
      { index: '/admin/messages', label: '留言板管理', icon: 'ChatDotRound' },
    ]
  }
  return [
    { index: '/admin/blogs', label: '博客文章管理', icon: 'Document' },
    { index: '/admin/profile', label: '个人介绍管理', icon: 'User' },
    { index: '/admin/messages', label: '留言板管理', icon: 'ChatDotRound' },
  ]
})

function goHome() { router.push('/blog') }
function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout { height: 100vh; }
.admin-aside { background-color: #304156; overflow: hidden; }
.admin-aside .el-menu { border-right: none; }
.aside-title {
  height: 60px; display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 18px; font-weight: bold;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.admin-header {
  display: flex; align-items: center; justify-content: space-between;
  background: #fff; border-bottom: 1px solid #e8e8e8; padding: 0 24px;
}
.header-right { display: flex; align-items: center; gap: 12px; }
.header-welcome { color: #333; font-size: 14px; }
.admin-main { background: #f0f2f5; padding: 24px; }
</style>