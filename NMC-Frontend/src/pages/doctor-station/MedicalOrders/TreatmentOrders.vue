<template>
  <div class="treatment-orders-container">
    <!-- 页面标题 -->
    <div class="page-title">
      <FileAddOutlined />
      医嘱患者：{{ patientName || '未选择患者' }}
      <a-button type="primary" @click="goBack">
        <template #icon><arrow-left-outlined /></template>
        返回
      </a-button>
    </div>

    <!-- 治疗医嘱标题 -->
    <div class="section-title">
      <a-divider orientation="left">治疗医嘱</a-divider>
    </div>

    <!-- 搜索和表格区域 -->
    <div class="table-section">
      <!-- 搜索框 -->
      <div class="search-container">
        <a-input
          v-model:value="searchParams.treatmentName"
          placeholder="请输入治疗项目名称搜索"
          style="width: 300px"
          allow-clear
          @press-enter="handleSearch"
        >
          <template #suffix>
            <SearchOutlined @click="handleSearch" />
          </template>
        </a-input>
      </div>

      <!-- 治疗项目表格 -->
      <a-table
        :columns="columns"
        :data-source="treatmentList"
        row-key="id"
        :pagination="false"
        bordered
        style="margin-top: 16px"
      >
        <!-- 自定义操作列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button
              v-if="addedTreatmentIds.has(record.id as number)"
              type="link"
              danger
              @click="handleRemoveTreatment(record.id as number)"
            >
              取消添加
            </a-button>
            <a-button
              v-else
              type="link"
              @click="handleAddTreatment(record.id as number)"
            >
              添加治疗
            </a-button>
          </template>
        </template>
      </a-table>

      <!-- 分页控件 -->
      <div class="pagination-container">
        <a-pagination
          v-model:current="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          @change="handlePageChange"
          @showSizeChange="handleSizeChange"
          show-size-changer
          :size-options="['5', '10', '20', '50']"
          :locale="{ itemsPerPage: '/页' }"
          :show-total="(total: number) => `共 ${total} 条记录`"
        />
      </div>
    </div>

    <!-- 添加治疗表单对话框 -->
    <a-modal
      v-model:visible="formVisible"
      title="添加治疗医嘱"
      :confirm-loading="confirmLoading"
      @ok="handleSubmit"
      @cancel="handleCancel"
    >
      <a-form
        :model="treatmentForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="医嘱内容" required>
          <a-input v-model:value="treatmentForm.doctorOrder" placeholder="请输入医嘱内容"  />
        </a-form-item>
        <a-form-item label="治疗方法" required>
          <a-select v-model:value="treatmentForm.useMethod" placeholder="请选择治疗方法">
            <a-select-option value="每日一次">每日一次</a-select-option>
            <a-select-option value="每日两次">每日两次</a-select-option>
            <a-select-option value="每周一次">每周一次</a-select-option>
            <a-select-option value="按需使用">按需使用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="治疗时间" required>
          <a-date-picker
            v-model:value="treatmentForm.orderTime"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { ArrowLeftOutlined,FileAddOutlined, SearchOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue';
import {
  listDiagnosisInfoByPageUsingGet,
  checkDiagnosisExistsUsingGet,
  addDiagnosisUsingPost,
  deleteDiagnosisUsingPost
} from '@/api/inpatientDiagnosisController';

import type {
  DiagnosisTreatment,
  listDiagnosisInfoByPageUsingGETParams,
  checkDiagnosisExistsUsingGETParams,
  AddInpatientDiagnosisRequest
} from '@/api/typings';

// 路由实例
const route = useRoute();

// 添加路由
const router = useRouter();
// 返回上一页
const goBack = () => {
  router.go(-1)
}
// 从路由参数中获取患者信息
const patientId = computed(() => Number(route.query.patientId));
const patientName = computed(() => route.query.patientName as string || '');

// 治疗项目列表数据
const treatmentList = ref<DiagnosisTreatment[]>([]);

// 已添加的治疗项目ID集合
const addedTreatmentIds = ref<Set<number>>(new Set());

// 搜索参数
const searchParams = reactive<Partial<listDiagnosisInfoByPageUsingGETParams>>({
  treatmentName: ''
});

// 分页参数
const pagination = reactive({
  current: 1,
  size: 5,  // 默认5条/页
  total: 0
});

// 表单相关状态
const formVisible = ref(false);
const confirmLoading = ref(false);
const currentTreatmentId = ref<number | null>(null);

// 表单数据
const treatmentForm = reactive<AddInpatientDiagnosisRequest>({
  diagnosisId: 0,
  patientId: 0,
  doctorOrder: '',
  useMethod: '',
  orderTime: ''
});

// 表格列定义
const columns = [
  {
    title: '项目ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '诊疗项目名称',
    dataIndex: 'treatmentName',
    key: 'treatmentName',
    width: 150,
    align: 'center'
  },
  {
    title: '项目编码',
    dataIndex: 'treatmentNumber',
    key: 'treatmentNumber',
    width: 120,
    align: 'center'
  },
  {
    title: '国家编码',
    dataIndex: 'countryNumber',
    key: 'countryNumber',
    width: 120,
    align: 'center'
  },
  {
    title: '项目说明',
    dataIndex: 'treatmentInfo',
    key: 'treatmentInfo',
    width: 200,
    align: 'center',
    ellipsis: true ,
    responsive: ['ld']
  },
  {
    title: '除外内容',
    dataIndex: 'treatmentExclude',
    key: 'treatmentExclude',
    width: 150,
    align: 'center',
    ellipsis: true,
    responsive: ['ld']
  },
  {
    title: '计价单位',
    dataIndex: 'treatmentUnit',
    key: 'treatmentUnit',
    width: 100,
    align: 'center'
  },
  {
    title: '价格(元)',
    dataIndex: 'treatmentPrice',
    key: 'treatmentPrice',
    width: 100,
    align: 'center',
    customRender: ({ text }: { text: number }) => text?.toFixed(2) || '0.00'
  },
  {
    title: '操作',
    key: 'action',
    width: 120,
    align: 'center',
    fixed: 'right' // 固定操作列
  }
];

// 获取治疗项目列表数据
const fetchTreatmentList = async () => {
  try {
    const params: listDiagnosisInfoByPageUsingGETParams = {
      current: pagination.current,
      size: pagination.size,
      treatmentName: searchParams.treatmentName || undefined
    };

    const response = await listDiagnosisInfoByPageUsingGet(params);

    if (response.data.data) {
      treatmentList.value = response.data.data.records || [];
      pagination.total = response.data.data.total || 0;

      // 检查哪些治疗项目已经添加
      await checkAddedTreatments();
    } else {
      console.error('获取治疗项目列表失败:', response.data.message);
    }
  } catch (error) {
    console.error('获取治疗项目列表时发生错误:', error);
  }
};

// 检查治疗项目是否已添加
const checkAddedTreatments = async () => {
  if (!patientId.value) return;

  const checks = treatmentList.value.map(treatment => {
    const params: checkDiagnosisExistsUsingGETParams = {
      diagnosisId: treatment.id as number,
      patientId: patientId.value
    };
    return checkDiagnosisExistsUsingGet(params);
  });

  try {
    const results = await Promise.all(checks);
    addedTreatmentIds.value = new Set(
      results
        .map((res, index) => res.data.data ? treatmentList.value[index].id as number : null)
        .filter(id => id !== null) as number[]
    );
  } catch (error) {
    console.error('检查治疗项目添加状态时发生错误:', error);
  }
};

// 添加治疗项目 - 打开表单对话框
const handleAddTreatment = (treatmentId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法添加治疗项目');
    return;
  }

  // 重置表单
  Object.assign(treatmentForm, {
    diagnosisId: treatmentId,
    patientId: patientId.value,
    doctorOrder: '',
    useMethod: '',
    orderTime: ''
  });

  currentTreatmentId.value = treatmentId;
  formVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  // 表单验证
  if (!treatmentForm.doctorOrder || !treatmentForm.useMethod || !treatmentForm.orderTime) {
    message.error('请填写所有必填字段');
    return;
  }

  confirmLoading.value = true;

  try {
    const response = await addDiagnosisUsingPost(treatmentForm);

    if (response.data.data) {
      message.success('添加治疗项目成功');
      addedTreatmentIds.value.add(treatmentForm.diagnosisId as number);
      formVisible.value = false;
      fetchTreatmentList(); // 刷新列表
    } else {
      message.error(response.data.message || '添加治疗项目失败');
    }
  } catch (error) {
    console.error('添加治疗项目时发生错误:', error);
    message.error('添加治疗项目失败');
  } finally {
    confirmLoading.value = false;
  }
};

