<template>
  <div class="not-found-container">
    <a-space direction="vertical" align="center" size="large">
      <div class="error-code">
        4
        <a-spin-spot size="large" class="zero-spinner" />
        4
      </div>

      <a-typography-title :level="3">页面神秘失踪</a-typography-title>

      <a-typography-text type="secondary">
        {{ errorMessages[currentMessage] }}
      </a-typography-text>

      <a-space>
        <a-button type="primary" @click="handleHome">
          <template #icon><home-outlined /></template>
          返回首页
        </a-button>
        <a-button @click="handleBack">
          <template #icon><rollback-outlined /></template>
          返回上一页
        </a-button>
      </a-space>

      <div class="search-box">
        <a-input-search
          placeholder="搜索您需要的内容..."
          enter-button="搜索"
          size="large"
          @search="handleSearch"
        />
      </div>
    </a-space>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  HomeOutlined,
  RollbackOutlined
} from '@ant-design/icons-vue'

const router = useRouter()

const errorMessages = [
  "宇宙射线干扰了页面加载...",
  "我们的数字小精灵弄丢了这张页面...",
  "或许您想访问的是其他内容？",
  "这个链接可能已经失效..."
]

const currentMessage = ref(0)

onMounted(() => {
  // 轮播错误信息
  const timer = setInterval(() => {
    currentMessage.value = (currentMessage.value + 1) % errorMessages.length
  }, 3000)

  return () => clearInterval(timer)
})

const handleHome = () => router.push('/')
const handleBack = () => router.go(-1)
const handleSearch = (value: string) => {
  // 这里可以接入站内搜索功能
  console.log('搜索:', value)
}
</script>

<style scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #1890ff;
  display: flex;
  align-items: center;
}

.zero-spinner {
  margin: 0 20px;
  color: #ff4d4f;
}

.search-box {
  width: 100%;
  max-width: 500px;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .error-code {
    font-size: 80px;
  }
}
</style>
