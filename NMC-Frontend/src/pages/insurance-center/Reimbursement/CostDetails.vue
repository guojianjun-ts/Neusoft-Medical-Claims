<template>
  <div class="cost-details-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2><DollarOutlined /> 参保人员费用报销详情</h2>
    </div>

    <!-- 参保人员信息 -->
    <div class="patient-info-container">
      <a-card :bordered="false">
        <a-row :gutter="24">
          <a-col :span="6">
            <div class="info-item">
              <span class="info-label">姓名：</span>
              <span class="info-value">{{ patientInfo.patientName }}</span>
            </div>
          </a-col>
          <a-col :span="6">
            <div class="info-item">
              <span class="info-label">性别：</span>
              <span class="info-value">{{ patientInfo.gender }}</span>
            </div>
          </a-col>
          <a-col :span="6">
            <div class="info-item">
              <span class="info-label">住院号：</span>
              <span class="info-value">{{ patientInfo.caseNumber }}</span>
            </div>
          </a-col>
          <a-col :span="6">
            <div class="info-item">
              <span class="info-label">总费用：</span>
              <span class="info-value text-primary">{{ totalCost.toFixed(2) }} 元</span>
            </div>
          </a-col>
        </a-row>
      </a-card>
    </div>

    <!-- 参保人员诊断信息 -->
    <div class="diagnosis-info-container">
      <a-card :bordered="false">
        <a-row :gutter="24">
          <a-col :span="8">
            <div class="diagnosis-item">
              <span class="diagnosis-label">入院诊断：</span>
              <span class="diagnosis-value">{{ admissionDiagnosis }}</span>
            </div>
          </a-col>
          <a-col :span="8">
            <div class="diagnosis-item">
              <span class="diagnosis-label">主要诊断：</span>
              <span class="diagnosis-value">{{ primaryDiagnosis }}</span>
            </div>
          </a-col>
          <a-col :span="8">
            <div class="diagnosis-item">
              <span class="diagnosis-label">其它诊断：</span>
              <span class="diagnosis-value">{{ otherDiagnosis }}</span>
            </div>
          </a-col>
        </a-row>
      </a-card>
    </div>

    <!-- 图表区域 -->
    <div class="chart-container">
      <!-- 药品费用图表 -->
      <a-card :bordered="false" class="chart-card">
        <template #title>
          <div class="chart-title">
            <PieChartOutlined />
            <span>药品费用占比</span>
          </div>
        </template>
        <div class="chart-filter">
          <div class="filter-item"
               v-for="(value, name) in drugFilters"
               :key="name"
               @click="toggleDrugFilter(name)">
            <div class="color-block" :style="{backgroundColor: drugColors[name]}"></div>
            <span class="filter-label">{{ name }}</span>
          </div>
        </div>
        <div ref="drugChartRef" class="chart-content"></div>
      </a-card>

      <!-- 报销详情图表 -->
      <a-card :bordered="false" class="chart-card">
        <template #title>
          <div class="chart-title">
            <PieChartOutlined />
            <span>报销详情占比</span>
          </div>
        </template>
        <div class="chart-filter">
          <div class="filter-item"
               v-for="(value, name) in reimbursementFilters"
               :key="name"
               @click="toggleReimbursementFilter(name)">
            <div class="color-block" :style="{backgroundColor: reimbursementColors[name]}"></div>
            <span class="filter-label">{{ name }}</span>
          </div>
        </div>
        <div ref="reimbursementChartRef" class="chart-content"></div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { DollarOutlined, PieChartOutlined } from '@ant-design/icons-vue';
import * as echarts from 'echarts';
import {
  getCurrentPatientUsingGet
} from '@/api/patientRegistrationController';
import {
  getTotalCostUsingGet,
  getDrugCategoryCostUsingGet,
  getAllCategoryCostUsingGet
} from '@/api/insuranceCostController';
import {
  listDiseaseByPatientIdUsingGet
} from '@/api/insuranceController';

// 路由和患者ID
const route = useRoute();
const patientId = ref<number | null>(Number(route.query.patientId) || null);