// 取消表单
const handleCancel = () => {
  formVisible.value = false;
};

// 移除治疗项目
const handleRemoveTreatment = async (treatmentId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法移除治疗项目');
    return;
  }

  try {
    const response = await deleteDiagnosisUsingPost({
      patientId: patientId.value,
      diagnosisId: treatmentId
    });

    if (response.data.data) {
      message.success('移除治疗项目成功');
      addedTreatmentIds.value.delete(treatmentId);
      fetchTreatmentList(); // 刷新列表
    } else {
      message.error(response.data.message || '移除治疗项目失败');
    }
  } catch (error) {
    console.error('移除治疗项目时发生错误:', error);
    message.error('移除治疗项目失败');
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchTreatmentList();
};

// 处理分页页码变化
const handlePageChange = (page: number) => {
  pagination.current = page;
  fetchTreatmentList();
};

// 处理分页大小变化
const handleSizeChange = (current: number, size: number) => {
  pagination.current = current;
  pagination.size = size;
  fetchTreatmentList();
};

// 页面加载时获取治疗项目列表
onMounted(() => {
  if (patientId.value) {
    fetchTreatmentList();
  }
});
</script>

<style scoped>
.treatment-orders-container {
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

.section-title {
  margin-bottom: 24px;
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
</style>
