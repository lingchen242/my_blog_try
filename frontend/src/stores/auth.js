import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '../utils/request'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const role = ref(localStorage.getItem('role') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 'admin')

  function setAuth(t, r, n) {
    token.value = t
    role.value = r
    nickname.value = n
    localStorage.setItem('token', t)
    localStorage.setItem('role', r)
    localStorage.setItem('nickname', n)
  }

  async function login(username, password) {
    const res = await request.post('/auth/login', { username, password })
    setAuth(res.data.token, res.data.role, res.data.nickname)
    return res.data
  }

  function logout() {
    token.value = ''
    role.value = ''
    nickname.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('nickname')
  }

  return { token, role, nickname, isLoggedIn, isAdmin, login, logout, setAuth }
})