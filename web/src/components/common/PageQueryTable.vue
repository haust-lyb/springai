<script setup>
import { Refresh, Search } from '@element-plus/icons-vue'

const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  total: {
    type: Number,
    default: 0
  },
  pageNo: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 10
  },
  pageSizes: {
    type: Array,
    default: () => [10, 20, 50, 100]
  },
  tableMinHeight: {
    type: String,
    default: '380px'
  },
  tableHeight: {
    type: String,
    default: ''
  },
  showReset: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits([
  'search',
  'reset',
  'page-change',
  'update:pageNo',
  'update:pageSize'
])

const runSearch = () => {
  emit('update:pageNo', 1)
  emit('search')
}

const runReset = () => {
  emit('update:pageNo', 1)
  emit('reset')
}

const changePage = (pageNo) => {
  emit('update:pageNo', pageNo)
  emit('page-change', { pageNo, pageSize: props.pageSize })
}

const changePageSize = (pageSize) => {
  emit('update:pageNo', 1)
  emit('update:pageSize', pageSize)
  emit('page-change', { pageNo: 1, pageSize })
}
</script>

<template>
  <section class="page-query-table">
    <div class="query-bar">
      <div class="query-fields">
        <slot name="query" />
      </div>
      <div class="query-actions">
        <el-button type="primary" :icon="Search" :loading="loading" @click="runSearch">查询</el-button>
        <el-button v-if="showReset" :icon="Refresh" @click="runReset">重置</el-button>
        <slot name="actions" />
      </div>
    </div>

    <div class="table-wrap" :style="{ minHeight: tableMinHeight, height: tableHeight || undefined }">
      <slot />
    </div>

    <div class="pagination-bar">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :current-page="pageNo"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        @current-change="changePage"
        @size-change="changePageSize"
      />
    </div>
  </section>
</template>

<style scoped>
.page-query-table {
  display: grid;
  gap: 14px;
}

.query-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.92);
}

.query-fields,
.query-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.query-fields {
  flex: 1;
  min-width: 0;
  flex-wrap: wrap;
}

.query-actions {
  flex: 0 0 auto;
}

.table-wrap {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 16px 42px rgba(35, 74, 122, 0.07);
}

.pagination-bar {
  display: flex;
  justify-content: flex-end;
  padding: 2px 0;
}

@media (max-width: 860px) {
  .query-bar {
    align-items: stretch;
    flex-direction: column;
  }

  .query-actions {
    justify-content: flex-end;
  }
}
</style>
