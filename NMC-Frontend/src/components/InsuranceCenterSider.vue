<template>
  <a-layout-sider class="sider" width="220">
    <a-menu
      mode="inline"
      :selectedKeys="selectedKeys"
      :openKeys="openKeys"
      @openChange="onOpenChange"
    >
      <!-- 参保人员信息管理 -->
      <a-menu-item key="/insurance-center/insured-info" @click="navigateTo('/insurance-center/insured-info')">
        <template #icon>
          <UserOutlined />
        </template>
        参保人员信息管理
      </a-menu-item>

      <!-- 参保人员费用查询 -->
      <a-menu-item key="/insurance-center/cost-query" @click="navigateTo('/insurance-center/cost-query')">
        <template #icon>
          <SearchOutlined />
        </template>
        参保人员费用查询
      </a-menu-item>

      <!-- 参保人员费用报销（直接跳转，不再有子菜单） -->
      <a-menu-item key="/insurance-center/reimbursement" @click="navigateTo('/insurance-center/reimbursement')">
        <template #icon>
          <DollarOutlined />
        </template>
        参保人员费用报销
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import {
  UserOutlined,
  SearchOutlined,
  DollarOutlined
} from '@ant-design/icons-vue';

const router = useRouter();
const route = useRoute();
const selectedKeys = ref([]);
const openKeys = ref([]); // 不再需要默认展开

const onOpenChange = (keys) => {
  openKeys.value = keys;
};

const navigateTo = (path) => {
  router.push(path);
};

// 自动更新选中状态
watch(() => route.path, (newPath) => {
  selectedKeys.value = [newPath];
}, { immediate: true });
</script>

<style scoped>
.sider {
  background: #fff;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
}

.ant-menu {
  border-right: none;
}

/* 增强交互效果 */
.ant-menu-item:hover,
.ant-menu-submenu-title:hover {
  background-color: #f0f7ff !important;
}

.ant-menu-item-selected {
  background-color: #e6f4ff !important;
  font-weight: 500;
}

.ant-menu-submenu-selected > .ant-menu-submenu-title {
  color: #1890ff;
}
</style>
