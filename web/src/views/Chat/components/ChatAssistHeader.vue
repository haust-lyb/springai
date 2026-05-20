<script setup>
import {Edit, More, Share, Star} from "@element-plus/icons-vue";

  defineProps({ title: String, desc: String })
</script>

<template>
  <div class="header-container">
    <div class="header-left">
      <div class="title">
        <div>{{ title }}</div>
        <el-icon class="edit" size="14" color="black">
          <Edit />
        </el-icon>
      </div>
      <div class="desc">{{ desc }}</div>
    </div>
    <div class="header-right">
      <el-button type="primary" :icon="Share" circle />
      <el-button :icon="Star" circle />
      <el-button :icon="More" circle />
    </div>
  </div>
</template>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-content: center;
  padding: 10px;
  border-bottom: 1px solid #c6c4c4;

  .header-left{
    .title{
      display: flex;
      justify-content: flex-start;
      align-items: center;
      padding: 10px 0;
      font-size: 20px;
      font-weight: bold;
    }
    .edit{
      margin-left: 5px;
      cursor: pointer;
    }
    .desc{
      font-size: 12px;
      color: gray;
    }
  }
  .header-right{
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
}
</style>