// 状态管理
const patientInfo = ref({
  patientName: '',
  gender: '',
  caseNumber: ''
});

const totalCost = ref(0);
const admissionDiagnosis = ref('');
const primaryDiagnosis = ref('');
const otherDiagnosis = ref('');
const drugCostData = ref({
  甲类: 0,
  乙类: 0,
  丙类: 0
});
const reimbursementData = ref({
  保险药品: 0,
  诊疗项目: 0,
  医疗服务: 0
});

// 图表引用
const drugChartRef = ref<HTMLDivElement | null>(null);
const reimbursementChartRef = ref<HTMLDivElement | null>(null);
const loading = ref(false);

// 颜色方案
const drugColors = {
  '甲类': '#FF6B6B',
  '乙类': '#4ECDC4',
  '丙类': '#FFD166'
};

const reimbursementColors = {
  '保险药品': '#06AED5',
  '诊疗项目': '#086788',
  '医疗服务': '#F0C808'
};

// 图表筛选器
const drugFilters = ref({
  '甲类': true,
  '乙类': true,
  '丙类': true
});

const reimbursementFilters = ref({
  '保险药品': true,
  '诊疗项目': true,
  '医疗服务': true
});

// 获取参保人员信息
const fetchPatientInfo = async () => {
  if (!patientId.value) {
    console.error('未获取到患者ID');
    return;
  }

  loading.value = true;
  try {
    const response = await getCurrentPatientUsingGet({ patientId: patientId.value });
    patientInfo.value = response.data.data;
  } catch (error) {
    console.error('获取参保人员信息失败:', error);
  } finally {
    loading.value = false;
  }
};

// 获取总费用
const fetchTotalCost = async () => {
  if (!patientId.value) {
    console.error('未获取到患者ID');
    return;
  }

  try {
    const response = await getTotalCostUsingGet({ patientId: patientId.value });
    totalCost.value = response.data.data;
  } catch (error) {
    console.error('获取总费用失败:', error);
  }
};

// 获取诊断信息
const fetchDiagnosisInfo = async () => {
  if (!patientId.value) {
    console.error('未获取到患者ID');
    return;
  }

  try {
    const response = await listDiseaseByPatientIdUsingGet({ patientId: patientId.value });
    const diseases = response.data.data.records;

    // 清空之前的诊断信息
    admissionDiagnosis.value = '';
    primaryDiagnosis.value = '';
    otherDiagnosis.value = '';

    // 分类整理诊断信息
    diseases.forEach((disease: any) => {
      if (disease.diseaseType === 1) {
        admissionDiagnosis.value += disease.diseaseName + ' ';
      } else if (disease.diseaseType === 2) {
        primaryDiagnosis.value += disease.diseaseName + ' ';
      } else if (disease.diseaseType === 3) {
        otherDiagnosis.value += disease.diseaseName + ' ';
      }
    });
  } catch (error) {
    console.error('获取诊断信息失败:', error);
  }
};

// 获取药品费用数据
const fetchDrugCostData = async () => {
  if (!patientId.value) {
    console.error('未获取到患者ID');
    return;
  }

  try {
    const response = await getDrugCategoryCostUsingGet({ patientId: patientId.value });
    drugCostData.value = response.data.data;
  } catch (error) {
    console.error('获取药品费用数据失败:', error);
  }
};

// 获取报销详情数据
const fetchReimbursementData = async () => {
  if (!patientId.value) {
    console.error('未获取到患者ID');
    return;
  }

  try {
    const response = await getAllCategoryCostUsingGet({ patientId: patientId.value });
    reimbursementData.value = response.data.data;
  } catch (error) {
    console.error('获取报销详情数据失败:', error);
  }
};

// 切换药品筛选器
const toggleDrugFilter = (type: string) => {
  drugFilters.value[type] = !drugFilters.value[type];
  // 确保至少有一个筛选器是开启的
  const activeFilters = Object.values(drugFilters.value).filter(Boolean);
  if (activeFilters.length === 0) {
    drugFilters.value[type] = true;
  }
  initDrugChart();
};

