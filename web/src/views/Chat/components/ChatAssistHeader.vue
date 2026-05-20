<script setup>
import { Edit, More, Share, Star } from '@element-plus/icons-vue'

defineProps({ title: String, desc: String })
const emit = defineEmits(['open-config'])
</script>

<template>
  <header class="header-container">
    <div class="header-left">
      <div class="assistant-avatar">AI</div>
      <div class="assistant-copy">
        <div class="title">
          <span>{{ title }}</span>
          <el-button class="edit-button" :icon="Edit" text circle />
        </div>
        <div class="desc">{{ desc }}</div>
      </div>
    </div>
    <div class="header-right">
      <el-button type="primary" :icon="Share" circle />
      <el-button :icon="Star" circle />
      <el-button :icon="More" circle @click="emit('open-config')" />
    </div>
  </header>
</template>

<style scoped>
.header-container {
  min-height: 92px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 18px;
  padding: 20px 24px;
  border-bottom: 1px solid #e5eaf2;
  background: rgba(255, 255, 255, 0.96);
}

.header-left {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.assistant-avatar {
  width: 34px;
  height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 8px;
  color: #fff;
  font-size: 12px;
  font-weight: 900;
  letter-spacing: 0.04em;
  background: linear-gradient(145deg, #2f83ff, #1267e8);
}

.assistant-copy {
  min-width: 0;
}

.title {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #101828;
  font-size: 20px;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.title span {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.edit-button {
  flex: 0 0 auto;
  color: #667085;
}

.desc {
  margin-top: 5px;
  color: #667085;
  font-size: 13px;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.header-right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  flex: 0 0 auto;
}

.header-right :deep(.el-button + .el-button) {
  margin-left: 0;
}

.header-right :deep(.el-button) {
  width: 38px;
  height: 38px;
  border-radius: 9px;
}

.header-right :deep(.el-button--primary) {
  border: 0;
  background: #1677ff;
  box-shadow: 0 10px 20px rgba(22, 119, 255, 0.18);
}

@media (max-width: 640px) {
  .header-container {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
