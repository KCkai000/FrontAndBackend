<template>
  <nav class="navbar">
    <div class="container navbar-inner">
      <RouterLink to="/" class="brand">📢 社群媒體</RouterLink>

      <div class="nav-actions">
        <template v-if="authStore.isLoggedIn">
          <span class="user-name">{{ authStore.userName }}</span>
          <button class="btn btn-secondary btn-sm" @click="handleLogout">登出</button>
        </template>
        <template v-else>
          <RouterLink to="/login"    class="btn btn-secondary btn-sm">登入</RouterLink>
          <RouterLink to="/register" class="btn btn-primary btn-sm">註冊</RouterLink>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router    = useRouter()
const authStore = useAuthStore()

function handleLogout() {
  authStore.logout()
  router.push({ name: 'Login' })
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: #fff;
  border-bottom: 1px solid #e4e6eb;
  height: 56px;
  display: flex;
  align-items: center;
}
.navbar-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.brand {
  font-size: 18px;
  font-weight: 700;
  color: #1877f2;
}
.nav-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}
.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #444;
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
