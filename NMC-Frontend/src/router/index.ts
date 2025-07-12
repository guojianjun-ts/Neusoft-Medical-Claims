import { createRouter, createWebHistory } from 'vue-router'

// 医疗基本信息维护模块
const DrugData = () => import('@/pages/medical-info/DataMaintenance/DrugData.vue')
const TreatmentItems = () => import('@/pages/medical-info/DataMaintenance/DiagnosisTreatment.vue')
const MedicalServices = () => import('@/pages/medical-info/DataMaintenance/MedicalServices.vue')
const DrugRates = () => import('@/pages/medical-info/ReimbursementRates/DrugRates.vue')
const Level3Hospital = () => import('@/pages/medical-info/ReimbursementRates/Level3Hospital.vue')
const Level2Hospital = () => import('@/pages/medical-info/ReimbursementRates/Level2Hospital.vue')
const Level1Hospital = () => import('@/pages/medical-info/ReimbursementRates/Level1Hospital.vue')

// 医生站医嘱处理模块
const Admission = () => import('@/pages/doctor-station/Admission.vue')
const PatientSelectDiagnosis = () => import('@/pages/doctor-station/Diagnosis/PatientSelect.vue')
const AdmissionDiagnosis = () => import('@/pages/doctor-station/Diagnosis/AdmissionDiagnosis.vue')
const PrimaryDiagnosis = () => import('@/pages/doctor-station/Diagnosis/PrimaryDiagnosis.vue')
const OtherDiagnosis = () => import('@/pages/doctor-station/Diagnosis/OtherDiagnosis.vue')
const PatientSelectOrders = () => import('@/pages/doctor-station/MedicalOrders/PatientSelect.vue')
const DrugOrders = () => import('@/pages/doctor-station/MedicalOrders/DrugOrders.vue')
const TreatmentOrders = () => import('@/pages/doctor-station/MedicalOrders/TreatmentOrders.vue')
const ServiceOrders = () => import('@/pages/doctor-station/MedicalOrders/ServiceOrders.vue')

// 医保中心报销管理模块
const InsuredInfo = () => import('@/pages/insurance-center/InsuredInfo.vue')
const CostQuery = () => import('@/pages/insurance-center/CostQuery.vue')
const CostDetails = () => import('@/pages/insurance-center/Reimbursement/CostDetails.vue')
const ReimbursementDetails = () => import('@/pages/insurance-center/Reimbursement/ReimbursementDetails.vue')

const routes = [
  {
    path: '/',
    redirect: '/home', // 默认重定向到主页
    children: [

      // ====================== 基础路由 ======================
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/pages/HomePage.vue'),
        meta: { title: '系统首页', showInMenu: true }
      },
      {
        path: 'about',
        name: 'About',
        component: () => import('@/views/AboutView.vue'),
        meta: { title: '关于系统', showInMenu: true }
      },
      // ====================== 医疗基本信息维护模块 ======================
      {
        path: 'medical-info',
        meta: { title: '信息维护' },
        children: [
          // 数据信息维护
          {
            path: 'drug-data',
            component: DrugData,
            meta: { title: '医保药品数据维护' }
          },
          {
            path: 'treatment-items',
            component: TreatmentItems,
            meta: { title: '诊疗项目数据维护' }
          },
          {
            path: 'medical-services',
            component: MedicalServices,
            meta: { title: '医疗服务设施维护' }
          },

          // 报销比例维护
          {
            path: 'drug-rates',
            component: DrugRates,
            meta: { title: '药品报销比例设置' }
          },
          {
            path: 'level3-rates',
            component: Level3Hospital,
            meta: { title: '三级医院报销比例' }
          },
          {
            path: 'level2-rates',
            component: Level2Hospital,
            meta: { title: '二级医院报销比例' }
          },
          {
            path: 'level1-rates',
            component: Level1Hospital,
            meta: { title: '一级医院报销比例' }
          }
        ]
      },

      // ====================== 医生站医嘱处理模块 ======================
      {
        path: 'doctor-station',
        meta: { title: '医生工作站' },
        children: [
          // 入院登记
          {
            path: 'admission',
            component: Admission,
            meta: { title: '患者入院登记' }
          },

          // 患者诊断
          {
            path: 'diagnosis',
            redirect: 'diagnosis/patient-select',
            meta: { title: '患者诊断' },
            children: [
              {
                path: 'patient-select',
                component: PatientSelectDiagnosis,
                meta: { title: '选择患者' }
              },
              {
                path: 'admission-diagnosis',
                component: AdmissionDiagnosis,
                meta: { title: '入院诊断' }
              },
              {
                path: 'primary-diagnosis',
                component: PrimaryDiagnosis,
                meta: { title: '主要诊断' }
              },
              {
                path: 'other-diagnosis',
                component: OtherDiagnosis,
                meta: { title: '其他诊断' }
              }
            ]
          },

          // 患者医嘱
          {
            path: 'medical-orders',
            redirect: 'medical-orders/patient-select',
            meta: { title: '患者医嘱' },
            children: [
              {
                path: 'patient-select',
                component: PatientSelectOrders,
                meta: { title: '选择患者' }
              },
              {
                path: 'drug-orders',
                component: DrugOrders,
                meta: { title: '药品处方医嘱' }
              },
              {
                path: 'treatment-orders',
                component: TreatmentOrders,
                meta: { title: '治疗项目医嘱' }
              },
              {
                path: 'service-orders',
                component: ServiceOrders,
                meta: { title: '医疗服务医嘱' }
              }
            ]
          }
        ]
      },

      // ====================== 医保中心报销管理模块 ======================
      {
        path: 'insurance-center',
        meta: { title: '医保中心管理' },
        children: [
          {
            path: 'insured-info',
            component: InsuredInfo,
            meta: { title: '参保人员信息管理' }
          },
          {
            path: 'cost-query',
            component: CostQuery,
            meta: { title: '参保人员费用查询' }
          },
          {
            path: 'reimbursement',
            redirect: 'reimbursement/cost-details',
            meta: { title: '费用报销' },
            children: [
              {
                path: 'cost-details',
                component: CostDetails,
                meta: { title: '费用详情' }
              },
              {
                path: 'reimbursement-details',
                component: ReimbursementDetails,
                meta: { title: '报销详情' }
              }
            ]
          }
        ]
      }
    ]
  },
  {
    path: '/user/login',
    component: () => import('@/pages/user/userLoginPage.vue'),
    meta: {
      hideLayout: true // 添加这个meta标记
    }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 设置页面标题
router.afterEach((to) => {
  const titles = [];

  // 遍历所有匹配的路由记录
  to.matched.forEach(record => {
    if (record.meta?.title) {
      titles.push(record.meta.title);
    }
  });

  document.title = titles.join(' - ') || '医疗管理系统';
});

export default router
