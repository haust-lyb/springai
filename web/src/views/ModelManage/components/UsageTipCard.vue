<script setup>
import { Right } from '@element-plus/icons-vue'
</script>

<template>
  <section class="usage-tip-card">
    <div class="tip-title">
      <span class="tip-bulb">💡</span>
      <span>使用提示</span>
    </div>
    <p>在使用模型前，请确保已正确配置 API 密钥和相关参数。</p>
    <a href="javascript:void(0)">
      查看使用文档
      <el-icon><Right /></el-icon>
    </a>
  </section>
</template>

<style scoped>
.usage-tip-card {
  padding: 18px 18px 20px;
  border-radius: 12px;
  border: 1px solid rgba(226, 233, 244, 0.7);
  background:
    radial-gradient(circle at 95% 10%, rgba(22, 119, 255, 0.12), transparent 34%),
    linear-gradient(135deg, #f3f8ff 0%, #eef4ff 100%);
  box-shadow: 0 16px 42px rgba(70, 108, 168, 0.1);
}

.tip-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: #101828;
  font-size: 15px;
  font-weight: 800;
}

.tip-bulb {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: rgba(255, 196, 9, 0.18);
}

.usage-tip-card p {
  margin: 0 0 14px;
  color: #58667f;
  font-size: 14px;
  line-height: 1.7;
}

.usage-tip-card a {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #1677ff;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
}
</style>
