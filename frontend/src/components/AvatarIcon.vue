<!-- 顯示使用者頭像或名稱首字縮寫 -->
<template>
  <div class="avatar" :style="{ background: bgColor }">
    <img v-if="src" :src="src" :alt="name" />
    <span v-else>{{ initial }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  name: { type: String, default: '?' },
  src:  { type: String, default: '' }
})

const COLORS = ['#1877f2','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22']

const initial = computed(() => (props.name || '?').charAt(0).toUpperCase())

const bgColor = computed(() => {
  let hash = 0
  for (const ch of (props.name || '')) hash = ch.charCodeAt(0) + hash * 31
  return COLORS[Math.abs(hash) % COLORS.length]
})
</script>
