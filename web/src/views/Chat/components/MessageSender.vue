<script setup>
import { computed, ref } from 'vue'
import {
  ChatLineRound,
  Microphone,
  Paperclip,
  Picture,
  Promotion
} from '@element-plus/icons-vue'

const emit = defineEmits(['send'])
const userPrompt = ref('')
const maxLength = 4000

const trimmedPrompt = computed(() => userPrompt.value.trim())
const canSend = computed(() => trimmedPrompt.value.length > 0)
const inputLength = computed(() => userPrompt.value.length)

const submitPrompt = () => {
  if (!canSend.value) return
  emit('send', trimmedPrompt.value)
  userPrompt.value = ''
}
</script>

<template>
  <form class="message-composer" @submit.prevent="submitPrompt">
    <div class="composer-main">
      <el-input
        v-model="userPrompt"
        :autosize="{ minRows: 3, maxRows: 8 }"
        :maxlength="maxLength"
        placeholder="输入消息，向助手提问"
        type="textarea"
        @keydown.enter.exact.prevent="submitPrompt"
      />
    </div>

    <div class="composer-toolbar">
      <div class="composer-actions" aria-label="消息工具">
        <el-tooltip content="上传附件" placement="top">
          <button class="tool-button" type="button" aria-label="上传附件">
            <el-icon><Paperclip /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="添加图片" placement="top">
          <button class="tool-button" type="button" aria-label="添加图片">
            <el-icon><Picture /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="语音输入" placement="top">
          <button class="tool-button" type="button" aria-label="语音输入">
            <el-icon><Microphone /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="常用提示词" placement="top">
          <button class="tool-button" type="button" aria-label="常用提示词">
            <el-icon><ChatLineRound /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <div class="composer-status">
        <span class="char-count" :class="{ warning: inputLength > maxLength * 0.9 }">
          {{ inputLength }}/{{ maxLength }}
        </span>
        <button class="send-button" type="submit" :disabled="!canSend" aria-label="发送消息">
          <el-icon><Promotion /></el-icon>
        </button>
      </div>
    </div>
  </form>
</template>

<style scoped>
.message-composer {
  flex: 0 0 auto;
  width: 100%;
  max-width: 820px;
  margin: 0 auto;
  padding: 14px 14px 12px;
  border: 1px solid #d8e3f2;
  border-radius: 12px;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(250, 253, 255, 0.98) 100%);
  box-shadow: 0 18px 42px rgba(31, 56, 88, 0.08);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.message-composer:focus-within {
  border-color: #9bc8ff;
  box-shadow: 0 18px 42px rgba(31, 56, 88, 0.08), 0 0 0 3px rgba(22, 119, 255, 0.1);
}

.composer-main :deep(.el-textarea__inner) {
  min-height: 76px !important;
  padding: 6px 2px;
  border: 0;
  color: #1d2939;
  background: transparent;
  box-shadow: none;
  font-size: 14px;
  line-height: 1.65;
  resize: none;
}

.composer-main :deep(.el-textarea__inner::placeholder) {
  color: #98a2b3;
}

.composer-main :deep(.el-textarea__inner:focus) {
  box-shadow: none;
}

.composer-toolbar {
  margin-top: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.composer-actions,
.composer-status {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.composer-status {
  flex: 0 0 auto;
  justify-content: flex-end;
}

.tool-button,
.send-button {
  border: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: color 0.18s ease, background 0.18s ease, box-shadow 0.18s ease, transform 0.18s ease;
}

.tool-button {
  width: 34px;
  height: 34px;
  border-radius: 9px;
  border: 0;
  color: #667085;
  background: #f5f8fc;
  font-size: 16px;
}

.tool-button:hover {
  color: #1677ff;
  background: #eaf4ff;
}

.char-count {
  color: #98a2b3;
  font-size: 12px;
  font-weight: 700;
}

.char-count.warning {
  color: #b54708;
}

.send-button {
  width: 40px;
  height: 40px;
  flex: 0 0 auto;
  border-radius: 10px;
  color: #fff;
  background: #1677ff;
  box-shadow: 0 10px 20px rgba(22, 119, 255, 0.18);
  font-size: 18px;
}

.send-button:hover:not(:disabled) {
  background: #0d63e5;
  box-shadow: 0 14px 26px rgba(22, 119, 255, 0.24);
  transform: translateY(-1px);
}

.send-button:disabled {
  cursor: not-allowed;
  color: #98a2b3;
  background: #eef2f7;
  box-shadow: none;
}

@media (max-width: 720px) {
  .message-composer {
    padding: 12px;
    border-radius: 10px;
  }

  .composer-toolbar {
    align-items: flex-start;
  }

  .composer-status {
    margin-left: auto;
  }
}
</style>
