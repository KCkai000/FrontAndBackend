<template>
  <div>
    <!-- 登入使用者才顯示發文區塊 -->
    <div v-if="authStore.isLoggedIn" class="card new-post-box">
      <div class="new-post-header">
        <AvatarIcon :name="authStore.userName" />
        <button class="compose-btn" @click="showPostForm = true">今天有什麼新鮮事？</button>
      </div>
    </div>

    <!-- 發文表單 Modal -->
    <PostFormModal
      v-if="showPostForm"
      @close="showPostForm = false"
      @saved="onPostSaved"
    />

    <!-- 編輯表單 Modal -->
    <PostFormModal
      v-if="editingPost"
      :post="editingPost"
      @close="editingPost = null"
      @saved="onPostUpdated"
    />

    <!-- 帖子列表 -->
    <div v-if="loading" class="loading">載入中...</div>
    <div v-else-if="posts.length === 0" class="loading">目前還沒有任何發文</div>
    <PostCard
      v-else
      v-for="post in posts"
      :key="post.postId"
      :post="post"
      @edit="editingPost = $event"
      @deleted="loadPosts"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { postApi } from '@/api'
import PostCard from '@/components/PostCard.vue'
import PostFormModal from '@/components/PostFormModal.vue'
import AvatarIcon from '@/components/AvatarIcon.vue'

const authStore = useAuthStore()
const posts        = ref([])
const loading      = ref(false)
const showPostForm = ref(false)
const editingPost  = ref(null)

async function loadPosts() {
  loading.value = true
  try {
    const res = await postApi.getAll()
    posts.value = res.data.data || []
  } catch {
    posts.value = []
  } finally {
    loading.value = false
  }
}

function onPostSaved() {
  showPostForm.value = false
  loadPosts()
}

function onPostUpdated() {
  editingPost.value = null
  loadPosts()
}

onMounted(loadPosts)
</script>

<style scoped>
.new-post-box {
  margin-bottom: 12px;
}
.new-post-header {
  display: flex;
  align-items: center;
  gap: 12px;
}
.compose-btn {
  flex: 1;
  background: #f0f2f5;
  border: 1px solid #ccd0d5;
  border-radius: 20px;
  padding: 10px 16px;
  font-size: 15px;
  color: #65676b;
  text-align: left;
  transition: background 0.15s;
}
.compose-btn:hover {
  background: #e4e6eb;
}
</style>
