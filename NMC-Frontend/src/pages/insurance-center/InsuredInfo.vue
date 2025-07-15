<template>
  <div class="insured-info-container">
    <!-- 搜索和表格区域 -->
    <div class="search-table-section">
      <!-- 搜索框 -->
      <div class="search-container">
        <a-input-search
          v-model:value="searchParams.name"
          placeholder="输入患者姓名搜索"
          enter-button
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
            <a-button type="link" @click="handleSelectPatient(record)">查看详情</a-button>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 患者详情抽屉 -->
    <a-drawer
      v-model:visible="drawerVisible"
      title="参保人员详细信息"
      width="40%"
      placement="right"
      :mask-closable="false"
    >
      <a-form
        v-if="selectedPatient"
        :model="selectedPatient"
        layout="vertical"
      >
        <a-form-item label="姓名">
          <a-input v-model:value="selectedPatient.patientName" />
        </a-form-item>

        <a-form-item label="身份证号">
          <a-input v-model:value="selectedPatient.cardNumber" />
        </a-form-item>

        <a-form-item label="性别">
          <a-select v-model:value="selectedPatient.gender">
            <a-select-option value="男">男</a-select-option>
            <a-select-option value="女">女</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="出生日期">
          <a-date-picker
            v-model:value="selectedPatient.birthday"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </a-form-item>

        <a-form-item label="家庭住址">
          <a-input v-model:value="selectedPatient.homeAddress" />
        </a-form-item>

        <a-form-item label="工作状态">
          <a-select v-model:value="selectedPatient.workStatus" placeholder="请选择工作状态">
            <a-select-option value="在职">在职</a-select-option>
            <a-select-option value="退休">退休</a-select-option>
          </a-select>
        </a-form-item>

        <div class="form-actions">
          <a-button type="primary" @click="handleUpdate" :loading="updating">保存修改</a-button>
          <a-button style="margin-left: 10px" @click="drawerVisible = false">取消</a-button>
        </div>
      </a-form>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import type { TableProps } from 'ant-design-vue';
import {
  listInsuredPatientsUsingGet,
  updatePatientInfoUsingPut
} from '@/api/insuranceController';

// 表格列定义
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
    title: '结算类别',
    dataIndex: 'paymentType',
    key: 'paymentType',
    width: 100
  },
  {
    title: '操作',
    key: 'action',
    width: 100,
    fixed: 'right'
  }
];

// 搜索参数
const searchParams = reactive({
  name: '',
  current: 1,
  size: 10
});

// 表格数据
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

// 状态管理
const loading = ref(false);
const drawerVisible = ref(false);
const selectedPatient = ref<API.PatientRegistration | null>(null);
const updating = ref(false);

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

// 选择患者查看详情
const handleSelectPatient = (record: API.PatientRegistration) => {
  selectedPatient.value = { ...record }; // 创建副本避免直接修改
  drawerVisible.value = true;
};

// 更新患者信息
const handleUpdate = async () => {
  if (!selectedPatient.value) return;

  updating.value = true;
  try {
    const res = await updatePatientInfoUsingPut(
      { id: selectedPatient.value.id! },
      selectedPatient.value
    );

    if (res.data?.data) {
      message.success('修改成功');
      fetchInsuredPatients(); // 刷新列表
      drawerVisible.value = false;
    } else {
      message.error(res.data?.message || '修改失败');
    }
  } catch (error) {
    console.error('修改参保人员信息失败:', error);
    message.error('修改参保人员信息失败');
  } finally {
    updating.value = false;
  }
};

// 初始化加载数据
onMounted(() => {
  fetchInsuredPatients();
});
</script>

<style scoped>
.insured-info-container {
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

.form-actions {
  margin-top: 24px;
  text-align: right;
}

.ant-table {
  flex: 1;
}
</style>
