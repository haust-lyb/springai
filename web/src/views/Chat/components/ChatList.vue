<script setup>
import { nextTick, onMounted, ref, watch } from 'vue'

const props = defineProps({
  messages: {
    type: Array,
    default: () => []
  }
})

const messageAreaRef = ref(null)

const scrollToBottom = async () => {
  await nextTick()
  if (!messageAreaRef.value) return
  messageAreaRef.value.scrollTop = messageAreaRef.value.scrollHeight
}

onMounted(scrollToBottom)
watch(() => props.messages.length, scrollToBottom)
</script>

<template>
  <div ref="messageAreaRef" class="message-area">
    <div class="message-container">
      <div
        v-for="(message, index) in messages"
        :key="`${message.role}-${index}`"
        class="message-row"
        :class="{ 'is-user-message': message.role === 'user' }"
      >
        <div class="message-avatar">{{ message.role === 'user' ? '你' : 'AI' }}</div>
        <div class="message">
          {{ message.content }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.message-area {
  width: 100%;
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  padding: 20px;
  background: linear-gradient(180deg, #f8fbff 0%, #f4f7fb 100%);
}

.message-container {
  max-width: 760px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.message-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  max-width: min(100%, 720px);
}

.message-row.is-user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
  max-width: min(88%, 560px);
}

.message-avatar {
  width: 34px;
  height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 50%;
  color: #fff;
  background: #1677ff;
  font-size: 12px;
  font-weight: 850;
}

.is-user-message .message-avatar {
  color: #1677ff;
  background: #dcecff;
}

.message {
  min-width: 0;
  padding: 16px 18px;
  border: 1px solid #dfe7f2;
  border-radius: 10px;
  color: #24324a;
  background: #fff;
  box-shadow: 0 10px 26px rgba(35, 74, 122, 0.05);
  font-size: 14px;
  line-height: 1.8;
  white-space: pre-wrap;
  overflow-wrap: anywhere;
}

.is-user-message .message {
  color: #101828;
  border-color: #bfdbff;
  background: linear-gradient(135deg, #eaf4ff 0%, #dbeaff 100%);
}

@media (max-width: 720px) {
  .message-area {
    padding: 14px;
  }

  .message-row,
  .message-row.is-user-message {
    max-width: 100%;
  }
}
</style>
