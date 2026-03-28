<template>
  <div>
    <div v-if="loading" class="loading">載入中...</div>

    <template v-else-if="post">
      <!-- 帖子詳情 -->
      <PostCard :post="post" :show-comments-link="false" @edit="editingPost = $event" @deleted="router.push({ name: 'Home' })" />

      <!-- 編輯表單 -->
      <PostFormModal
        v-if="editingPost"
        :post="editingPost"
        @close="editingPost = null"
        @saved="onPostUpdated"
      />

      <!-- 留言區 -->
      <CommentSection :post-id="post.postId" />
    </template>

    <div v-else class="loading">找不到此發文</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { postApi } from '@/api'
import PostCard from '@/components/PostCard.vue'
import PostFormModal from '@/components/PostFormModal.vue'
import CommentSection from '@/components/CommentSection.vue'

const props  = defineProps({ postId: { type: String, required: true } })
const router = useRouter()

const post        = ref(null)
const loading     = ref(false)
const editingPost = ref(null)

async function loadPost() {
  loading.value = true
  try {
    const res = await postApi.getById(props.postId)
    post.value = res.data.data
  } catch {
    post.value = null
  } finally {
    loading.value = false
  }
}

function onPostUpdated() {
  editingPost.value = null
  loadPost()
}

onMounted(loadPost)
</script>
