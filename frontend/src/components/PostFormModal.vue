<template>
  <!-- 遮罩層 -->
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-box card">
      <div class="modal-header">
        <h3>{{ isEdit ? '編輯發文' : '建立新發文' }}</h3>
        <button class="close-btn" @click="$emit('close')">✕</button>
      </div>

      <form @submit.prevent="handleSave">
        <div class="form-group">
          <label>內容</label>
          <textarea
            v-model="form.content"
            placeholder="你想說什麼？"
            rows="5"
            maxlength="5000"
            required
          ></textarea>
        </div>

        <div class="form-group">
          <label>圖片網址（選填）</label>
          <input v-model="form.image" type="text" placeholder="https://..." maxlength="500" />
        </div>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="$emit('close')">取消</button>
          <button type="submit" class="btn btn-primary" :disabled="loading">
            {{ loading ? '儲存中...' : (isEdit ? '儲存變更' : '發布') }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { postApi } from '@/api'

const props = defineProps({
  post: { type: Object, default: null }
})
const emit = defineEmits(['close', 'saved'])

const isEdit = computed(() => !!props.post)

const form = reactive({
  content: props.post?.content ?? '',
  image:   props.post?.image   ?? ''
})

const errorMsg = ref('')
const loading  = ref(false)

async function handleSave() {
  errorMsg.value = ''
  loading.value  = true
  try {
    const payload = {
      content: form.content,
      image:   form.image || null
    }
    if (isEdit.value) {
      await postApi.update(props.post.postId, payload)
    } else {
      await postApi.create(payload)
    }
    emit('saved')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || '操作失敗，請稍後再試'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
}
.modal-box {
  width: 100%;
  max-width: 500px;
  padding: 24px;
  animation: fadeIn 0.15s ease;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-12px); }
  to   { opacity: 1; transform: translateY(0);     }
}
.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.modal-header h3 {
  font-size: 18px;
  font-weight: 700;
}
.close-btn {
  background: none;
  font-size: 18px;
  color: #65676b;
  padding: 4px;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.close-btn:hover {
  background: #f0f2f5;
}
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 16px;
}
</style>
