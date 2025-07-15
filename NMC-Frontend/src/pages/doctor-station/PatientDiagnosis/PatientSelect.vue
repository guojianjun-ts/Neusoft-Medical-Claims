<template>
  <div id="patientSelectPage">
    <!-- 页面标题 -->
    <div class="title">
      <FileAddOutlined />
      <span class="text">患者选择</span>
    </div>

    <!-- 操作按钮 -->
    <div style="margin-bottom: 16px; border-bottom: 1px solid #444; height: 80px; display: flex; align-items: center;">
      <a-space>
        <a-button type="primary" @click="addAdmissionDiagnosis">增加入院诊断</a-button>
        <a-button type="primary" @click="addPrimaryDiagnosis">增加主要诊断</a-button>
        <a-button type="primary" @click="addOtherDiagnosis">增加其他诊断</a-button>
      </a-space>
    </div>

    <!-- 患者姓名搜索栏 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="患者姓名">
        <a-input v-model:value="searchParams.patientName" placeholder="输入患者姓名" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <div style="margin-bottom: 16px" />

    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
      :row-key="record => record.id"
      @change="doTableChange"
      bordered
      class="no-wrap-header"
    >
      <template #bodyCell="{ column, record }">
        <!-- 可根据需要添加自定义单元格内容 -->
      </template>
    </a-table>

    <!-- 患者详细信息 -->
    <h2>患者详细信息</h2>
    <div style="background-color: #f5f5f5; padding: 16px; border-radius: 4px;">
      <a-form ref="formRef" :model="selectedPatient" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <!-- 第一行：住院号、姓名、身份证号 -->
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="住院号" name="caseNumber">
              <a-input v-model:value="selectedPatient.caseNumber" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="姓名" name="patientName">
              <a-input v-model:value="selectedPatient.patientName" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="身份证号" name="cardNumber">
              <a-input v-model:value="selectedPatient.cardNumber" disabled />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第二行：年龄、出生日期、性别 -->
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="年龄" name="page">
              <a-input v-model:value="selectedPatient.page" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="出生日期" name="birthday">
              <a-select v-model:value="selectedPatient.birthday" disabled>
                <a-select-option value="2000-01-01">2000-01-01</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="性别" name="gender">
              <a-select v-model:value="selectedPatient.gender" disabled>
                <a-select-option value="男">男</a-select-option>
                <a-select-option value="女">女</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第三行：家庭住址 -->
        <a-row>
          <a-col :span="24">
            <a-form-item label="家庭住址" name="homeAddress">
              <a-input v-model:value="selectedPatient.homeAddress" disabled />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第四行：结算类别、工作状态、入院时间 -->
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="结算类别" name="paymentType">
              <a-select v-model:value="selectedPatient.paymentType" disabled>
                <a-select-option value="类别1">类别1</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工作状态" name="workStatus">
              <a-select v-model:value="selectedPatient.workStatus" disabled>
                <a-select-option value="在职">在职</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="入院时间" name="visitDate">
              <a-input v-model:value="selectedPatient.visitDate" disabled />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { message, type FormInstance } from 'ant-design-vue';
import { FileAddOutlined } from '@ant-design/icons-vue';
import { listPatientByPageUsingGet } from '@/api/patientRegistrationController';
import type { PatientRegistration } from '@/api/typings.d.ts';

// 搜索参数
const searchParams = reactive({
  current: 1,
  pageSize: 10,
  patientName: undefined as string | undefined
});

// 数据列表
const dataList = ref<PatientRegistration[]>([]);
const total = ref(0);
const selectedRowKeys = ref<number[]>([]);
const selectedPatient = ref<PatientRegistration>({});
const formRef = ref<FormInstance>();

// 表格列配置
const columns = [
  {
    title: '序号',
    dataIndex: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '住院号',
    dataIndex: 'caseNumber',
    width: 150
  },
  {
    title: '姓名',
    dataIndex: 'patientName',
    width: 150
  },
  {
    title: '身份证号',
    dataIndex: 'cardNumber',
    width: 200
  },
  {
    title: '性别',
    dataIndex: 'gender',
    width: 80
  },
  {
    title: '工作状态',
    dataIndex: 'workStatus',
    width: 150
  },
  {
    title: '结算类别',
    dataIndex: 'paymentType',
    width: 150
  },
  {
    title: '入院时间',
    dataIndex: 'visitDate',
    width: 150
  }
];

// 分页配置
const pagination = computed(() => ({
  current: searchParams.current,
  pageSize: searchParams.pageSize,
  total: total.value,
  showSizeChanger: true,
  showTotal: total => `共 ${total} 条`,
  pageSizeOptions: ['3','10', '20', '50', '100']
}));

// 获取数据（统一处理分页和搜索）
const fetchData = async () => {
  try {
    const params = {
      current: searchParams.current,
      size: searchParams.pageSize,
      patientName: searchParams.patientName || undefined // 传undefined会被过滤
    };

    const res = await listPatientByPageUsingGet(params);

    if (res.data.data) {
      dataList.value = res.data.data.records || [];
      total.value = res.data.data.total || 0;
    } else {
      message.error(res.data.message || '获取数据失败');
    }
  } catch (error) {
    message.error('获取数据失败');
  }
};

// 搜索
const doSearch = () => {
  searchParams.current = 1;
  fetchData();
};

// 表格变化
const doTableChange = (pag: any) => {
  searchParams.current = pag.current;
  searchParams.pageSize = pag.pageSize;
  fetchData();
};

// 行选择变化
const onSelectChange = (selectedKeys: number[]) => {
  selectedRowKeys.value = selectedKeys;
  if (selectedKeys.length > 0) {
    const selectedRecord = dataList.value.find(record => record.id === selectedKeys[0]);
    if (selectedRecord) {
      selectedPatient.value = { ...selectedRecord };
    }
  } else {
    selectedPatient.value = {};
  }
};

// 增加入院诊断
const addAdmissionDiagnosis = () => {
  // 实现增加入院诊断的逻辑
  console.log('增加入院诊断');
};

// 增加主要诊断
const addPrimaryDiagnosis = () => {
  // 实现增加主要诊断的逻辑
  console.log('增加主要诊断');
};

// 增加其他诊断
const addOtherDiagnosis = () => {
  // 实现增加其他诊断的逻辑
  console.log('增加其他诊断');
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
#patientSelectPage {
  padding: 16px;
}
.title{
  display: flex;
  align-items: center;
  height: 80px;
  border-bottom: 1px solid #444;
  font-size: 28px;
}
.title svg {
  margin-right: 8px;
  font-size: 22px;
}
/* 新增样式：使标签左对齐 */
.ant-form-item-label {
  text-align: left !important;
}
</style>
