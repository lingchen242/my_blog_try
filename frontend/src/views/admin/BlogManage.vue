<template>
  <div class="manage-card">
    <div class="manage-header">
      <h3>博客文章管理</h3>
      <el-button v-if="!authStore.isAdmin" type="primary" @click="openAddDialog">
        <el-icon><Plus /></el-icon>新增博客
      </el-button>
    </div>

    <!-- 管理员：搜索栏 -->
    <div v-if="authStore.isAdmin" class="search-bar">
      <el-input v-model="searchUsername" placeholder="按用户名搜索" clearable style="width:180px" />
      <el-input v-model="searchTitle" placeholder="按文章标题搜索" clearable style="width:220px" />
      <el-button type="primary" @click="loadData">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 管理员表格 -->
    <el-table v-if="authStore.isAdmin" :data="blogs" stripe style="width:100%">
      <el-table-column prop="authorName" label="作者" width="120" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="summary" label="摘要" min-width="250" show-overflow-tooltip />
      <el-table-column prop="createTime" label="发布时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 普通用户表格 -->
    <el-table v-else :data="blogs" stripe style="width:100%">
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column prop="createTime" label="发布时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEditDialog(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑博客' : '新增博客'"
      width="700px" :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="请输入正文内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { getMyBlogs, getAdminAllBlogs, addBlog, updateBlog, deleteBlog } from '../../api/blog'

const authStore = useAuthStore()
const blogs = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)
const searchUsername = ref('')
const searchTitle = ref('')

const form = reactive({ title: '', category: '', summary: '', content: '' })
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
}

function formatTime(t) {
  if (!t) return ''
  return t.replace('T', ' ').substring(0, 16)
}

function resetForm() {
  form.title = ''; form.category = ''; form.summary = ''; form.content = ''
  editId.value = null
}

function openAddDialog() { isEdit.value = false; resetForm(); dialogVisible.value = true }
function openEditDialog(row) {
  isEdit.value = true; editId.value = row.id
  Object.assign(form, { title: row.title, category: row.category || '', summary: row.summary || '', content: row.content })
  dialogVisible.value = true
}

function resetSearch() { searchUsername.value = ''; searchTitle.value = ''; loadData() }

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) { await updateBlog(editId.value, form); ElMessage.success('更新成功') }
    else { await addBlog(form); ElMessage.success('新增成功') }
    dialogVisible.value = false; loadData()
  } catch { ElMessage.error('操作失败') }
  finally { submitLoading.value = false }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    await deleteBlog(row.id); ElMessage.success('删除成功'); loadData()
  } catch { /* 取消 */ }
}

async function loadData() {
  try {
    if (authStore.isAdmin) {
      const res = await getAdminAllBlogs(searchUsername.value, searchTitle.value)
      blogs.value = res.data || []
    } else {
      const res = await getMyBlogs()
      blogs.value = res.data || []
    }
  } catch { ElMessage.error('加载失败') }
}

onMounted(loadData)
</script>

<style scoped>
.manage-card { background: #fff; border-radius: 8px; padding: 24px; }
.manage-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.manage-header h3 { margin: 0; font-size: 18px; }
.search-bar { display: flex; gap: 12px; margin-bottom: 16px; align-items: center; }
</style>