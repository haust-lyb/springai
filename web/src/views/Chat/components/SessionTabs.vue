<script setup>
import { computed } from 'vue'
import { ChatLineRound, Close, Plus } from '@element-plus/icons-vue'

const props = defineProps({
  topics: {
    type: Array,
    default: () => []
  },
  activeName: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:activeName', 'add', 'close'])

const activeIndex = computed(() => {
  const index = props.topics.findIndex(topic => topic.name === props.activeName)
  return index >= 0 ? index : 0
})

const selectTopic = (name) => {
  emit('update:activeName', name)
}

const moveFocus = (offset) => {
  if (!props.topics.length) return
  const nextIndex = (activeIndex.value + offset + props.topics.length) % props.topics.length
  emit('update:activeName', props.topics[nextIndex].name)
}

const closeTopic = (event, topic) => {
  event.stopPropagation()
  emit('close', topic.name)
}
</script>

<template>
  <div class="session-tabs" role="tablist" aria-label="会话列表">
    <div class="tabs-scroll">
      <button
        v-for="topic in topics"
        :key="topic.name"
        type="button"
        class="session-tab"
        :class="{ active: topic.name === activeName }"
        role="tab"
        :aria-selected="topic.name === activeName"
        :title="topic.label"
        @click="selectTopic(topic.name)"
        @keydown.left.prevent="moveFocus(-1)"
        @keydown.right.prevent="moveFocus(1)"
      >
        <span class="tab-icon">
          <el-icon><ChatLineRound /></el-icon>
        </span>
        <span class="tab-label">{{ topic.label }}</span>
        <button
          v-if="topics.length > 1"
          type="button"
          class="tab-close"
          title="关闭会话"
          @click="closeTopic($event, topic)"
        >
          <el-icon><Close /></el-icon>
        </button>
      </button>
    </div>

    <button type="button" class="new-session" title="新建会话" @click="emit('add')">
      <el-icon><Plus /></el-icon>
    </button>
  </div>
</template>

<style scoped>
.session-tabs {
  min-width: 0;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 42px;
  align-items: center;
  gap: 10px;
}

.tabs-scroll {
  min-width: 0;
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding: 0 2px;
  scrollbar-width: thin;
}

.session-tab,
.new-session,
.tab-close {
  border: 0;
  font: inherit;
  cursor: pointer;
}

.session-tab {
  position: relative;
  min-width: 128px;
  max-width: 220px;
  height: 42px;
  padding: 0 10px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  flex: 0 0 auto;
  border: 1px solid #dfe7f2;
  border-radius: 9px;
  color: #475467;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 8px 20px rgba(37, 63, 95, 0.03);
  transition: color 0.18s ease, border-color 0.18s ease, background 0.18s ease, box-shadow 0.18s ease;
}

.session-tab:hover {
  color: #101828;
  border-color: #c9daf1;
  background: #fff;
}

.session-tab.active {
  color: #1267e8;
  border-color: #a9ceff;
  background: linear-gradient(180deg, #ffffff 0%, #f1f7ff 100%);
  box-shadow: 0 12px 26px rgba(22, 119, 255, 0.1);
}

.session-tab.active::after {
  position: absolute;
  left: 12px;
  right: 12px;
  bottom: -1px;
  height: 2px;
  border-radius: 999px;
  background: #1677ff;
  content: '';
}

.tab-icon {
  width: 24px;
  height: 24px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 7px;
  color: #7a8aa0;
  background: #f5f8fc;
}

.active .tab-icon {
  color: #fff;
  background: #1677ff;
}

.tab-label {
  min-width: 0;
  flex: 1;
  overflow: hidden;
  color: inherit;
  font-size: 14px;
  font-weight: 750;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tab-close {
  width: 22px;
  height: 22px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 6px;
  color: #98a2b3;
  background: transparent;
  opacity: 0;
  transition: color 0.18s ease, background 0.18s ease, opacity 0.18s ease;
}

.session-tab:hover .tab-close,
.session-tab.active .tab-close {
  opacity: 1;
}

.tab-close:hover {
  color: #d92d20;
  background: #fff1f0;
}

.new-session {
  width: 42px;
  height: 42px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dfe7f2;
  border-radius: 9px;
  color: #1677ff;
  background: #fff;
  box-shadow: 0 8px 20px rgba(37, 63, 95, 0.04);
  transition: color 0.18s ease, border-color 0.18s ease, background 0.18s ease, box-shadow 0.18s ease;
}

.new-session:hover {
  border-color: #a9ceff;
  background: #f3f8ff;
  box-shadow: 0 12px 24px rgba(22, 119, 255, 0.1);
}

@media (max-width: 720px) {
  .session-tab {
    min-width: 118px;
    max-width: 176px;
  }
}
</style>
