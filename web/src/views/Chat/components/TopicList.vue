<script setup>
import {onMounted, ref} from 'vue'
import ChatWithAI from "@/views/Chat/components/ChatWithAI.vue";


const activeName = ref("")

const topics = ref([])

const currentTopic = ref(null)

onMounted(() => {
  topics.value.push({
    label: '新对话',
    name: '1',
    id: 1
  })
  topics.value.push({
    label: '人工智能的发展',
    name: '2',
    id: 2
  })
  topics.value.push({
    label: '推荐旅游景点',
    name: '3',
    id: 3
  })
  topics.value.push({
    label: '如何提高工作效率',
    name: '4',
    id: 4
  })

  currentTopic.value = topics.value[0]
  activeName.value = topics.value[0].name
})

const handleClick = (tab, event) => {
  console.log(tab, event)
}

const handleTabsEdit = (targetName, action) => {
  console.log(targetName, action)
  if (action === 'remove') {
    topics.value = topics.value.filter(topic => topic.name !== targetName)
  }
  if (action === 'add') {
    const newTopic = {
      label: 'New Topic',
      name: Date.now().toString(),
      id: Date.now().toString()
    }
    topics.value.push(newTopic)
    activeName.value = newTopic.name
  }
}
</script>

<template>
  <div class="app-container">
    <el-tabs
        v-model="activeName"
        editable
        type="card"
        class="chat-tabs"
        @tab-click="handleClick"
        @edit="handleTabsEdit"
    >
      <el-tab-pane style="height: calc( 100vh - 190px);overflow: auto;" v-for="item in topics" :key="item.name" :label="item.label" :name="item.name">
        <chat-with-a-i :chatId="item.id"></chat-with-a-i>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>

.app-container {
  padding: 20px;
  flex: 1 1 auto;
  height: 100%;

  .chat-tabs{
    height: 100%;
  }
}
</style>