<template>
  <div class="full-screen-login">
    <div class="login-container">
      <div class="login-banner">
        <h1>东软医疗保险系统</h1>
        <p>致力于医疗行业数字化改造</p>
      </div>
      <div class="login-form">
        <h2 class="title">用户登录</h2>
        <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
          <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
            <a-input v-model:value="formState.userAccount" placeholder="请输入账号" size="large" />
          </a-form-item>
          <a-form-item
            name="userPassword"
            :rules="[
              { required: true, message: '请输入密码' },
              { min: 8, message: '密码长度不能小于8位' },
            ]"
          >
            <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" size="large" />
          </a-form-item>
          <div class="login-actions">
            <a-checkbox>记住我</a-checkbox>
            <a href="/forgot-password">忘记密码?</a>
          </div>
          <a-form-item>
            <a-button type="primary" html-type="submit" size="large" block>登录</a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue'
import { userLoginUsingPost } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'
import router from '@/router'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const loginUserStore = useLoginUserStore()

const handleSubmit = async (values: any) => {
  const res = await userLoginUsingPost(values)
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
.full-screen-login {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  background-size: cover;
}

.login-container {
  display: flex;
  width: 80%;
  max-width: 1200px;
  height: 80vh;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-banner {
  flex: 1;
  background: linear-gradient(to right, #1890ff, #096dd9);
  color: white;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-banner h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
}

.login-banner p {
  font-size: 1.2rem;
  opacity: 0.9;
}

.login-form {
  flex: 1;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.title {
  font-size: 1.8rem;
  margin-bottom: 30px;
  color: #333;
  text-align: center;
}

.login-actions {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.register-tips {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.register-tips a {
  color: #1890ff;
  font-weight: 500;
}


@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    height: auto;
    width: 90%;
  }

  .login-banner,
  .login-form {
    padding: 40px 30px;
  }

  .login-banner {
    text-align: center;
  }
}
</style>
