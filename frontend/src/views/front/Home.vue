<template>
  <div class="home">
    <header class="header">
      <div class="header-inner">
        <h1 class="logo">My Blog</h1>
        <div class="header-right" v-if="authStore.isLoggedIn">
          <el-button v-if="authStore.role !== 'guest'" type="primary" @click="goToAdmin" text>后台管理</el-button>
          <el-button type="danger" @click="handleLogout" text>退出登录</el-button>
        </div>
        <div class="header-right" v-else>
          <el-button type="primary" @click="goToLogin" text>登录</el-button>
        </div>
      </div>
    </header>

    <main class="main">
      <section class="section profile-section" v-if="profile">
        <h2 class="section-title">{{ profile.title || '关于我' }}</h2>
        <div class="profile-content">{{ profile.content }}</div>
      </section>

      <section class="section">
        <h2 class="section-title">博客文章</h2>
        <div v-if="blogs.length === 0" class="empty">暂无文章</div>
        <div v-for="blog in blogs" :key="blog.id" class="blog-card">
          <div class="blog-header">
            <h3 class="blog-title">{{ blog.title }}</h3>
            <el-tag v-if="blog.category" size="small">{{ blog.category }}</el-tag>
          </div>
          <p class="blog-summary">{{ blog.summary }}</p>
          <div class="blog-meta">
            <span>{{ formatTime(blog.createTime) }}</span>
            <el-button type="primary" link @click="viewBlog(blog)">阅读全文</el-button>
          </div>
        </div>
      </section>
    </main>

    <!-- 博客详情对话框（含留言） -->
    <el-dialog v-model="dialogVisible" title="博客详情" width="700px" @closed="blogMessages=[]">
      <template v-if="currentBlog">
        <h2 style="margin-top:0">{{ currentBlog.title }}</h2>
        <div style="margin-bottom:12px">
          <el-tag v-if="currentBlog.category" size="small">{{ currentBlog.category }}</el-tag>
          <span style="color:#999;font-size:14px;margin-left:8px">{{ formatTime(currentBlog.createTime) }}</span>
        </div>
        <div class="blog-detail-content">{{ currentBlog.content }}</div>

        <el-divider />
        <h3 style="margin:0 0 12px">留言 ({{ blogMessages.length }})</h3>

        <div v-if="blogMessages.length === 0" class="empty" style="padding:12px 0">暂无留言，来说两句吧</div>
        <div v-for="msg in blogMessages" :key="msg.id" class="msg-item">
          <div class="msg-header">
            <strong>{{ msg.nickname }}</strong>
            <span class="msg-time">{{ formatTime(msg.createTime) }}</span>
          </div>
          <p class="msg-content">{{ msg.content }}</p>
        </div>

        <div class="message-form" style="margin-top:16px">
          <el-input v-model="msgInputNickname" placeholder="你的昵称" style="margin-bottom:8px" />
          <el-input v-model="msgInputContent" type="textarea" :rows="3" placeholder="写下你想说的话..." style="margin-bottom:8px" />
          <el-button type="primary" :loading="msgLoading" @click="submitBlogMessage">提交留言</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { getBlogList, getMyBlogs } from '../../api/blog'
import { getMessageList, addMessage } from '../../api/message'
import { getProfile } from '../../api/profile'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const profile = ref(null)
const blogs = ref([])
const dialogVisible = ref(false)
const currentBlog = ref(null)
const blogMessages = ref([])
const msgInputNickname = ref('')
const msgInputContent = ref('')
const msgLoading = ref(false)

function formatTime(t) {
  if (!t) return ''
  return t.replace('T', ' ').substring(0, 16)
}

function goToLogin() { router.push('/login') }
function goToAdmin() { router.push('/admin') }
function handleLogout() { authStore.logout(); router.push('/login') }

async function viewBlog(blog) {
  currentBlog.value = blog
  dialogVisible.value = true
  msgInputContent.value = ''
  // 已登录用户自动填充昵称
  msgInputNickname.value = authStore.isLoggedIn ? authStore.nickname : ''
  // 加载该文章下的留言
  try {
    const res = await getMessageList({ blogId: blog.id })
    blogMessages.value = res.data || []
  } catch { console.error('加载留言失败') }
}

async function submitBlogMessage() {
  if (!msgInputNickname.value.trim() || !msgInputContent.value.trim()) {
    ElMessage.warning('请填写昵称和留言内容')
    return
  }
  msgLoading.value = true
  try {
    await addMessage({
      blogId: currentBlog.value.id,
      nickname: msgInputNickname.value,
      content: msgInputContent.value,
    })
    ElMessage.success('留言成功')
    msgInputContent.value = ''
    const res = await getMessageList({ blogId: currentBlog.value.id })
    blogMessages.value = res.data || []
  } catch { ElMessage.error('留言失败') }
  finally { msgLoading.value = false }
}

onMounted(async () => {
  try {
    const blogApi = authStore.isLoggedIn && authStore.role !== 'guest' ? getMyBlogs : getBlogList
    const [blogRes, profileRes] = await Promise.all([blogApi(), getProfile()])
    blogs.value = blogRes.data || []
    profile.value = profileRes.data
  } catch { console.error('加载数据失败') }
})
</script>

<style scoped>
.home { min-height:100vh; background:#f5f5f5; }
.header { background:#fff; border-bottom:1px solid #e8e8e8; position:sticky; top:0; z-index:100; }
.header-inner { max-width:800px; margin:0 auto; padding:0 20px; height:60px; display:flex; align-items:center; justify-content:space-between; }
.header-right { display:flex; align-items:center; gap:8px; }
.logo { margin:0; font-size:22px; color:#333; }
.main { max-width:800px; margin:0 auto; padding:24px 20px; }
.section { background:#fff; border-radius:8px; padding:24px; margin-bottom:20px; box-shadow:0 1px 4px rgba(0,0,0,0.06); }
.section-title { margin:0 0 16px; font-size:18px; color:#333; padding-left:10px; border-left:3px solid #409eff; }
.profile-content { line-height:1.8; color:#555; }
.empty { text-align:center; color:#999; padding:24px 0; }
.blog-card { border-bottom:1px solid #f0f0f0; padding:16px 0; }
.blog-card:last-child { border-bottom:none; }
.blog-header { display:flex; align-items:center; gap:8px; }
.blog-title { margin:0; font-size:18px; color:#333; }
.blog-summary { margin:8px 0; color:#666; line-height:1.6; }
.blog-meta { display:flex; align-items:center; justify-content:space-between; font-size:13px; color:#999; }
.blog-detail-content { line-height:1.8; white-space:pre-wrap; color:#555; }
.msg-item { border-bottom:1px solid #f0f0f0; padding:10px 0; }
.msg-item:last-child { border-bottom:none; }
.msg-header { display:flex; justify-content:space-between; margin-bottom:4px; }
.msg-time { font-size:12px; color:#999; }
.msg-content { margin:0; color:#555; line-height:1.6; }
.message-form { display:flex; flex-direction:column; }
</style>