// 切换报销筛选器
const toggleReimbursementFilter = (type: string) => {
  reimbursementFilters.value[type] = !reimbursementFilters.value[type];
  // 确保至少有一个筛选器是开启的
  const activeFilters = Object.values(reimbursementFilters.value).filter(Boolean);
  if (activeFilters.length === 0) {
    reimbursementFilters.value[type] = true;
  }
  initReimbursementChart();
};

// 初始化药品费用图表
const initDrugChart = () => {
  if (!drugChartRef.value) return;

  // 应用筛选器
  const filteredData = Object.entries(drugCostData.value)
    .filter(([name]) => drugFilters.value[name])
    .map(([name, value]) => ({
      name,
      value,
      itemStyle: { color: drugColors[name] }
    }));

  const myChart = echarts.init(drugChartRef.value);
  const option = {
    title: {
      text: '药品费用占比',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'normal'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}元 ({d}%)'
    },
    series: [
      {
        name: '药品费用',
        type: 'pie',
        radius: ['50%', '80%'],
        center: ['50%', '55%'],
        data: filteredData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        label: {
          show: true,
          formatter: '{b}: {d}%',
          fontSize: 12
        },
        labelLine: {
          show: true
        }
      }
    ]
  };

  myChart.setOption(option);

  // 监听窗口大小变化，调整图表
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 初始化报销详情图表
const initReimbursementChart = () => {
  if (!reimbursementChartRef.value) return;

  // 应用筛选器
  const filteredData = Object.entries(reimbursementData.value)
    .filter(([name]) => reimbursementFilters.value[name])
    .map(([name, value]) => ({
      name,
      value,
      itemStyle: { color: reimbursementColors[name] }
    }));

  const myChart = echarts.init(reimbursementChartRef.value);
  const option = {
    title: {
      text: '报销详情占比',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'normal'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}元 ({d}%)'
    },
    series: [
      {
        name: '报销详情',
        type: 'pie',
        radius: ['50%', '80%'],
        center: ['50%', '55%'],
        data: filteredData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        label: {
          show: true,
          formatter: '{b}: {d}%',
          fontSize: 12
        },
        labelLine: {
          show: true
        }
      }
    ]
  };

  myChart.setOption(option);

  // 监听窗口大小变化，调整图表
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 监听数据变化，更新图表
watch(drugCostData, () => {
  initDrugChart();
});

watch(reimbursementData, () => {
  initReimbursementChart();
});

// 页面加载时获取数据并初始化图表
onMounted(async () => {
  await fetchPatientInfo();
  await fetchTotalCost();
  await fetchDiagnosisInfo();
  await fetchDrugCostData();
  await fetchReimbursementData();

  // 确保DOM加载完成后初始化图表
  setTimeout(() => {
    initDrugChart();
    initReimbursementChart();
  }, 100);
});
</script>

<style scoped>
.cost-details-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
  margin: 0;
  display: flex;
  align-items: center;
}

.page-header h2 .anticon {
  margin-right: 8px;
}

.patient-info-container,
.diagnosis-info-container {
  margin-bottom: 16px;
}

.ant-card {
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.info-item,
.diagnosis-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.info-label,
.diagnosis-label {
  color: rgba(0, 0, 0, 0.65);
  margin-right: 8px;
  font-size: 14px;
}

.info-value,
.diagnosis-value {
  font-weight: 500;
  font-size: 14px;
}

.text-primary {
  color: #1890ff;
}

.chart-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 24px;
  margin-top: 24px;
}

.chart-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.chart-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.chart-title .anticon {
  margin-right: 8px;
  color: #1890ff;
}

.chart-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.filter-item {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  background-color: #f8f9fa;
  transition: all 0.3s;
}

.filter-item:hover {
  background-color: #e9ecef;
}

.color-block {
  width: 16px;
  height: 16px;
  border-radius: 3px;
  margin-right: 8px;
}

.filter-label {
  font-size: 14px;
}

.chart-content {
  height: 320px;
  width: 100%;
}

@media (max-width: 768px) {
  .chart-container {
    grid-template-columns: 1fr;
  }

  .info-item,
  .diagnosis-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .info-label,
  .diagnosis-label {
    margin-bottom: 4px;
  }
}
</style>
