<script setup>
import { ref } from 'vue';

const chatHistory = ref([]);

chatHistory.value.push({ role: 'user', content: '你好' });
chatHistory.value.push({ role: 'assistant', content: '你好，有什么我可以帮忙的吗？' });
chatHistory.value.push({ role: 'user', content: '我需要帮助' });
chatHistory.value.push({ role: 'assistant', content: '当然可以，你需要帮助什么？' })
chatHistory.value.push({ role: 'user', content: '帮我写一篇歌颂祖国的作文' })
chatHistory.value.push({ role: 'assistant', content: '好的，大兄弟，我帮你写一篇结构完整、情感真挚的赞美祖国的作文，内容可以体现爱国情怀、历史底蕴以及现代发展成就。下面是范文：\n' +
      '\n' +
      '---\n' +
      '\n' +
      '**题目：我心中的祖国**\n' +
      '\n' +
      '每当我仰望蔚蓝的天空，心中总涌起一种深深的自豪感——那是对我伟大祖国的热爱。她有悠久的历史，有灿烂的文化，也有日新月异的现代发展，这一切都让我为身为中华儿女而自豪。\n' +
      '\n' +
      '中华民族拥有五千年的文明史。从古老的长城到庄严的故宫，从诗词歌赋到精妙的书画，这些瑰宝不仅展示了祖先的智慧，也让世界见证了中华文化的独特魅力。每一块砖、每一幅画，都承载着先辈们的辛勤与智慧，提醒我们不忘来路。\n' +
      '\n' +
      '而今的祖国，更是一片欣欣向荣的热土。高楼林立，科技创新日新月异，航天事业不断突破，人工智能、大数据等领域也逐渐走在世界前列。祖国的发展让每一个中国人都能切身感受到她的力量，也激励我们勇敢追梦，为她添光彩。\n' +
      '\n' +
      '祖国不仅是一片土地，更是一种精神的象征。她教会我们坚韧、勤奋和团结，也让我们懂得感恩与责任。无论我们走到哪里，心中那份对祖国的热爱与牵挂，从未改变。\n' +
      '\n' +
      '我爱我的祖国，爱她的山川河流，爱她的历史文化，更爱她那日益辉煌的今天与充满希望的未来。愿我们每一个中华儿女都能携手努力，让祖国更加繁荣昌盛，让她的光芒照耀世界每一个角落。\n' +
      '\n' +
      '---\n' +
      '\n' +
      '如果你愿意，我可以帮你再写一个**更短、更生动、适合中学或大学作文的版本**，语言更口语化，也更有感染力。\n' +
      '\n' +
      '你希望我写这个版本吗？\n' })




</script>

<template>
  <div class="message-area">
    <div class="message-container">
      <div class="message" :class="{'is-user-message': message.role === 'user'}" v-for="message in chatHistory">
        {{ message.content }}
      </div>
    </div>
  </div>
</template>

<style scoped>
  .message-area {
    width: 100%;
    height: 100%;
    overflow-y: scroll;
    flex: 1 1 auto;
    padding: 10px;
    box-sizing: border-box;
    margin-bottom: 10px;

    border: 1px solid #ccc;

    .message-container{
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;

      .message{
        background: bisque;
        padding: 10px;
        margin-bottom: 10px;
      }
      .message.is-user-message{
        align-self: flex-end; /* 单独靠右 */
        background: rgba(189, 189, 193, 0.37);
      }
    }

  }
</style>