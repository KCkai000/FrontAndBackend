<template>
  <div class="card post-card">
    <!-- Header -->
    <div class="post-header">
      <AvatarIcon :name="post.userName" :src="post.authorAvatar" />
      <div class="post-meta">
        <span class="post-author">{{ post.userName }}</span>
        <span class="post-time">{{ formatTime(post.createdAt) }}</span>
      </div>

      <!-- 操作選單（僅自己的帖子） -->
      <div v-if="authStore.isLoggedIn && authStore.userId === post.userId" class="post-actions">
        <button class="btn btn-secondary btn-sm" @click="$emit('edit', post)">編輯</button>
        <button class="btn btn-danger btn-sm" @click="confirmDelete">刪除</button>
      </div>
    </div>

    <!-- Content -->
    <p class="post-content">{{ post.content }}</p>

    <!-- Image -->
    <img v-if="post.image" :src="post.image" class="post-image" alt="post image" />

    <!-- Footer -->
    <div class="post-footer">
      <template v-if="showCommentsLink !== false">
        <RouterLink :to="{ name: 'PostDetail', params: { postId: post.postId } }" class="comment-link">
          💬 {{ post.commentCount ?? 0 }} 則留言
        </RouterLink>
      </template>
      <span v-else class="comment-count">💬 留言</span>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { postApi } from '@/api'
import AvatarIcon from './AvatarIcon.vue'

const props = defineProps({
  post: { type: Object, required: true },
  showCommentsLink: { type: Boolean, default: true }
})

const emit = defineEmits(['edit', 'deleted'])

const authStore = useAuthStore()

function formatTime(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const now = new Date()
  const diffMs = now - d
  const diffMin = Math.floor(diffMs / 60000)
  if (diffMin < 1)  return '剛剛'
  if (diffMin < 60) return `${diffMin} 分鐘前`
  const diffHr = Math.floor(diffMin / 60)
  if (diffHr < 24)  return `${diffHr} 小時前`
  return d.toLocaleDateString('zh-TW', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

async function confirmDelete() {
  if (!confirm('確定刪除此發文？')) return
  try {
    await postApi.delete(props.post.postId)
    emit('deleted', props.post.postId)
  } catch (err) {
    alert(err.response?.data?.message || '刪除失敗')
  }
}
</script>

<style scoped>
.post-card {
  transition: box-shadow 0.15s;
}
.post-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}
.post-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.post-author {
  font-weight: 700;
  font-size: 15px;
}
.post-time {
  font-size: 12px;
  color: #65676b;
}
.post-actions {
  display: flex;
  gap: 6px;
}
.post-content {
  font-size: 15px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
  margin-bottom: 12px;
}
.post-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 6px;
  margin-bottom: 12px;
}
.post-footer {
  border-top: 1px solid #e4e6eb;
  padding-top: 8px;
  margin-top: 4px;
}
.comment-link {
  font-size: 14px;
  color: #65676b;
  font-weight: 600;
}
.comment-link:hover {
  text-decoration: underline;
}
.comment-count {
  font-size: 14px;
  color: #65676b;
}
</style>
