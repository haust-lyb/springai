<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  ArrowDown,
  ChatRound,
  Connection,
  Lock,
  Setting,
  User
} from '@element-plus/icons-vue'
import SpringAiIcon from '@/assets/spring-ai.svg'
import authApi from '@/api/auth.js'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const password = ref('')
const rememberMe = ref(false)
const loading = ref(false)

const features = [
  {
    title: '多模型接入',
    desc: '支持 OpenAI、Anthropic、Ollama 等多种模型',
    icon: Connection
  },
  {
    title: '助手与话题管理',
    desc: '轻松创建助手，管理话题与会话',
    icon: ChatRound
  },
  {
    title: '灵活配置',
    desc: '自定义提示词、模型参数与系统设置',
    icon: Setting
  },
  {
    title: '安全可靠',
    desc: '企业级安全保障，保护你的数据隐私',
    icon: Lock
  }
]

const handleLogin = async () => {
  if (!username.value || !password.value) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  authApi.login({ username: username.value, password: password.value }).then((res) => {
    if (res.code === 200) {
      userStore.setToken(res.data.token)
      userStore.setUserInfo({
        id: res.data.userId,
        username: res.data.username,
        nickname: res.data.nickname,
        role: res.data.role
      })
      loading.value = false
      router.push('/chat')
    }
    if (res.code !== 200) {
      ElMessage.error(res.message)
      loading.value = false
    }
  }).catch(() => {
    loading.value = false
  })
}
</script>

<template>
  <main class="login-page">
    <section class="brand-panel">
      <div class="brand-header">
        <span class="brand-logo-wrap">
          <img :src="SpringAiIcon" alt="Spring AI Logo" class="brand-logo" />
        </span>
        <span class="brand-name">AI 集成聊天系统</span>
      </div>

      <div class="hero-copy">
        <h1>一站式 AI 对话管理平台</h1>
        <p>连接多种大模型，打造专属 AI 助手，提升团队生产力</p>
      </div>

      <div class="feature-list">
        <div v-for="item in features" :key="item.title" class="feature-item">
          <span class="feature-icon">
            <el-icon><component :is="item.icon" /></el-icon>
          </span>
          <span>
            <strong>{{ item.title }}</strong>
            <small>{{ item.desc }}</small>
          </span>
        </div>
      </div>

      <div class="hero-illustration" aria-hidden="true">
        <div class="orb orb-left">AI</div>
        <div class="mock-window">
          <div class="window-dots">
            <i></i><i></i><i></i>
          </div>
          <div class="mock-sidebar">
            <span></span><span></span><span></span><span></span>
          </div>
          <div class="mock-chat">
            <b></b>
            <span></span>
            <em></em>
            <span class="short"></span>
          </div>
          <div class="floating-bubble bubble-message"></div>
          <div class="floating-bubble bubble-user"></div>
        </div>
      </div>
    </section>

    <section class="login-panel">
      <div class="top-actions">
        <button type="button" class="ghost-action">
          浅色模式
        </button>
        <button type="button" class="ghost-action">
          简体中文
          <el-icon><ArrowDown /></el-icon>
        </button>
      </div>

      <div class="login-card">
        <div class="card-heading">
          <h2>欢迎回来</h2>
          <p>登录以继续使用 AI 集成聊天系统</p>
        </div>

        <div class="login-tabs">
          <button type="button" class="tab-item active">账号登录</button>
          <button type="button" class="tab-item">SSO 登录</button>
        </div>

        <div class="form-section">
          <label class="form-label">邮箱或用户名</label>
          <el-input
            v-model="username"
            size="large"
            placeholder="请输入邮箱或用户名"
            :prefix-icon="User"
            class="login-input"
            @keyup.enter="handleLogin"
          />

          <label class="form-label">密码</label>
          <el-input
            v-model="password"
            size="large"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            class="login-input"
            show-password
            @keyup.enter="handleLogin"
          />

          <div class="login-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <a href="javascript:void(0)">忘记密码?</a>
          </div>

          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </div>

        <div class="divider"><span>或使用以下方式登录</span></div>

        <div class="oauth-list">
          <button type="button" class="oauth-button google"><span>G</span>使用 Google 登录</button>
          <button type="button" class="oauth-button github"><span>●</span>使用 GitHub 登录</button>
          <button type="button" class="oauth-button microsoft"><span>▦</span>使用 Microsoft 登录</button>
        </div>

        <div class="register-tip">
          还没有账号？<a href="javascript:void(0)">立即注册</a>
        </div>
      </div>
    </section>

    <footer class="login-footer">
      <span>© 2024 AI 集成聊天系统. 保留所有权利。</span>
      <nav>
        <a href="javascript:void(0)">帮助文档</a>
        <a href="javascript:void(0)">隐私政策</a>
        <a href="javascript:void(0)">服务条款</a>
      </nav>
    </footer>
  </main>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  position: relative;
  display: grid;
  grid-template-columns: minmax(480px, 1.05fr) minmax(460px, 1fr);
  overflow: hidden;
  color: #101828;
  background:
    radial-gradient(circle at 18% 10%, rgba(22, 119, 255, 0.1), transparent 28%),
    radial-gradient(circle at 82% 22%, rgba(91, 141, 239, 0.1), transparent 28%),
    linear-gradient(135deg, #f8fbff 0%, #ffffff 52%, #f9fbff 100%);
}

