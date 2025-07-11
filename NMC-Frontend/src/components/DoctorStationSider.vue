<template>
  <a-layout-sider class="sider" width="220">
    <a-menu
      mode="inline"
      :selectedKeys="selectedKeys"
      :openKeys="openKeys"
      @openChange="onOpenChange"
    >
      <!-- 入院登记 -->
      <a-menu-item key="/doctor-station/admission" @click="navigateTo('/doctor-station/admission')">
        <template #icon>
          <UserAddOutlined />
        </template>
        入院登记
      </a-menu-item>

      <!-- 患者诊断 -->
      <a-sub-menu key="diagnosis">
        <template #icon>
          <FileSearchOutlined />
        </template>
        <template #title>患者诊断</template>
        <a-menu-item key="/doctor-station/diagnosis/patient-select"
                     @click="navigateTo('/doctor-station/diagnosis/patient-select')">
          <template #icon>
            <TeamOutlined />
          </template>
          患者选择
        </a-menu-item>
        <a-menu-item key="/doctor-station/diagnosis/admission-diagnosis"
                     @click="navigateTo('/doctor-station/diagnosis/admission-diagnosis')">
          <template #icon>
            <FileTextOutlined />
          </template>
          入院诊断
        </a-menu-item>
        <a-menu-item key="/doctor-station/diagnosis/primary-diagnosis"
                     @click="navigateTo('/doctor-station/diagnosis/primary-diagnosis')">
          <template #icon>
            <FileMarkdownOutlined />
          </template>
          主要诊断
        </a-menu-item>
        <a-menu-item key="/doctor-station/diagnosis/other-diagnosis"
                     @click="navigateTo('/doctor-station/diagnosis/other-diagnosis')">
          <template #icon>
            <FileExclamationOutlined />
          </template>
          其它诊断
        </a-menu-item>
      </a-sub-menu>

      <!-- 患者医嘱 -->
      <a-sub-menu key="medical-orders">
        <template #icon>
          <FormOutlined />
        </template>
        <template #title>患者医嘱</template>
        <a-menu-item key="/doctor-station/medical-orders/patient-select"
                     @click="navigateTo('/doctor-station/medical-orders/patient-select')">
          <template #icon>
            <TeamOutlined />
          </template>
          患者选择
        </a-menu-item>
        <a-menu-item key="/doctor-station/medical-orders/drug-orders"
                     @click="navigateTo('/doctor-station/medical-orders/drug-orders')">
          <template #icon>
            <MedicineBoxOutlined />
          </template>
          药品处方医嘱
        </a-menu-item>
        <a-menu-item key="/doctor-station/medical-orders/treatment-orders"
                     @click="navigateTo('/doctor-station/medical-orders/treatment-orders')">
          <template #icon>
            <InteractionOutlined />
          </template>
          治疗项目医嘱
        </a-menu-item>
        <a-menu-item key="/doctor-station/medical-orders/service-orders"
                     @click="navigateTo('/doctor-station/medical-orders/service-orders')">
          <template #icon>
            <AuditOutlined />
          </template>
          医疗服务医嘱
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import {
  UserAddOutlined,
  FileSearchOutlined,
  FileTextOutlined,
  FileMarkdownOutlined,
  FileExclamationOutlined,
  FormOutlined,
  MedicineBoxOutlined,
  InteractionOutlined,
  AuditOutlined,
  TeamOutlined
} from '@ant-design/icons-vue';

const router = useRouter();
const selectedKeys = ref([]);
const openKeys = ref(['diagnosis', 'medical-orders']);

const onOpenChange = (keys) => {
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
