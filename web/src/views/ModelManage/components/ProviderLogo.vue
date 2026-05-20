<script setup>
defineProps({
  logo: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: '#1677ff'
  },
  size: {
    type: String,
    default: 'medium'
  }
})
</script>

<template>
  <span :class="['provider-logo', `provider-logo--${size}`, `provider-logo--${logo}`]" :style="{ '--logo-color': color }">
    <span v-if="logo === 'swirl'" class="swirl-mark">◎</span>
    <span v-else-if="logo === 'cube'" class="cube-mark">◇</span>
    <span v-else-if="logo === 'llama'" class="llama-mark">♞</span>
    <span v-else>{{ logo }}</span>
  </span>
</template>

<style scoped>
.provider-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  color: #fff;
  font-size: 13px;
  font-weight: 800;
  line-height: 1;
  background: var(--logo-color);
  box-shadow: 0 6px 16px color-mix(in srgb, var(--logo-color), transparent 78%);
}

.provider-logo--small {
  width: 21px;
  height: 21px;
  font-size: 11px;
}

.provider-logo--AI {
  color: #141414;
}

.provider-logo--G {
  color: #fff;
  background: conic-gradient(from 15deg, #4285f4 0 25%, #34a853 0 50%, #fbbc05 0 75%, #ea4335 0);
}

.provider-logo--A {
  border-radius: 6px;
  background: linear-gradient(145deg, #12a4ff, #0866c8);
}

.provider-logo--llama {
  color: #111827;
  background: #f8fafc;
  border: 1px solid #dbe3ef;
  box-shadow: none;
}

.provider-logo--cube {
  border-radius: 8px;
  background: linear-gradient(145deg, #2488ff, #0d63e5);
}

.swirl-mark,
.cube-mark,
.llama-mark {
  transform: translateY(-0.5px);
}
</style>
