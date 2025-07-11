<template>
  <a-layout-header class="global-header">
    <div class="logo">
      <router-link to="/">
        <img src="/favicon.png" alt="logo" class="logo-img" />
        <span class="logo-text">NMC医保系统</span>
      </router-link>
    </div>
    <div class="header-divider"></div>
    <a-menu
      v-model:selectedKeys="selectedKeys"
      theme="dark"
      mode="horizontal"
      :items="items"
      @click="handleMenuClick"
    />
    <div class="user-info">
      <a-dropdown v-if="loginUser.id">
        <a-space>
          <a-avatar :src="loginUser.userAvatar" />
          {{ loginUser.userName || '无名' }}
        </a-space>
        <template #overlay>
          <a-menu>
            <a-menu-item @click="doLogout">
              <LogoutOutlined />
              退出登录
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
      <a-button v-else type="primary" href="/user/login">登录</a-button>
    </div>
  </a-layout-header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { userLogoutUsingPost } from '@/api/userController'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loginUser = computed(() => loginUserStore.loginUser)

const items = [
  { key: 'medical-info', label: '医疗基本信息维护' },
  { key: 'doctor-station', label: '医生站医嘱处理' },
  { key: 'insurance-center', label: '医保中心报销管理' }
]

const selectedKeys = ref([])

// 初始化选中状态
const updateSelectedKeys = () => {
  const path = router.currentRoute.value.path
  if (path.startsWith('/medical-info')) selectedKeys.value = ['medical-info']
  else if (path.startsWith('/doctor-station')) selectedKeys.value = ['doctor-station']
  else if (path.startsWith('/insurance-center')) selectedKeys.value = ['insurance-center']
  else selectedKeys.value = []
}

updateSelectedKeys()

// 监听路由变化
router.afterEach(() => {
  updateSelectedKeys()
})

const emit = defineEmits(['module-change'])

const handleMenuClick = ({ key }) => {
  emit('module-change', key)
}

// 用户注销
const doLogout = async () => {
  const res = await userLogoutUsingPost()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({})
    router.push('/user/login')
  }
}
</script>

<style scoped>
.global-header {
  display: flex;
  align-items: center;
  background: #001529;
  padding: 0;
  height: 64px;
  line-height: 64px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.logo {
  width: 220px; /* 与侧边栏宽度一致 */
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #002140;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.logo a {
  display: flex;
  align-items: center;
  color: white;
  text-decoration: none;
  width: 100%;
  justify-content: center;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 8px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
}

.header-divider {
  height: 100%;
  width: 1px;
  background-color: rgba(255, 255, 255, 0.1);
}

.ant-menu {
  flex: 1;
  line-height: 62px;
  border-bottom: none;
  padding-left: 20px;
}

.user-info {
  margin-right: 24px;
  margin-left: auto;
  padding-right: 20px;
  color: white; /* 新增这一行 */
}

/* 确保下拉菜单中的用户名也是白色 */
.user-info :deep(.ant-space) {
  color: white;
}

/* 确保下拉菜单项文本颜色 */
.user-info :deep(.ant-dropdown-menu-item) {
  color: rgba(0, 0, 0, 0.85); /* 下拉菜单保持默认黑色 */
}

/* 响应式调整 */
@media (max-width: 992px) {
  .logo-text {
    display: none;
  }
  .logo {
    width: 64px;
  }
}
</style>
