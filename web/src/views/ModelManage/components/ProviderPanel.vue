<script setup>
import { Plus } from '@element-plus/icons-vue'
import ProviderLogo from './ProviderLogo.vue'

defineProps({
  providers: {
    type: Array,
    required: true
  }
})
</script>

<template>
  <section class="provider-card">
    <h2>模型平台</h2>

    <div class="provider-list">
      <button
        v-for="provider in providers"
        :key="provider.id"
        :class="['provider-item', { 'is-active': provider.active }]"
        type="button"
      >
        <span class="provider-main">
          <ProviderLogo :logo="provider.logo" :color="provider.color" />
          <span>{{ provider.name }}</span>
        </span>

        <span class="provider-meta">
          <span class="provider-count">{{ provider.count }}</span>
          <span v-if="provider.online" class="provider-dot" />
        </span>
      </button>
    </div>

    <el-button class="add-provider-button" :icon="Plus">添加平台</el-button>
  </section>
</template>

<style scoped>
.provider-card {
  padding: 18px 16px 20px;
  border: 1px solid var(--model-border, #e4eaf3);
  border-radius: 12px;
  background: var(--model-card, #fff);
  box-shadow: var(--model-shadow, 0 16px 50px rgba(35, 74, 122, 0.08));
}

.provider-card h2 {
  margin: 0 0 18px;
  font-size: 18px;
  font-weight: 800;
  color: #101828;
}

.provider-list {
  display: grid;
  gap: 8px;
  margin-bottom: 18px;
}

.provider-item {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  min-height: 48px;
  border: 0;
  border-radius: 8px;
  padding: 8px 14px;
  color: #14213d;
  background: transparent;
  cursor: pointer;
  transition: background 0.2s ease, transform 0.2s ease, box-shadow 0.2s ease;
}

.provider-item:hover {
  background: #f5f8ff;
  transform: translateX(2px);
}

.provider-item.is-active {
  color: #0f63e6;
  background: linear-gradient(90deg, #eaf3ff 0%, #f2f7ff 100%);
  box-shadow: inset 0 0 0 1px rgba(22, 119, 255, 0.08);
}

.provider-main,
.provider-meta {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.provider-main {
  min-width: 0;
  font-size: 15px;
  font-weight: 600;
}

.provider-meta {
  flex-shrink: 0;
}

.provider-count {
  min-width: 25px;
  height: 23px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  color: #47607f;
  font-size: 12px;
  font-weight: 700;
  background: #f0f4fa;
}

.is-active .provider-count {
  color: #1677ff;
  background: #dcecff;
}

.provider-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #34c759;
  box-shadow: 0 0 0 3px rgba(52, 199, 89, 0.12);
}

.add-provider-button {
  width: 100%;
  height: 42px;
  border-radius: 8px;
  color: #101828;
  font-weight: 700;
  border-color: #dbe3ef;
  background: #fff;
}

@media (max-width: 1360px) {
  .provider-list {
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  }
}

@media (max-width: 640px) {
  .provider-list {
    grid-template-columns: 1fr;
  }
}
</style>
