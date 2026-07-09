<template>
  <div class="login-wrapper">
    <div class="login-card">
      <h2 class="login-title">个人博客 · 登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" show-password :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-btn" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-divider style="margin:8px 0"><span style="font-size:12px;color:#999">或</span></el-divider>
      <el-button type="success" :loading="loading" class="login-btn" @click="handleGuestLogin">
        游客登录
      </el-button>
      <p class="login-hint">
        示例账号：admin / 123456（管理员）| user / 123456（普通用户）
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const auth = useAuthStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const data = await auth.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push(data.role === 'admin' ? '/admin' : '/blog')
  } catch { ElMessage.error('用户名或密码错误') }
  finally { loading.value = false }
}

async function handleGuestLogin() {
  loading.value = true
  try {
    const res = await request.post('/auth/guest-login')
    auth.setAuth(res.data.token, res.data.role, res.data.nickname)
    ElMessage.success('游客登录成功')
    router.push('/blog')
  } catch { ElMessage.error('游客登录失败') }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-wrapper { display:flex; justify-content:center; align-items:center; min-height:100vh; background:linear-gradient(135deg,#667eea 0%,#764ba2 100%); }
.login-card { width:400px; padding:40px; background:#fff; border-radius:12px; box-shadow:0 8px 30px rgba(0,0,0,0.15); }
.login-title { text-align:center; margin:0 0 32px; font-size:22px; color:#333; }
.login-btn { width:100%; }
.login-hint { text-align:center; margin:16px 0 0; font-size:12px; color:#999; }
</style>