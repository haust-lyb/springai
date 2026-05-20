<script setup>
import Sidebar from './Sidebar.vue'
</script>

<template>
  <div class="layout">
    <Sidebar />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  width: 100%;
  height: 100vh;
  background: #f5f7fa;
}

.main-content {
  flex: 1;
  min-width: 0;
  overflow: hidden;
}
</style>
