import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token    = ref(localStorage.getItem('token') || '')
  const userId   = ref(Number(localStorage.getItem('userId')) || null)
  const userName = ref(localStorage.getItem('userName') || '')
  const phone    = ref(localStorage.getItem('phone') || '')

  const isLoggedIn = computed(() => !!token.value)

  async function login(loginData) {
    const res = await authApi.login(loginData)
    const data = res.data.data
    token.value    = data.token
    userId.value   = data.userId
    userName.value = data.userName
    phone.value    = data.phone
    localStorage.setItem('token',    data.token)
    localStorage.setItem('userId',   data.userId)
    localStorage.setItem('userName', data.userName)
    localStorage.setItem('phone',    data.phone)
  }

  function logout() {
    token.value    = ''
    userId.value   = null
    userName.value = ''
    phone.value    = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('userName')
    localStorage.removeItem('phone')
  }

  return { token, userId, userName, phone, isLoggedIn, login, logout }
})
