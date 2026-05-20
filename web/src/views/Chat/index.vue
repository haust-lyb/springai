<script setup>
import AssistItem from "@/views/Chat/components/AssistItem.vue";
import { onMounted, ref, computed } from "vue";
import { Search, Plus } from "@element-plus/icons-vue";
import chatapi from "@/api/chat.js"
import ChatAssistHeader from "@/views/Chat/components/ChatAssistHeader.vue";
import TopicList from "@/views/Chat/components/TopicList.vue";
import AssistConfig from "@/views/Chat/components/AssistConfig.vue";

const assist_items = ref([]);
const searchKeyword = ref('');

const filteredAssistants = computed(() => {
  if (!searchKeyword.value) {
    return assist_items.value;
  }
  const keyword = searchKeyword.value.toLowerCase();
  return assist_items.value.filter(item =>
    item.name.toLowerCase().includes(keyword) ||
    (item.desc && item.desc.toLowerCase().includes(keyword))
  );
});

const loadAssistants = async () => {
  try {
    const res = await chatapi.getAssistants();
    if (res.code === 200 && res.data?.assistants) {
      assist_items.value = res.data.assistants.map((item, index) => ({
        id: item.id,
        name: item.name,
        desc: item.desc || '',
        isSelected: index === 0
      }));
    }
  } catch (error) {
    console.error('加载助手列表失败:', error);
  }
};

const assistClick = (id) => {
  assist_items.value.forEach((item) => {
    if (item.id === id) {
      item.isSelected = true
    } else {
      item.isSelected = false
    }
  })
}

onMounted(() => {
  loadAssistants();
});
</script>

<template>
  <div class="main-container">
    <div class="assist-list-container">
      <div class="title">
        <div class="title-text">助手列表</div>
        <div class="title-icno"></div>
      </div>

      <div class="search">
        <el-input
          v-model="searchKeyword"
          size="small"
          placeholder="搜索助手"
          :prefix-icon="Search"
        />
      </div>

      <div class="assists">
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

      <div class="add-assist">
        <el-icon>
          <Plus/>
        </el-icon>
        <div class="add-assist-text">新建助手</div>
      </div>
    </div>

    <div class="topics-container">
      <div style="width: 100%;flex: 0 0 auto;">
        <ChatAssistHeader title="通用助手" desc="通用对话助手，答疑解惑"></ChatAssistHeader>
      </div>
      <div style="width: 100%;flex: 1 1 auto;">
        <TopicList></TopicList>
      </div>
    </div>

    <div class="assist-config-container">
      <AssistConfig></AssistConfig>
    </div>
  </div>
</template>

<style scoped>
.main-container {
  padding: 10px;
  background: white;
  height: 100vh;
  display: flex;
  gap: 10px;
}

.assist-list-container {
  flex:0 0 auto;
  max-width: 230px;
  .title{
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    .title-text{
      font-size: 16px;
      font-weight: bold;
    }
  }
  .search{
    margin-bottom: 10px;
  }

  .add-assist{
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 20px;
    padding: 20px;
    color: #8894ad;
    :hover{
      cursor: pointer;
      color: #667085;
    }
  }
}

.topics-container{
  height: 100vh;
  border-left: 1px solid rgba(191, 188, 188, 0.77);
  flex: 3 1 auto;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.assist-config-container{
  height: 100vh;
  border-left: 1px solid rgba(191, 188, 188, 0.77);
  flex: 0 0 300px;
}
</style>
