<template>
  <div class="card comment-section">
    <h4 class="section-title">留言</h4>

    <!-- 新增留言表單 -->
    <div v-if="authStore.isLoggedIn" class="new-comment">
      <AvatarIcon :name="authStore.userName" />
      <div class="comment-input-wrap">
        <textarea
          v-model="newContent"
          placeholder="留下您的看法..."
          rows="2"
          maxlength="2000"
          @keydown.ctrl.enter="submitComment"
        ></textarea>
        <div v-if="commentError" class="error-msg">{{ commentError }}</div>
        <div class="comment-actions">
          <span class="tip">Ctrl + Enter 快速送出</span>
          <button class="btn btn-primary btn-sm" :disabled="submitting || !newContent.trim()" @click="submitComment">
            {{ submitting ? '送出中...' : '送出留言' }}
          </button>
        </div>
      </div>
    </div>
    <p v-else class="login-hint">
      <RouterLink to="/login" class="link">登入</RouterLink> 才能留言
    </p>

    <!-- 留言列表 -->
    <div v-if="loading" class="loading" style="padding:16px 0">載入留言中...</div>
    <div v-else-if="comments.length === 0" class="empty-comments">目前還沒有留言，快來第一個留言！</div>
    <div v-else class="comment-list">
      <div v-for="c in comments" :key="c.commentId" class="comment-item">
        <AvatarIcon :name="c.userName" :src="c.authorAvatar" style="width:32px;height:32px;font-size:13px" />
        <div class="comment-body">
          <div class="comment-header">
            <span class="comment-author">{{ c.userName }}</span>
            <span class="comment-time">{{ formatTime(c.createdAt) }}</span>
          </div>
          <p class="comment-content">{{ c.content }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { commentApi } from '@/api'
import AvatarIcon from './AvatarIcon.vue'

const props = defineProps({
  postId: { type: [String, Number], required: true }
})

const authStore  = useAuthStore()
const comments   = ref([])
const loading    = ref(false)
const newContent = ref('')
const submitting = ref(false)
const commentError = ref('')

async function loadComments() {
  loading.value = true
  try {
    const res = await commentApi.getByPost(props.postId)
    comments.value = res.data.data || []
  } catch {
    comments.value = []
  } finally {
    loading.value = false
  }
}

async function submitComment() {
  if (!newContent.value.trim()) return
  commentError.value = ''
  submitting.value   = true
  try {
    await commentApi.create(props.postId, { content: newContent.value.trim() })
    newContent.value = ''
    await loadComments()
  } catch (err) {
    commentError.value = err.response?.data?.message || '留言失敗，請稍後再試'
  } finally {
    submitting.value = false
  }
}

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

onMounted(loadComments)
</script>

<style scoped>
.comment-section {
  margin-top: 8px;
}
.section-title {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 16px;
  color: #1c1e21;
}
.new-comment {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e6eb;
}
.comment-input-wrap {
  flex: 1;
}
.comment-input-wrap textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ccd0d5;
  border-radius: 6px;
  font-size: 14px;
  resize: none;
  background: #f0f2f5;
  transition: border-color 0.15s, background 0.15s;
}
.comment-input-wrap textarea:focus {
  border-color: #1877f2;
  background: #fff;
}
.comment-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}
.tip {
  font-size: 12px;
  color: #a0a3a8;
}
.login-hint {
  font-size: 14px;
  color: #65676b;
  margin-bottom: 16px;
}
.link {
  color: #1877f2;
  font-weight: 600;
}
.link:hover { text-decoration: underline; }
.empty-comments {
  text-align: center;
  color: #65676b;
  font-size: 14px;
  padding: 24px 0;
}
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.comment-item {
  display: flex;
  gap: 10px;
}
.comment-body {
  flex: 1;
  background: #f0f2f5;
  border-radius: 12px;
  padding: 10px 14px;
}
.comment-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 4px;
}
.comment-author {
  font-weight: 700;
  font-size: 13px;
}
.comment-time {
  font-size: 11px;
  color: #65676b;
}
.comment-content {
  font-size: 14px;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