.brand-panel {
  min-height: 100vh;
  position: relative;
  display: flex;
  flex-direction: column;
  padding: 36px 72px 96px;
  overflow: hidden;
  background:
    linear-gradient(180deg, rgba(235, 244, 255, 0.52), rgba(255, 255, 255, 0.2)),
    radial-gradient(circle at 74% 72%, rgba(22, 119, 255, 0.13), transparent 38%);
}

.brand-header,
.top-actions,
.feature-item,
.login-options,
.login-footer,
.login-footer nav {
  display: flex;
  align-items: center;
}

.brand-header {
  gap: 12px;
  font-size: 22px;
  font-weight: 850;
}

.brand-logo-wrap {
  width: 36px;
  height: 36px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 11px;
  background: #1677ff;
  box-shadow: 0 12px 24px rgba(22, 119, 255, 0.22);
}

.brand-logo {
  width: 26px;
  height: 26px;
}

.hero-copy {
  margin-top: 108px;
  max-width: 650px;
}

.hero-copy h1 {
  margin: 0;
  color: #101828;
  font-size: clamp(34px, 4.2vw, 48px);
  line-height: 1.16;
  font-weight: 900;
  letter-spacing: -0.04em;
}

.hero-copy p {
  margin: 22px 0 0;
  color: #52627d;
  font-size: 18px;
  line-height: 1.7;
  font-weight: 650;
}

.feature-list {
  display: grid;
  gap: 28px;
  margin-top: 48px;
  max-width: 560px;
}

.feature-item {
  gap: 16px;
}

.feature-icon {
  width: 48px;
  height: 48px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  border-radius: 13px;
  color: #1677ff;
  background: #eaf3ff;
  font-size: 22px;
}

.feature-item strong,
.feature-item small {
  display: block;
}

.feature-item strong {
  color: #101828;
  font-size: 16px;
  font-weight: 850;
  margin-bottom: 7px;
}

.feature-item small {
  color: #60708f;
  font-size: 14px;
  line-height: 1.4;
}

.hero-illustration {
  position: absolute;
  right: 70px;
  bottom: 70px;
  width: 520px;
  height: 270px;
  pointer-events: none;
}

.mock-window {
  position: absolute;
  left: 86px;
  bottom: 18px;
  width: 390px;
  height: 210px;
  border: 8px solid rgba(193, 215, 250, 0.76);
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.72);
  box-shadow: 0 30px 70px rgba(72, 119, 194, 0.18);
  backdrop-filter: blur(12px);
}

.mock-window::after {
  position: absolute;
  left: -110px;
  right: -110px;
  bottom: -42px;
  height: 54px;
  border-radius: 50%;
  background: radial-gradient(ellipse, rgba(126, 172, 245, 0.18), transparent 72%);
  content: '';
}

.window-dots {
  position: absolute;
  left: 18px;
  top: 17px;
  display: flex;
  gap: 6px;
}

.window-dots i {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ff746d;
}

.window-dots i:nth-child(2) {
  background: #ffc65a;
}

.window-dots i:nth-child(3) {
  background: #68d391;
}

.mock-sidebar {
  position: absolute;
  left: 18px;
  top: 43px;
  display: grid;
  gap: 12px;
}

.mock-sidebar span {
  width: 54px;
  height: 12px;
  border-radius: 999px;
  background: #dbeafe;
}

.mock-sidebar span:first-child {
  width: 70px;
  background: #9cc3ff;
}

.mock-chat {
  position: absolute;
  left: 112px;
  right: 34px;
  top: 48px;
  display: grid;
  gap: 24px;
}

.mock-chat b,
.mock-chat span,
.mock-chat em {
  display: block;
  height: 24px;
  border-radius: 999px;
  background: #eaf2ff;
}

.mock-chat b {
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #7aaeff, #377dff);
}

.mock-chat em {
  width: 150px;
  margin-left: auto;
  background: #f1f6ff;
}

.mock-chat .short {
  width: 190px;
}

