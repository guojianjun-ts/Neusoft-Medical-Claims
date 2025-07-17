<template>
  <div class="patient-select-container">
    <!-- 页面标题 -->
    <div class="page-title">
      <FileAddOutlined />
      医嘱患者选择：{{ selectedPatient?.patientName || '未选择患者' }}
    </div>

    <!-- 导航按钮区域 + 取消选中按钮 -->
    <div class="nav-buttons">
      <a-button
        type="primary"
        @click="handleNavigate('drug-orders')"
        :disabled="!selectedPatient"
      >
        <template #icon>
          <MedicineBoxOutlined />
        </template>
        增加药品处方医嘱
      </a-button>
      <a-button
        type="primary"
        @click="handleNavigate('treatment-orders')"
        :disabled="!selectedPatient"
        style="margin-left: 16px"
      >
        <template #icon>
          <ToolOutlined />
        </template>
        增加诊疗项目医嘱
      </a-button>
      <a-button
        type="primary"
        @click="handleNavigate('service-orders')"
        :disabled="!selectedPatient"
        style="margin-left: 16px"
      >
        <template #icon>
          <UserOutlined />
        </template>
        增加医疗服务医嘱
      </a-button>
    </div>

    <!-- 搜索和表格区域 -->
    <div class="table-section">
      <!-- 搜索框 + 取消选中按钮（并排显示） -->
      <div class="search-container">
        <div style="display: flex; align-items: center;">
          <a-input
            v-model:value="searchParams.patientName"
            placeholder="请输入患者姓名搜索"
            style="width: 300px"
            allow-clear
            @press-enter="handleSearch"
          >
            <template #suffix>
              <SearchOutlined @click="handleSearch" />
            </template>
          </a-input>

          <!-- 取消选中按钮：移到搜索框右侧，方块样式 -->
          <a-button
            type="default"
            @click="handleCancelSelect"
            :disabled="!selectedPatient"
            style="margin-left: 12px; width: 100px; height: 40px;"
          >
          取消选中
          </a-button>
        </div>
      </div>

      <!-- 患者表格（移除多余的 renderCell 避免报错） -->
      <a-table
        :columns="columns"
        :data-source="patientList"
        row-key="id"
        :pagination="false"
        :row-selection="{
          type: 'radio',
          selectedRowKeys: selectedRowKeys,
          onChange: handleRowSelect
        }"
        bordered
        style="margin-top: 16px"
      />

      <!-- 分页控件（固定选项，避免切换后变化） -->
      <div class="pagination-container">
        <a-pagination
          v-model:current="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          @change="handlePageChange"
          @showSizeChange="handleSizeChange"
          show-size-changer
        :size-options="['3', '5', '10', '20']"
        :locale="{ itemsPerPage: '/页' }"
        :show-total="(total: number) => `共 ${total} 条记录`"
        :page-size-options="['3', '5', '10', '20']"
        />
      </div>
    </div>

  <!-- 患者详细信息表单 -->
  <div v-if="selectedPatient" class="patient-detail">
    <a-divider orientation="left">患者详细信息</a-divider>
    <a-form
      layout="vertical"
      :model="selectedPatient"
      :disabled="true"
    >
      <a-row :gutter="24">
        <a-col :span="6">
          <a-form-item label="患者ID">
            <a-input v-model:value="selectedPatient.id" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="患者姓名">
            <a-input v-model:value="selectedPatient.patientName" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="性别">
            <a-input v-model:value="selectedPatient.gender" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="出生日期">
            <a-input v-model:value="selectedPatient.birthday" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="身份证号">
            <a-input v-model:value="selectedPatient.cardNumber" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="住院号">
            <a-input v-model:value="selectedPatient.caseNumber" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="就诊日期">
            <a-input v-model:value="selectedPatient.visitDate" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="支付类型">
            <a-input v-model:value="selectedPatient.paymentType" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="年龄类型">
            <a-input v-model:value="selectedPatient.ageType" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="工作状态">
            <a-input v-model:value="selectedPatient.workStatus" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="家庭地址">
            <a-input v-model:value="selectedPatient.homeAddress" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, watch } from 'vue';
import { useRouter } from 'vue-router';
import {
  SearchOutlined,
  MedicineBoxOutlined,
  ToolOutlined,
  UserOutlined, FileAddOutlined
} from '@ant-design/icons-vue'
import {
  listPatientByPageUsingGet,
  getCurrentPatientUsingGet
} from '@/api/patientRegistrationController';

import type {
  PatientVO,
  PatientRegistration,
  listPatientByPageUsingGETParams
} from '@/api/typings';
import React from 'react'

// 新增：格式化时间为本地时间字符串
const formatToLocalTimeString = (date: Date | string) => {
  if (!date) return '';

  const d = new Date(date);
  // 确保日期对象有效
  if (isNaN(d.getTime())) return '';

  const pad = (num: number) => num.toString().padStart(2, '0');

  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
};

// 路由实例
const router = useRouter();

// 患者列表数据
const patientList = ref<PatientVO[]>([]);

// 选中的患者
const selectedPatient = ref<PatientRegistration | null>(null);

// 选中的行ID（单选框绑定）
const selectedRowKeys = ref<React.Key[]>([]);

