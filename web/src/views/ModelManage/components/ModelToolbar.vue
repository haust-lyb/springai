<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'

const keyword = ref('')
const status = ref('all')

const statusOptions = [
  { label: '全部状态', value: 'all' },
  { label: '启用', value: 'enabled' },
  { label: '停用', value: 'disabled' }
]
</script>

<template>
  <div class="model-toolbar">
    <el-input
      v-model="keyword"
      :prefix-icon="Search"
      class="model-search"
      placeholder="搜索模型名称、平台或标签..."
      size="large"
      clearable
    />

    <el-select v-model="status" class="status-select" size="large">
      <el-option
        v-for="item in statusOptions"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
  </div>
</template>

<style scoped>
.model-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.model-search {
  width: min(420px, 100%);
}

.status-select {
  width: 172px;
}

.model-toolbar :deep(.el-input__wrapper),
.model-toolbar :deep(.el-select__wrapper) {
  min-height: 46px;
  border-radius: 8px;
  box-shadow: 0 0 0 1px #dde5f0 inset;
  background: rgba(255, 255, 255, 0.9);
}

.model-toolbar :deep(.el-input__wrapper:hover),
.model-toolbar :deep(.el-select__wrapper:hover) {
  box-shadow: 0 0 0 1px #bfd0ea inset;
}

@media (max-width: 640px) {
  .model-toolbar {
    align-items: stretch;
    flex-direction: column;
  }

  .model-search,
  .status-select {
    width: 100%;
  }
}
</style>
