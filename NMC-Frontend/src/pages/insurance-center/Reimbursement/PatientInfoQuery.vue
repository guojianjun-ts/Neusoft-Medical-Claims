<template>
  <div class="patient-reimbursement-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2><DollarOutlined /> 参保人员费用报销</h2>
    </div>

    <!-- 搜索区域 -->
    <div class="search-container">
      <a-input-search
        v-model:value="searchParams.patientName"
        placeholder="请输入参保人员姓名"
        enter-button="搜索"
        style="width: 400px"
        @search="handleSearch"
      />
    </div>

    <!-- 参保人员表格 -->
    <div class="table-container">
      <a-table
        :columns="columns"
        :data-source="patientList"
        :pagination="pagination"
        :loading="loading"
        row-key="id"
        bordered
        @change="handleTableChange"
      >
        <!-- 序号列 -->
        <template #index="{ index }">
          {{ (pagination.current - 1) * pagination.pageSize + index + 1 }}
        </template>

        <!-- 操作列 -->
        <template #action="{ record }">
          <a-space>
            <a-button
              type="link"
              @click="handleViewDetail(record.id)"
              :disabled="!record.id"
            >
              费用详情
            </a-button>
            <a-button
              type="primary"
              @click="handleReimbursement(record.id)"
              :disabled="!record.id"
            >
              立即报销
            </a-button>
          </a-space>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { DollarOutlined } from '@ant-design/icons-vue';
import { listInsuredPatientsUsingGet } from '@/api/insuranceController';
import type { PatientRegistration } from '@/api/typings.d.ts';
import { message } from 'ant-design-vue';

const router = useRouter();

// 搜索参数
const searchParams = reactive({
  patientName: '',
  current: 1,
  size: 10
});

// 分页参数
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  pageSizeOptions: ['5', '10', '20', '50'],
  showTotal: (total: number) => `共 ${total} 条记录`
});

// 表格列定义
const columns = [
  {
    title: '序号',
    dataIndex: 'index',
    key: 'index',
    width: 80,
    align: 'center',
    slots: { customRender: 'index' }
  },
  {
    title: '住院号',
    dataIndex: 'caseNumber',
    key: 'caseNumber',
    width: 120,
    align: 'center'
  },
  {
    title: '姓名',
    dataIndex: 'patientName',
    key: 'patientName',
    width: 100,
    align: 'center'
  },
  {
    title: '性别',
    dataIndex: 'gender',
    key: 'gender',
    width: 80,
    align: 'center'
  },
  {
    title: '身份证号',
    dataIndex: 'cardNumber',
    key: 'cardNumber',
    width: 180,
    align: 'center'
  },
  {
    title: '家庭住址',
    dataIndex: 'homeAddress',
    key: 'homeAddress',
    width: 200,
    ellipsis: true
  },
  {
    title: '入院日期',
    dataIndex: 'visitDate',
    key: 'visitDate',
    width: 120,
    align: 'center'
  },
  {
    title: '工作状态',
    dataIndex: 'workStatus',
    key: 'workStatus',
    width: 120,
    align: 'center'
  },
  {
    title: '结算类别',
    dataIndex: 'paymentType',
    key: 'paymentType',
    width: 120,
    align: 'center'
  },
  {
    title: '操作',
    key: 'action',
    width: 250,
    align: 'center',
    fixed: 'right',
    slots: { customRender: 'action' }
  }
];

// 参保人员列表数据
const patientList = ref<PatientRegistration[]>([]);
const loading = ref(false);

// 获取参保人员列表
const fetchPatientList = async () => {
  try {
    loading.value = true;
    const response = await listInsuredPatientsUsingGet({
      current: searchParams.current,
      size: searchParams.size,
      name: searchParams.patientName || undefined
    });

    if (response.data?.data) {
      patientList.value = response.data.data.records || [];
      pagination.total = response.data.data.total || 0;
    }
  } catch (error) {
    console.error('获取参保人员列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchPatientList();
};

// 处理分页变化
const handleTableChange = (pag: any) => {
  searchParams.current = pag.current;
  searchParams.size = pag.pageSize;
  fetchPatientList();
};

// 查看详情
const handleViewDetail = (patientId: number) => {
  router.push({
    path: '/insurance-center/cost-details',
    query: { patientId }
  });
};

// 费用报销
const handleReimbursement = (patientId: number) => {
  router.push({
    path: '/insurance-center/reimbursement-details',
    query: { patientId }
  });
};

// 页面加载时获取数据
onMounted(() => {
  fetchPatientList();
});
</script>

<style scoped>
.patient-reimbursement-container {
  padding: 24px;
  background-color: #fff;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  align-items: center;
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

.search-container {
  margin-bottom: 16px;
}

.table-container {
  margin-top: 16px;
}
</style>
