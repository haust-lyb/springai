<script setup>
import { computed, onMounted, ref } from 'vue'
import ChatWithAI from '@/views/Chat/components/ChatWithAI.vue'
import SessionTabs from '@/views/Chat/components/SessionTabs.vue'

const activeName = ref('')
const topics = ref([])
const newTopicSeed = ref(1)

const currentTopic = computed(() => {
  return topics.value.find(topic => topic.name === activeName.value) || topics.value[0]
})

onMounted(() => {
  topics.value.push({ label: '新对话', name: '1', id: 1 })
  topics.value.push({ label: '人工智能的发展', name: '2', id: 2 })
  topics.value.push({ label: '推荐旅游景点', name: '3', id: 3 })
  topics.value.push({ label: '如何提高工作效率', name: '4', id: 4 })

  activeName.value = topics.value[0].name
})

const createTopic = () => {
  newTopicSeed.value += 1
  const topicId = Date.now().toString()
  const newTopic = {
    label: `新对话 ${newTopicSeed.value}`,
    name: topicId,
    id: topicId
  }
  topics.value.push(newTopic)
  activeName.value = newTopic.name
}

const closeTopic = (targetName) => {
  if (topics.value.length <= 1) return

  const closingIndex = topics.value.findIndex(topic => topic.name === targetName)
  topics.value = topics.value.filter(topic => topic.name !== targetName)

  if (activeName.value === targetName) {
    const nextTopic = topics.value[Math.max(0, closingIndex - 1)] || topics.value[0]
    activeName.value = nextTopic?.name || ''
  }
}
</script>

<template>
  <div class="app-container">
    <div class="topic-header">
      <div>
        <div class="topic-title">话题列表</div>
        <div class="topic-meta">{{ topics.length }} 个会话</div>
      </div>
    </div>

    <SessionTabs
      v-model:active-name="activeName"
      :topics="topics"
      @add="createTopic"
      @close="closeTopic"
    />

    <div class="chat-panel">
      <ChatWithAI v-if="currentTopic" :key="currentTopic.id" :chat-id="String(currentTopic.id)" />
    </div>
  </div>
</template>

<style scoped>
.app-container {
  flex: 1 1 auto;
  min-height: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 18px 24px 0;
}

.topic-header {
  flex: 0 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 14px;
}

.topic-title {
  color: #101828;
  font-size: 16px;
  font-weight: 850;
}

.topic-meta {
  margin-top: 4px;
  color: #98a2b3;
  font-size: 12px;
  font-weight: 700;
}

.chat-panel {
  flex: 1 1 0;
  min-height: 0;
  display: flex;
  overflow: hidden;
  margin-top: 10px;
  border-top: 1px solid #e8eef7;
}
</style>
