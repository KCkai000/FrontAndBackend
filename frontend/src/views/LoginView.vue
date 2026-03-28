<template>
  <div class="auth-page">
    <div class="auth-card card">
      <h2 class="auth-title">登入帳號</h2>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>手機號碼</label>
          <input
            v-model="form.phone"
            type="tel"
            placeholder="09xxxxxxxx"
            maxlength="10"
            required
          />
        </div>

        <div class="form-group">
          <label>密碼</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="請輸入密碼"
            required
          />
        </div>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

        <button type="submit" class="btn btn-primary" style="width:100%;margin-top:8px" :disabled="loading">
          {{ loading ? '登入中...' : '登入' }}
        </button>
      </form>

      <p class="auth-footer">
        還沒有帳號？
        <RouterLink to="/register" class="link">立即註冊</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router   = useRouter()
const authStore = useAuthStore()

const form = reactive({ phone: '', password: '' })
const errorMsg = ref('')
const loading  = ref(false)

async function handleLogin() {
  errorMsg.value = ''
  loading.value  = true
  try {
    await authStore.login({ phone: form.phone, password: form.password })
    router.push({ name: 'Home' })
  } catch (err) {
    errorMsg.value = err.response?.data?.message || '登入失敗，請稍後再試'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 48px;
}
.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 32px;
}
.auth-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
  text-align: center;
}
.auth-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #606770;
}
.link {
  color: #1877f2;
  font-weight: 600;
}
.link:hover {
  text-decoration: underline;
}
</style>
