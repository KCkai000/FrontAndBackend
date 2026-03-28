import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 每個請求自動附加 JWT Token
api.interceptors.request.use(config => {
  const authStore = useAuthStore()
  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`
  }
  return config
})

// 統一處理 401 - token 過期自動登出（登入/註冊 API 本身的 401 不重新導向）
api.interceptors.response.use(
  response => response,
  error => {
    const isAuthEndpoint = error.config?.url?.includes('/auth/')
    if (error.response?.status === 401 && !isAuthEndpoint) {
      const authStore = useAuthStore()
      authStore.logout()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// ── Auth ────────────────────────────────────────────────────
export const authApi = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data)
}

// ── Posts ───────────────────────────────────────────────────
export const postApi = {
  getAll: () => api.get('/posts'),
  getById: (postId) => api.get(`/posts/${postId}`),
  create: (data) => api.post('/posts', data),
  update: (postId, data) => api.put(`/posts/${postId}`, data),
  delete: (postId) => api.delete(`/posts/${postId}`)
}

// ── Comments ─────────────────────────────────────────────────
export const commentApi = {
  getByPost: (postId) => api.get(`/posts/${postId}/comments`),
  create: (postId, data) => api.post(`/posts/${postId}/comments`, data)
}

export default api
