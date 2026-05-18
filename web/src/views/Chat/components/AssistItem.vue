<script setup>

const props = defineProps({id:{type: [String,Number],required: true},isSelected: Boolean, name: String, desc: String})
const emit = defineEmits(['click'])
const handleClick = () => {
  // 把 id 传给父组件
  emit('click', props.id)
}

</script>

<template>
  <div class="assist-item" :class="{ 'isSelected': props.isSelected }" @click="handleClick">
    <div class="icon">
      <el-icon :size="20" color="white">
        <ElementPlus/>
      </el-icon>
    </div>
    <div class="info">
      <div class="name" :class="{ 'isSelected': props.isSelected }">{{ name }}</div>
      <div class="desc">{{ desc }}</div>
    </div>
  </div>
</template>

<style scoped>
.assist-item.isSelected {
  background-color: #a5daf3;
  border-radius: 5px;
}
.assist-item {
  padding: 10px;
  display: flex;
  gap: 10px;
  justify-content: flex-start;
  align-items: center;

  .icon {
    background: #1f6bff;
    padding: 5px;
    border-radius: 50%;
    width: 35px;
    height: 35px;
    display: flex;
    justify-content: center;
    align-items: center;

    flex: 0 0 auto;
  }

  .info {
    .name {
      font-size: 14px;
      font-weight: bold;
    }

    .name.isSelected {
      color: blue;
    }

    .desc {
      color: gray;
      font-size: 12px;
    }
  }
}
</style>