import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from '@/api/userController'

export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  // 新增布局状态
  const layoutState = ref({
    showSidebar: true,
    showHeader: true,
    showFooter: true
  })

  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet()
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data
    }
  }

  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser;
  }

  // 新增布局控制方法
  function setLayoutState(state: Partial<typeof layoutState.value>) {
    layoutState.value = { ...layoutState.value, ...state }
  }

  return {
    loginUser,
    setLoginUser,
    fetchLoginUser,
    layoutState,
    setLayoutState
  };
});
