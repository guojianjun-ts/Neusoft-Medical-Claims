<template>
  <div class="cost-query-container">
    <!-- 搜索和表格区域 -->
    <div class="search-table-section">
      <!-- 搜索框 -->
      <div class="search-container">
        <a-input-search
          v-model:value="searchParams.name"
          placeholder="输入患者姓名搜索"
          enter-button
          style="width: 400px"
          @search="handleSearch"
        />
      </div>

      <!-- 参保人员表格 -->
      <a-table
        :columns="columns"
        :data-source="tableData.records"
        :pagination="tableData.pagination"
        :loading="loading"
        row-key="id"
        bordered
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" @click="handleSelectPatient(record)">查看费用</a-button>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 费用详情抽屉 -->
    <a-drawer
      v-model:visible="drawerVisible"
      title="患者费用详情"
      width="80%"
      placement="right"
      :mask-closable="false"
    >
      <a-tabs v-model:activeKey="activeTab">
        <a-tab-pane key="drug" tab="药品费用">
          <a-table
            :columns="drugColumns"
            :data-source="drugData.records"
            :pagination="drugData.pagination"
            :loading="drugLoading"
            row-key="drugId"
            bordered
          />
        </a-tab-pane>

        <a-tab-pane key="diagnosis" tab="诊疗项目">
          <a-table
            :columns="diagnosisColumns"
            :data-source="diagnosisData.records"
            :pagination="diagnosisData.pagination"
            :loading="diagnosisLoading"
            row-key="diagnosisId"
            bordered
          />
        </a-tab-pane>

        <a-tab-pane key="medical" tab="医疗服务">
          <a-table
            :columns="medicalColumns"
            :data-source="medicalData.records"
            :pagination="medicalData.pagination"
            :loading="medicalLoading"
            row-key="medicalId"
            bordered
          />
        </a-tab-pane>
      </a-tabs>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import type { TableProps } from 'ant-design-vue';
import {
  listInsuredPatientsUsingGet,
  listDrugCostByPatientIdUsingGet,
  listDiagnosisCostByPatientIdUsingGet,
  listServiceCostByPatientIdUsingGet
} from '@/api/insuranceController';

// 主表格列定义
const columns = [
  {
    title: '患者ID',
    dataIndex: 'id',
    key: 'id',
    width: 100
  },
  {
    title: '住院号',
    dataIndex: 'caseNumber',
    key: 'caseNumber',
    width: 120
  },
  {
    title: '姓名',
    dataIndex: 'patientName',
    key: 'patientName',
    width: 100
  },
  {
    title: '身份证号',
    dataIndex: 'cardNumber',
    key: 'cardNumber',
    width: 180
  },
  {
    title: '性别',
    dataIndex: 'gender',
    key: 'gender',
    width: 80
  },
  {
    title: '操作',
    key: 'action',
    width: 100,
    fixed: 'right'
  }
];

// 药品费用表格列
const drugColumns = [
  {
    title: '药品名称',
    dataIndex: 'drugName',
    key: 'drugName'
  },
  {
    title: '规格',
    dataIndex: 'specification',
    key: 'specification'
  },
  {
    title: '单位',
    dataIndex: 'unit',
    key: 'unit'
  },
  {
    title: '生产厂家',
    dataIndex: 'manufacturer',
    key: 'manufacturer'
  },
  {
    title: '价格(元)',
    dataIndex: 'price',
    key: 'price',
    align: 'right'
  }
];

// 诊疗项目表格列
const diagnosisColumns = [
  {
    title: '诊疗项目名称',
    dataIndex: 'diagnosisName',
    key: 'diagnosisName'
  },
  {
    title: '诊疗项目编码',
    dataIndex: 'diagnosisCode',
    key: 'diagnosisCode'
  },
  {
    title: '除外内容',
    dataIndex: 'excludeContent',
    key: 'excludeContent'
  },
  {
    title: '计价单位',
    dataIndex: 'unit',
    key: 'unit'
  },
  {
    title: '价格(元)',
    dataIndex: 'price',
    key: 'price',
    align: 'right'
  }
];

// 医疗服务表格列
const medicalColumns = [
  {
    title: '医疗服务名称',
    dataIndex: 'serviceName',
    key: 'serviceName'
  },
  {
    title: '医疗服务编码',
    dataIndex: 'serviceCode',
    key: 'serviceCode'
  },
  {
    title: '除外内容',
    dataIndex: 'excludeContent',
    key: 'excludeContent'
  },
  {
    title: '计价单位',
    dataIndex: 'unit',
    key: 'unit'
  },
  {
    title: '价格(元)',
    dataIndex: 'price',
    key: 'price',
    align: 'right'
  }
];

