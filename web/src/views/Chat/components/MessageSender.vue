<script setup>
import {Plus, Promotion} from "@element-plus/icons-vue";
import {ref} from "vue";

const userPrompt = ref("")
</script>

<template>
  <!--    输入区域-->
  <div class="input-area">
    <div class="input-container">
      <el-input :autosize="{ minRows: 2, maxRows: 10 }" style="border: 0;" placeholder="有问题，尽管问" type="textarea" v-model="userPrompt"></el-input>
    </div>
    <div class="button-container">
      <!--        按钮区域-->
      <el-button :icon="Plus" circle type="info"/>
      <el-button :icon="Promotion" circle  type="primary"/>
    </div>
  </div>
</template>

<style scoped>
.input-area{
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 2px 2px 5px #dcdfe6;
  padding: 10px;
  width: 100%;
  flex: 0 0 auto;

  .button-container{
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>