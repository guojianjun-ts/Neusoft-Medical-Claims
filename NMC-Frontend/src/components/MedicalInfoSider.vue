<template>
  <a-layout-sider class="sider" width="220">
    <a-menu
      mode="inline"
      :selectedKeys="selectedKeys"
      :openKeys="openKeys"
      @openChange="onOpenChange"
    >
      <a-sub-menu key="data-info">
        <template #icon><DatabaseOutlined /></template>
        <template #title>
          <span>数据信息维护</span>
        </template>
        <a-menu-item key="/medical-info/drug-data" @click="navigateTo('/medical-info/drug-data')">
          <template #icon><MedicineBoxOutlined /></template>
          医保药品数据维护
        </a-menu-item>
        <a-menu-item key="/medical-info/treatment-items" @click="navigateTo('/medical-info/treatment-items')">
          <template #icon><ExperimentOutlined /></template>
          诊疗项目数据维护
        </a-menu-item>
        <a-menu-item key="/medical-info/medical-services" @click="navigateTo('/medical-info/medical-services')">
          <HeartFilled />
          医疗服务设施数据维护
        </a-menu-item>
      </a-sub-menu>

      <a-sub-menu key="reimbursement">
        <template #icon><PercentageOutlined /></template>
        <template #title>
          <span>报销比例维护</span>
        </template>
        <a-menu-item key="/medical-info/drug-reimbursement" @click="navigateTo('/medical-info/drug-rates')">
          <template #icon><MedicineBoxOutlined /></template>
          药品报销比例
        </a-menu-item>
        <a-menu-item key="/medical-info/level1-rates" @click="navigateTo('/medical-info/level1-rates')">
          <template #icon><BankOutlined /></template>
          一级医院报销比例
        </a-menu-item>
        <a-menu-item key="/medical-info/level2-rates" @click="navigateTo('/medical-info/level2-rates')">
          <template #icon><BankOutlined /></template>
          二级医院报销比例
        </a-menu-item>
        <a-menu-item key="/medical-info/level3-rates" @click="navigateTo('/medical-info/level3-rates')">
          <template #icon><BankOutlined /></template>
          三级医院报销比例
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import {
  DatabaseOutlined,
  MedicineBoxOutlined,
  ExperimentOutlined,
  PercentageOutlined,
  BankOutlined,
  HeartFilled
} from '@ant-design/icons-vue';

const router = useRouter();
const selectedKeys = ref([]);
const openKeys = ref(['data-info', 'reimbursement']); // 默认展开所有子菜单

const onOpenChange = (keys) => {
  // 不再自动折叠其他菜单，保持当前状态
  openKeys.value = keys;
};

const navigateTo = (path) => {
  router.push(path);
  selectedKeys.value = [path];
};

// 初始化选中状态
router.afterEach((to) => {
  selectedKeys.value = [to.path];
});
</script>

<style scoped>
.sider {
  background: #fff;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
}

.ant-menu {
  border-right: none;
}

/* 添加与 DoctorStationSider 一致的交互效果 */
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
