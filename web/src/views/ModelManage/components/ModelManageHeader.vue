<script setup>
import { Document, Plus } from '@element-plus/icons-vue'
</script>

<template>
  <header class="model-header">
    <div>
      <h1>模型管理</h1>
      <p>管理和配置接入的 AI 模型服务，支持多平台、多模型的统一管理与调用</p>
    </div>

    <div class="model-header-actions">
      <el-button :icon="Document" class="doc-button">使用文档</el-button>
      <el-button type="primary" :icon="Plus" class="add-button">添加模型</el-button>
    </div>
  </header>
</template>

<style scoped>
.model-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 18px;
  margin-bottom: 34px;
}

.model-header h1 {
  margin: 0 0 8px;
  font-size: 22px;
  line-height: 1.2;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: #101828;
}

.model-header p {
  margin: 0;
  color: #58667f;
  font-size: 14px;
  line-height: 1.7;
}

.model-header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.doc-button,
.add-button {
  height: 38px;
  border-radius: 8px;
  padding: 0 16px;
  font-weight: 600;
}

.doc-button {
  border-color: #dbe3ef;
  color: #1f2a44;
  background: rgba(255, 255, 255, 0.82);
}

.add-button {
  border: none;
  background: linear-gradient(135deg, #1677ff 0%, #0f63e6 100%);
  box-shadow: 0 10px 22px rgba(22, 119, 255, 0.22);
}

@media (max-width: 760px) {
  .model-header {
    flex-direction: column;
    margin-bottom: 22px;
  }

  .model-header-actions {
    width: 100%;
  }

  .model-header-actions :deep(.el-button) {
    flex: 1;
  }
}
</style>
