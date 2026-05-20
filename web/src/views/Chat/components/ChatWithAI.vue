<script setup>
import MessageSender from "@/views/Chat/components/MessageSender.vue";
import ChatList from "@/views/Chat/components/ChatList.vue";

defineProps({chatId: String})
</script>

<template>
  <div class="chat-container">
    <!--    消息展示区域-->
    <ChatList></ChatList>
    <!--    消息发送区域-->
    <MessageSender></MessageSender>
  </div>
</template>

<style scoped>

.chat-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 100%;

}
</style>