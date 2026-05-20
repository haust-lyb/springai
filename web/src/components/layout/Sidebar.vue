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
  { label: '助手管理', path: '/chat', icon: ChatDotSquare, desc: '会话与助手' },
  { label: '模型管理', path: '/model', icon: Box, desc: '平台与模型' },
  { label: '系统设置', path: '/settings', icon: Setting, desc: '全局配置' }
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
  <aside :class="['sidebar', { collapsed }]">
    <div class="sidebar-header">
      <div class="brand-mark">
        <img class="logo-icon" :src="SpringAi" alt="logo" />
      </div>
      <div v-if="!collapsed" class="brand-copy">
        <span class="logo-title">Spring AI</span>
        <span class="logo-subtitle">AI 集成聊天系统</span>
      </div>
    </div>

    <nav class="menu-list" aria-label="主导航">
      <div v-if="!collapsed" class="menu-section-label">工作台</div>
      <button
        v-for="item in menuItems"
        :key="item.label"
        type="button"
        :class="['menu-item', { active: route.path === item.path }]"
        @click="navigateTo(item.path)"
      >
        <span class="menu-icon-wrap">
          <el-icon class="menu-icon">
            <component :is="item.icon" />
          </el-icon>
        </span>
        <span v-if="!collapsed" class="menu-copy">
          <span class="menu-label">{{ item.label }}</span>
          <span class="menu-desc">{{ item.desc }}</span>
        </span>
      </button>
    </nav>

    <div class="sidebar-footer">
      <el-dropdown @command="handleLogout" trigger="click" placement="top">
        <div class="user-info">
          <el-avatar :size="collapsed ? 34 : 38" class="user-avatar">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div v-if="!collapsed" class="user-detail">
            <div class="username">admin</div>
            <el-tag size="small" type="primary" effect="light" round>专业版</el-tag>
          </div>
          <span v-if="!collapsed" class="user-arrow">⌄</span>
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

    <button class="collapse-btn" type="button" @click="toggleCollapse">
      <el-icon><component :is="collapseIcon" /></el-icon>
      <span v-if="!collapsed">收起侧边栏</span>
    </button>
  </aside>
</template>

<style scoped>
.sidebar {
  position: relative;
  width: 224px;
  height: 100%;
  flex: 0 0 auto;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background:
    radial-gradient(circle at 20% 0%, rgba(22, 119, 255, 0.1), transparent 30%),
    linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
  border-right: 1px solid #e3eaf5;
  box-shadow: 14px 0 42px rgba(35, 74, 122, 0.06);
  transition: width 0.25s ease;
}

.sidebar.collapsed {
  width: 76px;
}

.sidebar-header {
  min-height: 80px;
  padding: 20px 16px 18px;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
  white-space: nowrap;
}

.brand-mark {
  width: 38px;
  height: 38px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 10px 24px rgba(22, 119, 255, 0.14);
}

.logo-icon {
  width: 30px;
  height: 30px;
}

.brand-copy {
  display: grid;
  gap: 3px;
  min-width: 0;
}

.logo-title {
  font-size: 17px;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: #101828;
}

.logo-subtitle {
  color: #667085;
  font-size: 12px;
  font-weight: 600;
}

.menu-list {
  flex: 1;
  padding: 8px 12px;
}

.menu-section-label {
  margin: 0 10px 10px;
  color: #98a2b3;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.08em;
}

.menu-item {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 54px;
  padding: 8px 10px;
  margin-bottom: 8px;
  border: 0;
  border-radius: 14px;
  color: #4b5d78;
  background: transparent;
  cursor: pointer;
  text-align: left;
  white-space: nowrap;
  transition: background 0.2s ease, color 0.2s ease, transform 0.2s ease, box-shadow 0.2s ease;
}

.menu-item:hover {
  color: #101828;
  background: #f2f6ff;
  transform: translateX(2px);
}

.menu-item.active {
  color: #1267e8;
  background: linear-gradient(135deg, #eaf3ff 0%, #f4f8ff 100%);
  box-shadow: inset 0 0 0 1px rgba(22, 119, 255, 0.1), 0 12px 28px rgba(22, 119, 255, 0.1);
}

.menu-icon-wrap {
  width: 34px;
  height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: inset 0 0 0 1px rgba(214, 224, 240, 0.82);
}

.menu-item.active .menu-icon-wrap {
  color: #fff;
  background: linear-gradient(145deg, #2488ff, #0d63e5);
  box-shadow: 0 8px 18px rgba(22, 119, 255, 0.22);
}

.menu-icon {
  font-size: 18px;
}

.menu-copy {
  display: grid;
  gap: 2px;
  min-width: 0;
}

.menu-label {
  font-size: 14px;
  font-weight: 800;
}

.menu-desc {
  color: #98a2b3;
  font-size: 12px;
  font-weight: 600;
}

.active .menu-desc {
  color: #5c8bdc;
}

.sidebar-footer {
  padding: 14px 12px 12px;
  border-top: 1px solid rgba(226, 233, 244, 0.9);
  flex-shrink: 0;
}

.user-info {
  width: 100%;
  min-height: 58px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 9px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.78);
  box-shadow: inset 0 0 0 1px rgba(226, 233, 244, 0.86);
  transition: background 0.2s ease, box-shadow 0.2s ease;
}

.user-info:hover {
  background: #fff;
  box-shadow: inset 0 0 0 1px #cfe0f7, 0 12px 24px rgba(35, 74, 122, 0.08);
}

:deep(.el-dropdown) {
  width: 100%;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  color: #1677ff;
  background: linear-gradient(145deg, #edf5ff, #ffffff);
  box-shadow: inset 0 0 0 1px #dbe9ff;
  flex-shrink: 0;
}

.user-detail {
  flex: 1;
  min-width: 0;
}

.username {
  color: #101828;
  font-size: 14px;
  font-weight: 800;
  margin-bottom: 4px;
}

.user-arrow {
  color: #667085;
  font-size: 18px;
  transform: translateY(-2px);
}

.collapse-btn {
  min-height: 52px;
  padding: 0 16px;
  border: 0;
  border-top: 1px solid rgba(226, 233, 244, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #667085;
  background: rgba(255, 255, 255, 0.55);
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
  transition: color 0.2s ease, background 0.2s ease;
  flex-shrink: 0;
  white-space: nowrap;
}

.collapse-btn:hover {
  color: #1677ff;
  background: #f5f9ff;
}

@media (max-width: 1100px) {
  .sidebar {
    width: 76px;
  }

  .sidebar-header {
    justify-content: center;
    padding-inline: 12px;
  }

  .brand-copy,
  .menu-section-label,
  .menu-copy,
  .user-detail,
  .user-arrow,
  .collapse-btn span {
    display: none;
  }

  .menu-list {
    padding-inline: 10px;
  }

  .menu-item {
    justify-content: center;
    padding-inline: 8px;
  }

  .sidebar-footer {
    padding-inline: 10px;
  }

  .user-info {
    justify-content: center;
    padding: 8px;
  }
}
</style>
