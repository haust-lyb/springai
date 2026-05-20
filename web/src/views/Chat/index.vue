<script setup>
import AssistItem from '@/views/Chat/components/AssistItem.vue'
import { onMounted, ref, computed } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import chatapi from '@/api/chat.js'
import ChatAssistHeader from '@/views/Chat/components/ChatAssistHeader.vue'
import TopicList from '@/views/Chat/components/TopicList.vue'
import AssistConfig from '@/views/Chat/components/AssistConfig.vue'

const assist_items = ref([])
const searchKeyword = ref('')
const configDrawerVisible = ref(false)

const openConfigDrawer = () => {
  configDrawerVisible.value = true
}

const closeConfigDrawer = () => {
  configDrawerVisible.value = false
}

const filteredAssistants = computed(() => {
  if (!searchKeyword.value) {
    return assist_items.value
  }
  const keyword = searchKeyword.value.toLowerCase()
  return assist_items.value.filter(item =>
    item.name.toLowerCase().includes(keyword) ||
    (item.desc && item.desc.toLowerCase().includes(keyword))
  )
})

const loadAssistants = async () => {
  try {
    const res = await chatapi.getAssistants()
    if (res.code === 200 && res.data?.assistants) {
      assist_items.value = res.data.assistants.map((item, index) => ({
        id: item.id,
        name: item.name,
        desc: item.desc || '',
        isSelected: index === 0
      }))
    }
  } catch (error) {
    console.error('加载助手列表失败:', error)
  }
}

const assistClick = (id) => {
  assist_items.value.forEach((item) => {
    item.isSelected = item.id === id
  })
}

onMounted(() => {
  loadAssistants()
})
</script>

<template>
  <main class="assistant-page">
    <section class="assistant-list-panel">
      <div class="section-heading">
        <div>
          <h2>助手列表</h2>
          <p>选择一个助手并管理会话能力</p>
        </div>
      </div>

      <el-input
        v-model="searchKeyword"
        class="assistant-search"
        placeholder="搜索助手"
        :prefix-icon="Search"
        clearable
      />

      <div class="assistant-list">
        <AssistItem
          v-for="item in filteredAssistants"
          :key="item.id"
          :id="item.id"
          :name="item.name"
          :isSelected="item.isSelected"
          :desc="item.desc"
          @click="assistClick"
        />
      </div>

      <button class="add-assist" type="button">
        <el-icon><Plus /></el-icon>
        <span>新建助手</span>
      </button>
    </section>

    <section class="chat-workspace-panel">
      <ChatAssistHeader
        title="通用助手"
        desc="通用对话助手，答疑解惑"
        @open-config="openConfigDrawer"
      />
      <TopicList />
    </section>
  </main>

  <el-drawer
    v-model="configDrawerVisible"
    class="assistant-config-drawer"
    direction="rtl"
    size="min(420px, 92vw)"
    :with-header="false"
    destroy-on-close
    append-to-body
  >
    <AssistConfig :close-handler="closeConfigDrawer" />
  </el-drawer>
</template>

<style scoped>
.assistant-page {
  --chat-primary: #1677ff;
  --chat-text: #101828;
  --chat-muted: #667085;
  --chat-border: #e5eaf2;
  --chat-soft: #f7faff;

  height: 100vh;
  overflow: auto;
  display: grid;
  gap: 18px;
  grid-template-columns: 320px minmax(0, 1fr);
  padding: 28px 36px 34px;
  color: var(--chat-text);
  background:
    radial-gradient(circle at 18% 10%, rgba(22, 119, 255, 0.08), transparent 28%),
    radial-gradient(circle at 88% 0%, rgba(91, 141, 239, 0.1), transparent 30%),
    linear-gradient(135deg, #f9fbff 0%, #f4f7fb 48%, #ffffff 100%);
}

.assistant-list-panel,
.chat-workspace-panel {
  min-width: 0;
  min-height: 0;
  border: 1px solid var(--chat-border);
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 18px 55px rgba(35, 74, 122, 0.08);
}

.assistant-list-panel {
  display: flex;
  flex-direction: column;
  padding: 24px 18px;
}

.chat-workspace-panel {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background:
    linear-gradient(180deg, #ffffff 0%, #fbfdff 46%, #f8fbff 100%);
}

.section-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 18px;
}

.section-heading h2 {
  margin: 0 0 8px;
  color: #101828;
  font-size: 20px;
  font-weight: 850;
  letter-spacing: -0.02em;
  white-space: nowrap;
}

.section-heading p {
  margin: 0;
  color: #667085;
  font-size: 13px;
  line-height: 1.5;
}

.assistant-search {
  margin-bottom: 18px;
}

.assistant-search :deep(.el-input__wrapper) {
  min-height: 42px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 0 0 1px #dbe3ef inset;
}

.assistant-list {
  flex: 1;
  min-height: 0;
  overflow: auto;
  display: grid;
  align-content: start;
  gap: 12px;
  padding-right: 2px;
}

.add-assist {
  width: 100%;
  min-height: 42px;
  margin-top: 18px;
  border: 0;
  border-radius: 10px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #667085;
  background: transparent;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: color 0.2s ease, background 0.2s ease;
}

.add-assist:hover {
  color: #1677ff;
  background: #f3f8ff;
}

@media (max-width: 1540px) {
  .assistant-page {
    grid-template-columns: 300px minmax(0, 1fr);
  }
}

@media (max-width: 1400px) {
  .assistant-page {
    padding: 22px;
    grid-template-columns: 292px minmax(0, 1fr);
  }
}

@media (max-width: 960px) {
  .assistant-page {
    height: auto;
    min-height: 100vh;
    grid-template-columns: 1fr;
  }

  .assistant-list-panel {
    min-height: 360px;
  }

  .chat-workspace-panel {
    min-height: 720px;
  }

}

@media (max-width: 640px) {
  .assistant-page {
    padding: 16px;
  }

  .assistant-list-panel {
    padding: 18px 14px;
  }
}

:global(.assistant-config-drawer .el-drawer__body) {
  padding: 0;
}

:global(.assistant-config-drawer.el-drawer) {
  border-radius: 14px 0 0 14px;
  overflow: hidden;
}
</style>
