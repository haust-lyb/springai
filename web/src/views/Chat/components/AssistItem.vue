<script setup>
import { ChatRound } from '@element-plus/icons-vue'

const props = defineProps({
  id: { type: [String, Number], required: true },
  isSelected: Boolean,
  name: String,
  desc: String
})
const emit = defineEmits(['click'])

const handleClick = () => {
  emit('click', props.id)
}
</script>

<template>
  <button class="assist-item" :class="{ isSelected: props.isSelected }" type="button" @click="handleClick">
    <span class="icon">
      <el-icon :size="20">
        <ChatRound />
      </el-icon>
    </span>
    <span class="info">
      <span class="name">{{ name }}</span>
      <span class="desc">{{ desc || '无详细说明' }}</span>
    </span>
  </button>
</template>

<style scoped>
.assist-item {
  width: 100%;
  min-width: 0;
  min-height: 72px;
  padding: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  border: 1px solid transparent;
  border-radius: 12px;
  color: #344054;
  background: transparent;
  cursor: pointer;
  text-align: left;
  transition: background 0.2s ease, border-color 0.2s ease, box-shadow 0.2s ease;
}

.assist-item:hover {
  border-color: #dbe7f7;
  background: #f7fbff;
}

.assist-item.isSelected {
  border-color: #d7e8ff;
  background: linear-gradient(135deg, #eaf4ff 0%, #f5f9ff 100%);
  box-shadow: inset 0 0 0 1px rgba(22, 119, 255, 0.04);
}

.icon {
  width: 44px;
  height: 44px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 50%;
  color: #fff;
  background: linear-gradient(145deg, #2f83ff, #1267e8);
  box-shadow: 0 10px 22px rgba(22, 119, 255, 0.18);
}

.info {
  min-width: 0;
  flex: 1;
  display: grid;
  gap: 6px;
}

.name {
  color: #101828;
  font-size: 15px;
  font-weight: 800;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.isSelected .name {
  color: #1267e8;
}

.desc {
  color: #667085;
  font-size: 13px;
  line-height: 1.35;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
