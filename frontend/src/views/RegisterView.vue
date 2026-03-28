<template>
  <div class="auth-page">
    <div class="auth-card card">
      <h2 class="auth-title">建立帳號</h2>

      <form @submit.prevent="handleRegister">
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
          <label>使用者名稱</label>
          <input
            v-model="form.userName"
            type="text"
            placeholder="請輸入您的名稱"
            maxlength="100"
            required
          />
        </div>

        <div class="form-group">
          <label>電子郵件（選填）</label>
          <input
            v-model="form.email"
            type="text"
            placeholder="example@email.com"
            maxlength="255"
          />
        </div>

        <div class="form-group">
          <label>密碼</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="至少 8 個字元"
            minlength="8"
            maxlength="64"
            required
          />
        </div>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
        <div v-if="successMsg" class="success-msg">{{ successMsg }}</div>

        <button type="submit" class="btn btn-primary" style="width:100%;margin-top:8px" :disabled="loading">
          {{ loading ? '註冊中...' : '建立帳號' }}
        </button>
      </form>

      <p class="auth-footer">
        已有帳號？
        <RouterLink to="/login" class="link">立即登入</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@/api'

const router = useRouter()

const form = reactive({ phone: '', userName: '', email: '', password: '' })
const errorMsg   = ref('')
const successMsg = ref('')
const loading    = ref(false)

async function handleRegister() {
  errorMsg.value   = ''
  successMsg.value = ''
  loading.value    = true
  try {
    await authApi.register({
      phone:    form.phone,
      userName: form.userName,
      email:    form.email || undefined,
      password: form.password
    })
    successMsg.value = '註冊成功！即將跳轉至登入頁...'
    setTimeout(() => router.push({ name: 'Login' }), 1500)
  } catch (err) {
    errorMsg.value = err.response?.data?.message || '註冊失敗，請稍後再試'
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
.success-msg {
  color: #2e7d32;
  font-size: 13px;
  margin-top: 8px;
}
</style>
