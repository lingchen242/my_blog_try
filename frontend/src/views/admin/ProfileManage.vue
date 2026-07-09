<template>
  <div class="manage-card">
    <h3>个人介绍管理</h3>
    <el-form label-width="80px">
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="如：关于我" />
      </el-form-item>
      <el-form-item label="介绍内容">
        <el-input v-model="form.content" type="textarea" :rows="12" placeholder="请在此输入个人介绍内容" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSave">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile } from '../../api/profile'

const form = reactive({ title: '', content: '' })
const loading = ref(false)

async function loadProfile() {
  try {
    const res = await getProfile()
    if (res.data) {
      form.title = res.data.title || ''
      form.content = res.data.content || ''
    }
  } catch { ElMessage.error('加载个人介绍失败') }
}

async function handleSave() {
  loading.value = true
  try {
    await updateProfile({ title: form.title, content: form.content })
    ElMessage.success('保存成功')
  } catch { ElMessage.error('保存失败') }
  finally { loading.value = false }
}

onMounted(loadProfile)
</script>

<style scoped>
.manage-card { background: #fff; border-radius: 8px; padding: 24px; max-width: 700px; }
.manage-card h3 { margin: 0 0 20px; font-size: 18px; }
</style>