// 搜索参数
const searchParams = reactive<Partial<listPatientByPageUsingGETParams>>({
  patientName: ''
});

// 分页参数
const pagination = reactive({
  current: 1,
  size: 5,  // 默认5条/页
  total: 0
});

// 表格列定义 - 修改就诊日期列的显示
const columns = [
  {
    title: '患者ID',
    dataIndex: 'id',
    key: 'id',
    width: 80
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    key: 'patientName',
    width: 120
  },
  {
    title: '性别',
    dataIndex: 'gender',
    key: 'gender',
    width: 80
  },
  {
    title: '住院号',
    dataIndex: 'caseNumber',
    key: 'caseNumber',
    width: 150
  },
  {
    title: '就诊日期',
    dataIndex: 'visitDate',
    key: 'visitDate',
    width: 150,
    customRender: ({ text }: { text: string }) => formatToLocalTimeString(text)
  },
  {
    title: '支付类型',
    dataIndex: 'paymentType',
    key: 'paymentType',
    width: 120
  },
  {
    title: '工作状态',
    dataIndex: 'workStatus',
    key: 'workStatus',
    width: 120
  }
];

// 获取患者列表数据
const fetchPatientList = async () => {
  try {
    const params: listPatientByPageUsingGETParams = {
      current: pagination.current,
      size: pagination.size,
      patientName: searchParams.patientName || undefined
    };

    const response = await listPatientByPageUsingGet(params);

    if (response.data.data) {
      patientList.value = response.data.data.records || [];
      pagination.total = response.data.data.total || 0;
    } else {
      console.error('获取患者列表失败:', response.data.message);
    }
  } catch (error) {
    console.error('获取患者列表时发生错误:', error);
  }
};

// 获取患者详细信息
const fetchPatientDetail = async (patientId: number) => {
  try {
    const response = await getCurrentPatientUsingGet({ patientId });

    if (response.data.data) {
      // 格式化就诊日期
      const patientData = response.data.data;
      if (patientData.visitDate) {
        patientData.visitDate = formatToLocalTimeString(patientData.visitDate);
      }
      selectedPatient.value = patientData;
    } else {
      console.error('获取患者详情失败:', response.data.message);
    }
  } catch (error) {
    console.error('获取患者详情时发生错误:', error);
  }
};

// 页面加载时获取患者列表
onMounted(() => {
  fetchPatientList();
});

// 处理表格行选择（选中时左侧圆圈变蓝）
const handleRowSelect = (selectedRowKeys: React.Key[], selectedRows: PatientVO[]) => {
  selectedRowKeys.values = selectedRowKeys; // 更新选中的行ID

  if (selectedRows.length > 0) {
    const patientId = selectedRows[0].id as number;
    fetchPatientDetail(patientId);
  } else {
    selectedPatient.value = null;
  }
};

// 取消选中功能
const handleCancelSelect = () => {
  selectedRowKeys.value = []; // 清空选中行ID
  selectedPatient.value = null; // 清空选中患者
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchPatientList();
};

// 处理分页页码变化
const handlePageChange = (page: number) => {
  pagination.current = page;
  fetchPatientList();
};

// 处理分页大小变化（固定显示3/5/10/20）
const handleSizeChange = (current: number, size: number) => {
  pagination.current = current;
  pagination.size = size;
  fetchPatientList();
};

// 导航到指定页面
const handleNavigate = (path: string) => {
  if (selectedPatient.value?.id) {
    console.log('准备跳转，选中的患者ID:', selectedPatient.value.id);
    router.push({
      path: `/doctor-station/medical-orders/${path}`, // 与路由配置中的路径完全匹配
      query: {
        patientId: selectedPatient.value.id,
        patientName: selectedPatient.value.patientName
      }
    }).catch(err => {
      console.error('路由跳转失败:', err);
    });
  } else {
    console.log('未选中患者，无法跳转');
  }
};

// 搜索防抖
const debouncedSearch = ref<NodeJS.Timeout | null>(null);
watch(
  () => searchParams.patientName,
  (newVal) => {
    if (debouncedSearch.value) clearTimeout(debouncedSearch.value);
    debouncedSearch.value = setTimeout(handleSearch, 500);
  }
);
</script>

<style scoped>
.patient-select-container {
  padding: 24px;
  background-color: #fff;
  min-height: 100vh;
  box-sizing: border-box;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #1890ff;
}

.nav-buttons {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
}

.table-section {
  margin-bottom: 32px;
}

.search-container {
  margin-bottom: 16px;
}

.pagination-container {
  margin-top: 16px;
  text-align: right;
}

.patient-detail {
  margin-top: 24px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 4px;
}

/* 患者详细信息表单：禁用状态文字黑色 */
.patient-detail :deep(.ant-form-item-control-input-content .ant-input-disabled) {
  color: #000 !important;
  background-color: #fff !important;
}

/* 单选框选中样式增强（确保变蓝） */
:deep(.ant-radio-wrapper-checked .ant-radio-checked .ant-radio-inner) {
  background-color: #1890ff !important;
  border-color: #1890ff !important;
}

/* 取消选中按钮 hover 效果 */
.nav-buttons .ant-btn-text:hover {
  text-decoration: underline;
}
</style>
