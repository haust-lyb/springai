<script setup>
import { ArrowLeft, ArrowRight, DataAnalysis, EditPen, MoreFilled } from '@element-plus/icons-vue'
import ProviderLogo from './ProviderLogo.vue'

defineProps({
  models: {
    type: Array,
    required: true
  }
})
</script>

<template>
  <section class="model-table-card">
    <h2>模型列表</h2>

    <div class="model-table-wrap">
      <table class="model-table">
        <thead>
          <tr>
            <th>模型名称</th>
            <th>所属平台</th>
            <th>模型 ID</th>
            <th>状态</th>
            <th>上下文长度</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="model in models" :key="model.id">
            <td>
              <div class="model-name-cell">
                <span>{{ model.name }}</span>
                <el-tag v-if="model.default" class="default-tag" size="small" type="primary" effect="light" round>默认</el-tag>
              </div>
            </td>
            <td>
              <div class="provider-cell">
                <ProviderLogo :logo="model.providerLogo" :color="model.providerColor" size="small" />
                <span>{{ model.provider }}</span>
              </div>
            </td>
            <td class="model-id-cell">{{ model.modelId }}</td>
            <td>
              <span class="status-pill">
                <span class="status-dot" />
                {{ model.status }}
              </span>
            </td>
            <td class="context-cell">{{ model.context }}</td>
            <td>
              <div class="action-group">
                <el-button :icon="EditPen" class="icon-action" aria-label="编辑" />
                <el-button :icon="DataAnalysis" class="icon-action" aria-label="统计" />
                <el-button :icon="MoreFilled" class="icon-action" aria-label="更多" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <footer class="table-footer">
      <span class="total-count">共 12 条</span>

      <div class="pagination-area">
        <el-button :icon="ArrowLeft" class="pager-button" disabled />
        <el-button type="primary" class="pager-number">1</el-button>
        <el-button class="pager-number">2</el-button>
        <el-button :icon="ArrowRight" class="pager-button" />
        <el-select model-value="10" class="page-size-select" size="default">
          <el-option label="10 条/页" value="10" />
          <el-option label="20 条/页" value="20" />
        </el-select>
      </div>

      <div class="jump-area">
        <span>跳至</span>
        <el-input model-value="1" class="jump-input" />
        <span>页</span>
      </div>
    </footer>
  </section>
</template>

<style scoped>
.model-table-card {
  min-width: 0;
  padding: 18px 22px 24px;
  border: 1px solid var(--model-border, #e4eaf3);
  border-radius: 12px;
  background: var(--model-card, #fff);
  box-shadow: var(--model-shadow, 0 16px 50px rgba(35, 74, 122, 0.08));
}

.model-table-card h2 {
  margin: 0 0 20px;
  font-size: 18px;
  font-weight: 800;
  color: #101828;
}

.model-table-wrap {
  overflow-x: auto;
  border: 1px solid #e1e8f2;
  border-radius: 9px;
}

.model-table {
  width: 100%;
  min-width: 840px;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 14px;
}

.model-table th,
.model-table td {
  height: 58px;
  padding: 0 18px;
  border-bottom: 1px solid #edf1f6;
  text-align: left;
  vertical-align: middle;
  white-space: nowrap;
}

.model-table th {
  height: 54px;
  color: #52617a;
  font-size: 13px;
  font-weight: 800;
  background: linear-gradient(180deg, #fbfcff 0%, #f7f9fd 100%);
}

.model-table tbody tr:last-child td {
  border-bottom: 0;
}

.model-table tbody tr {
  transition: background 0.18s ease;
}

.model-table tbody tr:hover {
  background: #f8fbff;
}

.model-name-cell,
.provider-cell,
.status-pill,
.action-group,
.table-footer,
.pagination-area,
.jump-area {
  display: flex;
  align-items: center;
}

.model-name-cell {
  gap: 8px;
  color: #101828;
  font-weight: 700;
}

.default-tag {
  min-width: 42px;
  justify-content: center;
}

.provider-cell {
  gap: 8px;
  color: #40506b;
  font-weight: 600;
}

.model-id-cell,
.context-cell {
  color: #40506b;
  font-weight: 600;
}

.status-pill {
  gap: 8px;
  color: #344054;
  font-weight: 600;
}

.status-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #34c759;
  box-shadow: 0 0 0 3px rgba(52, 199, 89, 0.12);
}

.action-group {
  gap: 8px;
}

.action-group :deep(.el-button + .el-button) {
  margin-left: 0;
}

.icon-action {
  width: 32px;
  height: 32px;
  padding: 0;
  border-radius: 8px;
  color: #465875;
  border-color: #e3eaf3;
  background: #fff;
}

.icon-action:hover {
  color: #1677ff;
  border-color: #bfdbff;
  background: #f5f9ff;
}

.table-footer {
  justify-content: space-between;
  gap: 16px;
  padding-top: 26px;
  color: #40506b;
  font-size: 14px;
}

.total-count {
  font-weight: 600;
}

.pagination-area {
  gap: 8px;
}

.pagination-area :deep(.el-button + .el-button) {
  margin-left: 0;
}

.pager-button,
.pager-number {
  width: 34px;
  height: 34px;
  padding: 0;
  border-radius: 7px;
}

.pager-number {
  font-weight: 700;
}

.page-size-select {
  width: 112px;
  margin-left: 18px;
}

.page-size-select :deep(.el-select__wrapper),
.jump-input :deep(.el-input__wrapper) {
  min-height: 34px;
  border-radius: 7px;
  box-shadow: 0 0 0 1px #dde5f0 inset;
}

.jump-area {
  gap: 8px;
  color: #40506b;
  font-weight: 600;
}

.jump-input {
  width: 70px;
}

.jump-input :deep(.el-input__inner) {
  text-align: center;
}

@media (max-width: 820px) {
  .model-table-card {
    padding: 16px;
  }

  .table-footer {
    align-items: flex-start;
    flex-direction: column;
  }

  .page-size-select {
    margin-left: 0;
  }

  .pagination-area {
    flex-wrap: wrap;
  }
}
</style>
