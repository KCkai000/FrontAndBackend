<template>
  <NavBar />
  <main class="container" style="padding-top: 70px; padding-bottom: 32px;">
    <RouterView />
  </main>
</template>

<script setup>
import NavBar from '@/components/NavBar.vue'
</script>
