<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
// 导入 ant-design-vue 配置组件和中文语言包
import { ConfigProvider } from 'ant-design-vue';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
</script>

<template>
  <div id="app">
    <!-- 用 ConfigProvider 包裹整个应用，配置全局中文环境 -->
    <ConfigProvider :locale="zhCN">
      <BasicLayout />
    </ConfigProvider>
  </div>
</template>

<style>
#app {
  min-height: 100vh;
}
</style>
