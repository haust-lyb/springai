<script setup>
import { ref } from 'vue'

const props = defineProps({
  closeHandler: {
    type: Function,
    default: null
  }
})

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
  props.closeHandler?.()
}
</script>

<template>
  <div class="assist-config-container">
    <div class="config-header">
      <h2>助手配置</h2>
      <div class="config-tabs">
        <button class="config-tab is-active" type="button">配置</button>
        <button class="config-tab" type="button">预览</button>
      </div>
    </div>

    <div class="config-container">
      <el-form :model="form" label-width="auto" label-position="top">
        <el-form-item label="助手名称">
          <el-input v-model="form.name" word-limit-position="inside" show-word-limit maxlength="20" placeholder="请输入助手名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.desc" word-limit-position="inside" show-word-limit maxlength="200" type="textarea" placeholder="请输入助手描述" :autosize="{ minRows: 2, maxRows: 4 }" />
        </el-form-item>
        <el-form-item label="系统提示词">
          <el-input v-model="form.systemPrompt" word-limit-position="inside" show-word-limit maxlength="2048" type="textarea" placeholder="请输入系统提示词" :autosize="{ minRows: 7, maxRows: 10 }" />
        </el-form-item>

        <el-form-item label="模型">
          <el-select v-model="form.modelId" placeholder="请选择模型">
            <el-option label="模型一" value="shanghai" />
            <el-option label="模型二" value="beijing" />
          </el-select>
        </el-form-item>

        <el-form-item label="温度（Temperature）">
          <el-slider v-model="form.temperature" show-input :step="0.1" :min="0" :max="1" />
        </el-form-item>

        <el-form-item label="最大回复长度">
          <el-slider v-model="form.maxTokens" show-input :step="1" :min="0" :max="4096" />
        </el-form-item>
      </el-form>
    </div>

    <div class="form-actions">
      <el-button @click="props.closeHandler?.()">取消</el-button>
      <el-button type="primary" @click="onSubmit">保存配置</el-button>
    </div>
  </div>
</template>

<style scoped>
.assist-config-container {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #fff;
}

.config-header {
  flex: 0 0 auto;
  padding: 24px 24px 0;
  border-bottom: 1px solid #e5eaf2;
}

.config-header h2 {
  margin: 0 0 20px;
  color: #101828;
  font-size: 20px;
  font-weight: 850;
  letter-spacing: -0.02em;
  white-space: nowrap;
}

.config-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0;
}

.config-tab {
  min-width: 0;
  height: 42px;
  border: 0;
  border-bottom: 3px solid transparent;
  color: #344054;
  background: transparent;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
}

.config-tab.is-active {
  color: #1677ff;
  border-bottom-color: #1677ff;
}

.config-container {
  flex: 1 1 auto;
  min-height: 0;
  overflow: auto;
  padding: 22px 24px;
}

.config-container :deep(.el-form-item) {
  margin-bottom: 22px;
}

.config-container :deep(.el-form-item__label) {
  color: #344054;
  font-size: 13px;
  font-weight: 800;
  padding-bottom: 8px;
  white-space: nowrap;
}

.config-container :deep(.el-input__wrapper),
.config-container :deep(.el-select__wrapper),
.config-container :deep(.el-textarea__inner) {
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 0 0 1px #dbe3ef inset;
}

.config-container :deep(.el-input__wrapper),
.config-container :deep(.el-select__wrapper) {
  min-height: 40px;
}

.config-container :deep(.el-input__wrapper:hover),
.config-container :deep(.el-select__wrapper:hover),
.config-container :deep(.el-textarea__inner:hover) {
  box-shadow: 0 0 0 1px #bfd0ea inset;
}

.config-container :deep(.el-slider__bar) {
  background: #1677ff;
}

.config-container :deep(.el-slider__button) {
  border-color: #1677ff;
}

.form-actions {
  flex: 0 0 auto;
  padding: 18px 24px 22px;
  border-top: 1px solid #e5eaf2;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  background: #fff;
}

.form-actions :deep(.el-button + .el-button) {
  margin-left: 0;
}

.form-actions :deep(.el-button) {
  min-width: 76px;
  border-radius: 8px;
  font-weight: 800;
}

.form-actions :deep(.el-button--primary) {
  border: 0;
  background: #1677ff;
  box-shadow: 0 10px 20px rgba(22, 119, 255, 0.16);
}
</style>
