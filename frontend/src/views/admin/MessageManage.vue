<template>
  <div class="manage-card">
    <h3>留言板管理</h3>

    <div v-if="authStore.isAdmin" class="search-bar">
      <el-input v-model="searchNickname" placeholder="按留言者名称搜索" clearable style="width:220px" />
      <el-button type="primary" @click="loadMessages">搜索</el-button>
      <el-button @click="searchNickname=''; loadMessages()">重置</el-button>
    </div>

    <el-table :data="messages" stripe style="width:100%">
      <el-table-column prop="nickname" label="留言者名称" width="150" />
      <el-table-column prop="content" label="留言内容" min-width="300" />
      <el-table-column prop="createTime" label="留言时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="messages.length === 0" class="empty">暂无留言</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { getMessageList, deleteMessage } from '../../api/message'

const authStore = useAuthStore()
const messages = ref([])
const searchNickname = ref('')

function formatTime(t) {
  if (!t) return ''
  return t.replace('T', ' ').substring(0, 16)
}

async function loadMessages() {
  try {
    const params = authStore.isAdmin && searchNickname.value ? { nickname: searchNickname.value } : undefined
    const res = await getMessageList(params)
    messages.value = res.data || []
  } catch { ElMessage.error('加载留言失败') }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除这条留言吗？', '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',
    })
    await deleteMessage(row.id); ElMessage.success('删除成功'); loadMessages()
  } catch { /* 取消 */ }
}

onMounted(loadMessages)
</script>

<style scoped>
.manage-card { background: #fff; border-radius: 8px; padding: 24px; }
.manage-card h3 { margin: 0 0 20px; font-size: 18px; }
.search-bar { display: flex; gap: 12px; margin-bottom: 16px; align-items: center; }
.empty { text-align: center; color: #999; padding: 40px 0; }
</style>