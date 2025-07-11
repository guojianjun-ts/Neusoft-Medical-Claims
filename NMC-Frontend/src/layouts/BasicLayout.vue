<template>
  <a-layout class="basic-layout">
    <!-- 顶部导航栏 -->
    <GlobalHeader @module-change="handleModuleChange" />

    <a-layout>
      <!-- 动态侧边栏 -->
      <component :is="currentSider" />

      <!-- 内容区域 -->
      <a-layout>
        <a-layout-content class="content-area">
          <router-view />
        </a-layout-content>
        <GlobalFooter />
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import GlobalHeader from '@/components/GlobalHeader.vue';
import GlobalFooter from '@/components/GlobalFooter.vue';
import MedicalInfoSider from '@/components/MedicalInfoSider.vue';
import DoctorStationSider from '@/components/DoctorStationSider.vue';
import InsuranceCenterSider from '@/components/InsuranceCenterSider.vue';

const router = useRouter();
const activeModule = ref('medical-info'); // 默认激活的模块

// 根据当前路由路径确定激活的模块
const determineActiveModule = (path) => {
  if (path.startsWith('/medical-info')) return 'medical-info';
  if (path.startsWith('/doctor-station')) return 'doctor-station';
  if (path.startsWith('/insurance-center')) return 'insurance-center';
  return 'medical-info'; // 默认
};

// 初始化时根据当前路由设置激活模块
activeModule.value = determineActiveModule(router.currentRoute.value.path);

// 监听路由变化
router.afterEach((to) => {
  activeModule.value = determineActiveModule(to.path);
});

// 动态侧边栏组件
const currentSider = computed(() => {
  switch (activeModule.value) {
    case 'medical-info': return MedicalInfoSider;
    case 'doctor-station': return DoctorStationSider;
    case 'insurance-center': return InsuranceCenterSider;
    default: return MedicalInfoSider;
  }
});

// 处理模块切换
const handleModuleChange = (module) => {
  activeModule.value = module;
  // 跳转到对应模块的默认页面
  switch (module) {
    case 'medical-info':
      router.push('/medical-info/drug-data');
      break;
    case 'doctor-station':
      router.push('/doctor-station/admission');
      break;
    case 'insurance-center':
      router.push('/insurance-center/insured-info');
      break;
  }
};
</script>

<style scoped>
.basic-layout {
  min-height: 100vh;
}

.content-area {
  padding: 24px;
  background: #fff;
  min-height: calc(100vh - 64px - 70px); /* 减去header和footer高度 */
}
</style>