// 搜索参数
const searchParams = reactive({
  name: '',
  current: 1,
  size: 10
});

// 主表格数据
const tableData = reactive({
  records: [] as API.PatientRegistration[],
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条记录`
  }
});

// 费用表格数据
const drugData = reactive({
  records: [] as API.DrugCostVO[],
  pagination: {
    current: 1,
    pageSize: 5,
    total: 0
  }
});

const diagnosisData = reactive({
  records: [] as API.DiagnosisCostVO[],
  pagination: {
    current: 1,
    pageSize: 5,
    total: 0
  }
});

const medicalData = reactive({
  records: [] as API.MedicalServiceCostVO[],
  pagination: {
    current: 1,
    pageSize: 5,
    total: 0
  }
});

// 状态管理
const loading = ref(false);
const drawerVisible = ref(false);
const activeTab = ref('drug');
const selectedPatient = ref<API.PatientRegistration | null>(null);
const drugLoading = ref(false);
const diagnosisLoading = ref(false);
const medicalLoading = ref(false);

// 获取参保人员列表
const fetchInsuredPatients = async () => {
  loading.value = true;
  try {
    const res = await listInsuredPatientsUsingGet({
      current: searchParams.current,
      size: searchParams.size,
      name: searchParams.name
    });

    if (res.data?.data) {
      tableData.records = res.data.data.records || [];
      tableData.pagination.total = res.data.data.total || 0;
    }
  } catch (error) {
    console.error('获取参保人员列表失败:', error);
    message.error('获取参保人员列表失败');
  } finally {
    loading.value = false;
  }
};

// 获取药品费用
const fetchDrugCosts = async () => {
  if (!selectedPatient.value) return;

  drugLoading.value = true;
  try {
    const res = await listDrugCostByPatientIdUsingGet({
      patientId: selectedPatient.value.id!,
      current: drugData.pagination.current,
      size: drugData.pagination.pageSize
    });

    if (res.data?.data) {
      drugData.records = res.data.data.records || [];
      drugData.pagination.total = res.data.data.total || 0;
    }
  } catch (error) {
    console.error('获取药品费用失败:', error);
    message.error('获取药品费用失败');
  } finally {
    drugLoading.value = false;
  }
};

// 获取诊疗项目费用
const fetchDiagnosisCosts = async () => {
  if (!selectedPatient.value) return;

  diagnosisLoading.value = true;
  try {
    const res = await listDiagnosisCostByPatientIdUsingGet({
      patientId: selectedPatient.value.id!,
      current: diagnosisData.pagination.current,
      size: diagnosisData.pagination.pageSize
    });

    if (res.data?.data) {
      diagnosisData.records = res.data.data.records || [];
      diagnosisData.pagination.total = res.data.data.total || 0;
    }
  } catch (error) {
    console.error('获取诊疗项目费用失败:', error);
    message.error('获取诊疗项目费用失败');
  } finally {
    diagnosisLoading.value = false;
  }
};

// 获取医疗服务费用
const fetchMedicalCosts = async () => {
  if (!selectedPatient.value) return;

  medicalLoading.value = true;
  try {
    const res = await listServiceCostByPatientIdUsingGet({
      patientId: selectedPatient.value.id!,
      current: medicalData.pagination.current,
      size: medicalData.pagination.pageSize
    });

    if (res.data?.data) {
      medicalData.records = res.data.data.records || [];
      medicalData.pagination.total = res.data.data.total || 0;
    }
  } catch (error) {
    console.error('获取医疗服务费用失败:', error);
    message.error('获取医疗服务费用失败');
  } finally {
    medicalLoading.value = false;
  }
};

// 搜索处理
const handleSearch = () => {
  searchParams.current = 1;
  fetchInsuredPatients();
};

// 表格分页变化
const handleTableChange: TableProps['onChange'] = (pagination) => {
  searchParams.current = pagination.current!;
  searchParams.size = pagination.pageSize!;
  fetchInsuredPatients();
};

// 选择患者查看费用详情
const handleSelectPatient = (record: API.PatientRegistration) => {
  selectedPatient.value = record;
  drawerVisible.value = true;
  activeTab.value = 'drug';

  // 重置分页并加载数据
  drugData.pagination.current = 1;
  diagnosisData.pagination.current = 1;
  medicalData.pagination.current = 1;

  fetchDrugCosts();
  fetchDiagnosisCosts();
  fetchMedicalCosts();
};

// 初始化加载数据
onMounted(() => {
  fetchInsuredPatients();
});
</script>

<style scoped>
.cost-query-container {
  padding: 20px;
  background-color: #fff;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.search-table-section {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.search-container {
  margin-bottom: 16px;
}

.ant-table {
  flex: 1;
}
</style>
