<script setup>

import AssistItem from "@/views/Chat/components/AssistItem.vue";
import {ref} from "vue";
import {Search,Plus} from "@element-plus/icons-vue";

const assist_items = ref([]);
assist_items.value.push({
  id: 1,
  name: "通用助手",
  desc: "通过对话助手，答疑解惑",
  isSelected: true
})
assist_items.value.push({
  id: 2,
  name: "编程助手",
  desc: "编程相关问题解答，代码优化"
})
assist_items.value.push({
  id: 3,
  name: "写作助手",
  desc: "文章创作、润色、写作建议"
})

const assistClick = (id) => {
  console.log(id)
  assist_items.value.forEach((item) => {
    if (item.id === id) {
      item.isSelected = true
    } else {
      item.isSelected = false
    }
  })
}
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
            style=""
            size="mini"
            placeholder="搜索助手"
            :prefix-icon="Search"
        />
      </div>

      <div class="assists">
        <AssistItem @click="assistClick" v-for="item in assist_items" :key="item.id" :id="item.id" :name="item.name" :isSelected="item.isSelected" :desc="item.desc"></AssistItem>
      </div>

      <div class="add-assist">
        <el-icon>
          <Plus/>
        </el-icon>
        <div class="add-assist-text">新建助手</div>
      </div>
    </div>

    <div class="topics-container">
      头部标题和相关操作
    </div>

    <div class="assist-config-container">
      助手配置区域
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
}

.assist-config-container{
  height: 100vh;
  border-left: 1px solid rgba(191, 188, 188, 0.77);
  flex: 0 0 240px;
}


</style>
