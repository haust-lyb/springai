<script setup>

// do not use same name with ref
import {ref} from "vue";

const form = ref({
  name: '',
  desc: '',
  systemPrompt: '',
  modelId: '',
  temperature: 0.6,
  maxTokens: 256
})

const onSubmit = () => {
  console.log('submit!')
}

</script>

<template>
  <div class="assist-config-container">
    <div class="title">
      助手配置
    </div>
    <div class="config-container">
      <el-form :model="form" label-width="auto" label-position="top">
        <el-form-item label="助手名称">
          <el-input word-limit-position="inside" show-word-limit maxlength="20" v-model="form.name"/>
        </el-form-item>
        <el-form-item label="描述">
          <el-input word-limit-position="inside" show-word-limit maxlength="200" type="textarea" placeholder="请输入助手描述"  :autosize="{ minRows: 2, maxRows: 4 }" v-model="form.desc"/>
        </el-form-item>
        <el-form-item label="系统提示词">
          <el-input word-limit-position="inside" show-word-limit maxlength="2048" type="textarea" placeholder="请输入系统提示词" :autosize="{ minRows: 6, maxRows: 10 }"  v-model="form.systemPrompt"/>
        </el-form-item>

        <el-form-item label="模型">
          <el-select v-model="form.modelId" placeholder="请选择模型">
            <el-option label="模型一" value="shanghai"/>
            <el-option label="模型二" value="beijing"/>
          </el-select>
        </el-form-item>

        <el-form-item label="温度（Temperature）">
          <el-slider show-input v-model="form.temperature" :step="0.1" :min="0" :max="1"/>
        </el-form-item>

        <el-form-item label="最大回复长度">
          <el-slider show-input v-model="form.maxTokens" :step="1" :min="0" :max="4096"/>
        </el-form-item>

        <el-form-item>
          <div style="width:100%;display: flex; justify-content: flex-end; align-items: center;">
            <el-button>重置为默认值</el-button>
            <el-button type="primary" @click="onSubmit">保存配置</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.assist-config-container {
  padding: 10px;
  box-sizing: border-box;
  height: 100%;

  .title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 10px;
  }
}
</style>