<template>
  <a-menu
    id="medicalInfoSideMenu"
    v-model:openKeys="openKeys"
    v-model:selectedKeys="selectedKeys"
    style="width: 200px; height: calc(100vh - 64px); position: fixed;"
    mode="inline"
    :items="items"
    @click="handleClick"
  ></a-menu>
</template>

<script lang="ts" setup>
import { reactive, ref, watch, VueElement, h } from 'vue';
import { DatabaseOutlined, PercentageOutlined } from '@ant-design/icons-vue';
import type { MenuProps, ItemType } from 'ant-design-vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const selectedKeys = ref<string[]>(['1']);
const openKeys = ref<string[]>(['sub1']);

function getItem(
  label: VueElement | string,
  key: string,
  icon?: any,
  children?: ItemType[],
  type?: 'group',
): ItemType {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as ItemType;
}

const items: ItemType[] = reactive([
  getItem('数据信息维护', 'sub1', () => h(DatabaseOutlined), [
    getItem('医保药品数据维护', '1'),
    getItem('诊疗项目数据维护', '2'),
    getItem('医疗服务设施数据维护', '3'),
  ]),
  getItem('报销比例维护', 'sub2', () => h(PercentageOutlined), [
    getItem('药品报销比例', '4'),
    getItem('三级医院报销比例', '5'),
    getItem('二级医院报销比例', '6'),
    getItem('一级医院报销比例', '7'),
  ]),
]);

const handleClick: MenuProps['onClick'] = (e) => {
  console.log('click', e);
  // 根据点击的菜单项跳转到相应的路由
  switch (e.key) {
    case '1':
      router.push('/info-management/drug');
      break;
    case '2':
      router.push('/info-management/treatment');
      break;
    case '3':
      router.push('/info-management/facility');
      break;
    case '4':
      router.push('/info-management/drug-reimbursement');
      break;
    case '5':
      router.push('/info-management/tertiary-reimbursement');
      break;
    case '6':
      router.push('/info-management/secondary-reimbursement');
      break;
    case '7':
      router.push('/info-management/primary-reimbursement');
      break;
  }
};

watch(openKeys, (val) => {
  console.log('openKeys', val);
});
</script>

<style scoped>
#medicalInfoSideMenu {
  border-inline-end: none !important;
}

#medicalInfoSideMenu :deep(.ant-menu-item) {
  padding-left: 16px !important;
  margin: 0;
}

#medicalInfoSideMenu :deep(.ant-menu-submenu-title) {
  padding-left: 16px !important;
  margin: 0;
}

#medicalInfoSideMenu :deep(.ant-menu-sub) {
  padding-left: 0 !important;
}
</style>
