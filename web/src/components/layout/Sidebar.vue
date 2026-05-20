<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import SpringAi from '@/assets/spring-ai.svg'
import {
  ChatDotSquare,
  Box,
  Setting,
  User,
  ArrowLeft,
  ArrowRight,
  SwitchButton
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const collapsed = ref(false)

const menuItems = [
  { label: '助手管理', path: '/chat', icon: ChatDotSquare },
  { label: '模型管理', path: '/model', icon: Box },
  { label: '系统设置', path: '/settings', icon: Setting }
]

const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}

const navigateTo = (path) => {
  router.push(path)
}

const collapseIcon = computed(() => collapsed.value ? ArrowRight : ArrowLeft)

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<template>
  <div :class="['sidebar', { collapsed: collapsed }]">
    <div class="sidebar-header">
      <img class="logo-icon" :src="SpringAi" />
      <span v-if="!collapsed" class="logo-title">Spirng AI </span>
    </div>

    <div class="menu-list">
      <div
        v-for="item in menuItems"
        :key="item.label"
        :class="['menu-item', { active: route.path === item.path }]"
        @click="navigateTo(item.path)"
      >
        <el-icon class="menu-icon">
          <component :is="item.icon" />
        </el-icon>
        <span v-if="!collapsed" class="menu-label">{{ item.label }}</span>
      </div>
    </div>

    <div class="sidebar-footer">
      <el-dropdown @command="handleLogout" trigger="click" placement="top">
        <div class="user-info">
          <el-avatar :size="32" class="user-avatar">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div v-if="!collapsed" class="user-detail">
            <div class="username">admin</div>
            <el-tag size="small" type="info" effect="light">专业版</el-tag>
          </div>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <div class="collapse-btn" @click="toggleCollapse">
      <el-icon><component :is="collapseIcon" /></el-icon>
      <span v-if="!collapsed">收起侧边栏</span>
    </div>
  </div>
</template>

<style scoped>
.sidebar {
  width: 200px;
  height: 100%;
  background: #fff;
  border-right: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
  transition: width 0.25s ease;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  padding: 20px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  white-space: nowrap;
}

.logo-icon {
  font-size: 24px;
  width: 30px;
  height: 30px;
  color: #3b82f6;
  flex-shrink: 0;
}

.logo-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  transition: opacity 0.15s ease;
}

.collapsed .logo-title {
  opacity: 0;
}

.new-btn {
  margin: 0 16px 16px;
  width: calc(100% - 32px);
  height: 40px;
  flex-shrink: 0;
  transition: opacity 0.15s ease;
}

.collapsed .new-btn {
  opacity: 0;
  pointer-events: none;
}

.menu-list {
  flex: 1;
  padding: 0 8px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  color: #4b5563;
  transition: all 0.2s;
  margin-bottom: 4px;
  white-space: nowrap;
}

.menu-item:hover {
  background: #f3f4f6;
  color: #1f2937;
}

.menu-item.active {
  background: #eff6ff;
  color: #3b82f6;
}

.menu-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.menu-label {
  font-size: 14px;
  transition: opacity 0.15s ease;
}

.collapsed .menu-label {
  opacity: 0;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid #f0f0f0;
  flex-shrink: 0;
  white-space: nowrap;
  display: flex;
  justify-content: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px;
  border-radius: 6px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f3f4f6;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  background: #f3f4f6;
  color: #6b7280;
  flex-shrink: 0;
}

.user-detail {
  flex: 1;
  min-width: 0;
  transition: opacity 0.15s ease;
}

.collapsed .user-detail {
  opacity: 0;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 4px;
}

.collapse-btn {
  padding: 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  color: #6b7280;
  font-size: 13px;
  transition: color 0.2s;
  flex-shrink: 0;
  white-space: nowrap;
}

.collapse-btn:hover {
  color: #3b82f6;
}

.collapsed .collapse-btn {
  justify-content: center;
}

.collapse-btn span {
  transition: opacity 0.15s ease;
}

.collapsed .collapse-btn span {
  opacity: 0;
}
</style>
