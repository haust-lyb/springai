<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { User, Lock, View, Hide } from '@element-plus/icons-vue'
import SpringAiIcon from '@/assets/spring-ai.svg'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)

const handleLogin = async () => {
  if (!username.value || !password.value) {
    return
  }
  loading.value = true
  setTimeout(() => {
    userStore.setToken('mock-token-123456')
    userStore.setUserInfo({ id: 1, username: username.value })
    loading.value = false
    router.push('/chat')
  }, 500)
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo-section">
        <img :src="SpringAiIcon" alt="Spring AI Logo" class="logo-img" />
        <h1 class="title">Spring AI</h1>
        <p class="subtitle">一站式 AI 对话管理平台</p>
      </div>

      <div class="form-section">
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon"><User /></el-icon>
            <el-input
              v-model="username"
              placeholder="请输入用户名"
              size="large"
              class="custom-input"
              @keyup.enter="handleLogin"
            />
          </div>
        </div>

        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon"><Lock /></el-icon>
            <el-input
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入密码"
              size="large"
              class="custom-input"
              show-password-on="click"
              @keyup.enter="handleLogin"
            />
          </div>
        </div>

        <el-button
          type="primary"
          size="large"
          class="login-btn"
          :loading="loading"
          @click="handleLogin"
        >
          登 录
        </el-button>
      </div>

      <div class="footer-section">
        <p>© 2024 Spring AI. All rights reserved.</p>
      </div>
    </div>

    <div class="decoration-bg">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.decoration-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  pointer-events: none;
}

.blob {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
  background: #fff;
}

.blob-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  right: -100px;
}

.blob-2 {
  width: 300px;
  height: 300px;
  bottom: -80px;
  left: -80px;
}

.blob-3 {
  width: 200px;
  height: 200px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-box {
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 48px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1;
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-img {
  width: 56px;
  height: 56px;
  margin-bottom: 16px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 8px 0;
}

.subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.form-section {
  margin-bottom: 32px;
}

.form-item {
  margin-bottom: 20px;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
  z-index: 10;
}

.custom-input {
  padding-left: 36px;
}

:deep(.custom-input .el-input__wrapper) {
  padding-left: 36px;
  border-radius: 10px;
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  box-shadow: none;
  transition: all 0.3s ease;
}

:deep(.custom-input .el-input__wrapper:hover) {
  border-color: #667eea;
}

:deep(.custom-input .el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  background: #fff;
}

.login-btn {
  width: 100%;
  height: 48px;
  margin-top: 8px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.footer-section {
  text-align: center;
}

.footer-section p {
  margin: 0;
  font-size: 12px;
  color: #9ca3af;
}
</style>