.floating-bubble {
  position: absolute;
  border-radius: 20px;
  background: linear-gradient(145deg, #4d91ff, #166eff);
  box-shadow: 0 18px 36px rgba(22, 119, 255, 0.28);
}

.bubble-message {
  right: 0;
  top: 122px;
  width: 94px;
  height: 72px;
}

.bubble-user {
  right: 66px;
  bottom: 0;
  width: 64px;
  height: 64px;
  opacity: 0.78;
}

.orb-left {
  position: absolute;
  left: 0;
  bottom: 28px;
  width: 76px;
  height: 76px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 20px;
  color: #fff;
  font-size: 34px;
  font-weight: 900;
  background: linear-gradient(145deg, #b8d6ff, #6fa7ff);
  box-shadow: 0 18px 44px rgba(22, 119, 255, 0.2);
}

.login-panel {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 86px 72px 96px;
}

.top-actions {
  position: absolute;
  right: 42px;
  top: 36px;
  gap: 12px;
}

.ghost-action {
  height: 36px;
  display: inline-flex;
  align-items: center;
  gap: 7px;
  border: 1px solid #dbe3ef;
  border-radius: 9px;
  padding: 0 14px;
  color: #344054;
  background: rgba(255, 255, 255, 0.82);
  font-size: 14px;
  font-weight: 700;
}

.login-card {
  width: min(100%, 520px);
  padding: 44px 40px 36px;
  border: 1px solid rgba(228, 234, 243, 0.88);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 24px 70px rgba(52, 83, 130, 0.1);
  backdrop-filter: blur(18px);
}

.card-heading {
  text-align: center;
}

.card-heading h2 {
  margin: 0;
  color: #101828;
  font-size: 28px;
  line-height: 1.2;
  font-weight: 900;
  letter-spacing: -0.03em;
}

.card-heading p {
  margin: 14px 0 0;
  color: #60708f;
  font-size: 14px;
  font-weight: 650;
}

.login-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  margin-top: 34px;
  border-bottom: 1px solid #e1e8f2;
}

.tab-item {
  height: 42px;
  border: 0;
  border-bottom: 2px solid transparent;
  color: #60708f;
  background: transparent;
  font-size: 15px;
  font-weight: 800;
  cursor: pointer;
}

.tab-item.active {
  color: #1677ff;
  border-bottom-color: #1677ff;
}

.form-section {
  margin-top: 24px;
}

.form-label {
  display: block;
  margin: 18px 0 9px;
  color: #1f2a44;
  font-size: 14px;
  font-weight: 800;
}

.login-input :deep(.el-input__wrapper) {
  min-height: 44px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 0 0 1px #dbe3ef inset;
}

.login-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #bfd0ea inset;
}

.login-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #1677ff inset, 0 0 0 3px rgba(22, 119, 255, 0.08);
}

.login-options {
  justify-content: space-between;
  gap: 12px;
  margin: 20px 0 22px;
}

.login-options a,
.register-tip a,
.login-footer a {
  color: #1677ff;
  font-weight: 800;
  text-decoration: none;
}

.login-btn {
  width: 100%;
  height: 46px;
  border: 0;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 850;
  background: linear-gradient(135deg, #1677ff 0%, #0f63e6 100%);
  box-shadow: 0 14px 28px rgba(22, 119, 255, 0.18);
}

.divider {
  position: relative;
  margin: 28px 0 18px;
  text-align: center;
  color: #8a98ad;
  font-size: 13px;
  font-weight: 700;
}

.divider::before {
  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  height: 1px;
  background: #e5eaf2;
  content: '';
}

.divider span {
  position: relative;
  padding: 0 16px;
  background: rgba(255, 255, 255, 0.94);
}

.oauth-list {
  display: grid;
  gap: 10px;
}

.oauth-button {
  height: 42px;
  border: 1px solid #dbe3ef;
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 11px;
  color: #1f2a44;
  background: #fff;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
}

.oauth-button span {
  font-size: 18px;
  font-weight: 900;
}

.google span {
  color: #4285f4;
}

.github span {
  color: #111827;
}

.microsoft span {
  color: #f25022;
}

.register-tip {
  margin-top: 34px;
  color: #60708f;
  text-align: center;
  font-size: 14px;
  font-weight: 700;
}

.login-footer {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  min-height: 64px;
  justify-content: center;
  gap: 24vw;
  border-top: 1px solid rgba(228, 234, 243, 0.72);
  color: #60708f;
  background: rgba(255, 255, 255, 0.5);
  font-size: 13px;
  font-weight: 650;
}

.login-footer nav {
  gap: 30px;
}

@media (max-width: 1180px) {
  .login-page {
    grid-template-columns: 1fr;
    overflow: auto;
  }

  .brand-panel {
    min-height: auto;
    padding: 30px 32px 42px;
  }

  .hero-copy {
    margin-top: 52px;
  }

  .hero-illustration {
    display: none;
  }

  .feature-list {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .login-panel {
    min-height: auto;
    padding: 38px 32px 110px;
  }

  .top-actions {
    position: fixed;
    right: 22px;
    top: 22px;
    z-index: 2;
  }

  .login-footer {
    gap: 40px;
  }
}

@media (max-width: 720px) {
  .brand-panel {
    padding: 24px 20px 32px;
  }

  .brand-name {
    font-size: 18px;
  }

  .hero-copy h1 {
    font-size: 30px;
  }

  .hero-copy p {
    font-size: 15px;
  }

  .feature-list {
    grid-template-columns: 1fr;
    gap: 18px;
  }

  .login-panel {
    padding: 24px 16px 130px;
  }

  .login-card {
    padding: 32px 22px 28px;
  }

  .top-actions {
    display: none;
  }

  .login-footer {
    flex-direction: column;
    gap: 8px;
    padding: 12px 16px;
    text-align: center;
  }

  .login-footer nav {
    gap: 16px;
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>
