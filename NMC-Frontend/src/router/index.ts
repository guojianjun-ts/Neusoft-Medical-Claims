import { createRouter, createWebHistory } from 'vue-router';
import UserLoginPage from '@/pages/user/userLoginPage.vue';
import HomePage from '@/pages/HomePage.vue';
import MedicalInfoManagement from '@/pages/InfoMaintenance/DrugMaintenanceInfo.vue';
import AboutView from '@/views/AboutView.vue';
import DrugMaintenanceInfo from '@/pages/InfoMaintenance/DrugMaintenanceInfo.vue';
import TreatmentMaintenance from '@/pages/InfoMaintenance/TreatmentMaintenance.vue';
import FacilityMaintenance from '@/pages/InfoMaintenance/FacilityMaintenance.vue';
import DrugReimbursement from '@/pages/InfoMaintenance/DrugReimbursement.vue';
import TertiaryHospitalReimbursement from '@/pages/InfoMaintenance/TertiaryHospitalReimbursement.vue';
import SecondaryHospitalReimbursement from '@/pages/InfoMaintenance/SecondaryHospitalReimbursement.vue';
import PrimaryHospitalReimbursement from '@/pages/InfoMaintenance/PrimaryHospitalReimbursement.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/user/login',
      name: '用户登录',
      component: UserLoginPage,
    },
    {
      path: '/info-management',
      name: '信息管理',
      component: MedicalInfoManagement,
      children: [
        {
          path: 'drug',
          name: '医保药品数据维护',
          component: DrugMaintenanceInfo,
        },
        {
          path: 'treatment',
          name: '诊疗项目数据维护',
          component: TreatmentMaintenance,
        },
        {
          path: 'facility',
          name: '医疗服务设施数据维护',
          component: FacilityMaintenance,
        },
        {
          path: 'drug-reimbursement',
          name: '药品报销比例',
          component: DrugReimbursement,
        },
        {
          path: 'tertiary-reimbursement',
          name: '三级医院报销比例',
          component: TertiaryHospitalReimbursement,
        },
        {
          path: 'secondary-reimbursement',
          name: '二级医院报销比例',
          component: SecondaryHospitalReimbursement,
        },
        {
          path: 'primary-reimbursement',
          name: '一级医院报销比例',
          component: PrimaryHospitalReimbursement,
        },
      ],
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
  ],
});

export default router;
