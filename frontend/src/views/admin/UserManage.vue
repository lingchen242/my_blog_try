<template>
  <div class="manage-card">
    <h3>用户管理</h3>
    <el-table :data="users" stripe style="width:100%">
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'admin' ? 'danger' : 'info'">{{ row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="150" />
      <el-table-column prop="createTime" label="注册时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, deleteUser } from '../../api/user'

const users = ref([])

function formatTime(t) {
  if (!t) return ''
  return t.replace('T', ' ').substring(0, 16)
}

async function loadUsers() {
  try {
    const res = await getUserList()
    users.value = res.data || []
  } catch { ElMessage.error('加载用户列表失败') }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除用户 ${row.username} 吗？`, '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',
    })
    await deleteUser(row.id); ElMessage.success('删除成功'); loadUsers()
  } catch { /* 取消 */ }
}

onMounted(loadUsers)
</script>

<style scoped>
.manage-card { background: #fff; border-radius: 8px; padding: 24px; }
.manage-card h3 { margin: 0 0 20px; font-size: 18px; }
</style>