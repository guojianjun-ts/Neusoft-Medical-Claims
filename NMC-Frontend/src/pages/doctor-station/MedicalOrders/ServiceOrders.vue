<template>
  <div class="service-orders-container">
    <!-- 页面标题 -->
    <div class="page-title">
      医嘱患者：{{ patientName || '未选择患者' }}
    </div>

    <!-- 医疗服务医嘱标题 -->
    <div class="section-title">
      <a-divider orientation="left">医疗服务医嘱</a-divider>
    </div>

    <!-- 搜索和表格区域 -->
    <div class="table-section">
      <!-- 搜索框 -->
      <div class="search-container">
        <a-input
          v-model:value="searchParams.serviceName"
          placeholder="请输入医疗服务名称搜索"
          style="width: 300px"
          allow-clear
          @press-enter="handleSearch"
        >
          <template #suffix>
            <SearchOutlined @click="handleSearch" />
          </template>
        </a-input>
      </div>

      <!-- 医疗服务表格 -->
      <div class="table-wrapper">
        <a-table
          :columns="columns"
          :data-source="serviceList"
          row-key="id"
          :pagination="false"
          bordered
          :scroll="{ x: 1200 }"
          style="margin-top: 16px"
        >
          <!-- 自定义操作列 -->
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'action'">
              <a-button
                v-if="addedServiceIds.has(record.id as number)"
                type="link"
                danger
                @click="handleRemoveService(record.id as number)"
              >
                取消添加
              </a-button>
              <a-button
                v-else
                type="link"
                @click="handleAddService(record.id as number)"
              >
                添加服务
              </a-button>
            </template>
          </template>
        </a-table>
      </div>

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

    <!-- 添加医疗服务表单对话框 -->
    <a-modal
      v-model:visible="formVisible"
      title="添加医疗服务医嘱"
      :confirm-loading="confirmLoading"
      @ok="handleSubmit"
      @cancel="handleCancel"
    >
      <a-form
        :model="serviceForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="医嘱内容" required>
          <a-input v-model:value="serviceForm.doctorOrder" placeholder="请输入医嘱内容" />
        </a-form-item>
        <a-form-item label="服务方法" required>
          <a-select v-model:value="serviceForm.useMethod" placeholder="请选择服务方法">
            <a-select-option value="每日一次">每日一次</a-select-option>
            <a-select-option value="每日两次">每日两次</a-select-option>
            <a-select-option value="每周一次">每周一次</a-select-option>
            <a-select-option value="按需使用">按需使用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="服务时间" required>
          <a-date-picker
            v-model:value="serviceForm.orderTime"
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
import { SearchOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import {
  listMedicalInfoByPageUsingGet,
  checkMedicalExistsUsingGet,
  addMedicalUsingPost,
  deleteMedicalUsingPost
} from '@/api/inpatientMedicalController';

import type {
  InpatientMedicalVO,
  listMedicalInfoByPageUsingGETParams,
  checkMedicalExistsUsingGETParams,
  AddInpatientMedicalRequest
} from '@/api/typings';

// 路由实例
const route = useRoute();

// 从路由参数中获取患者信息
const patientId = computed(() => Number(route.query.patientId));
const patientName = computed(() => route.query.patientName as string || '');

// 医疗服务列表数据
const serviceList = ref<InpatientMedicalVO[]>([]);

// 已添加的医疗服务ID集合
const addedServiceIds = ref<Set<number>>(new Set());

// 搜索参数
const searchParams = reactive<Partial<listMedicalInfoByPageUsingGETParams>>({
  serviceName: ''
});

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,  // 默认10条/页
  total: 0
});

// 表单相关状态
const formVisible = ref(false);
const confirmLoading = ref(false);
const currentServiceId = ref<number | null>(null);

// 表单数据
const serviceForm = reactive<AddInpatientMedicalRequest>({
  medicalId: 0,
  patientId: 0,
  doctorOrder: '',
  useMethod: '',
  orderNumber: 1,
  orderTime: ''
});

// 表格列定义 - 根据InpatientMedicalVO实体类定义
const columns = [
  {
    title: '服务ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '医疗服务名称',
    dataIndex: 'serviceName',
    key: 'serviceName',
    width: 150,
    align: 'center',
    ellipsis: true
  },
  {
    title: '项目编码',
    dataIndex: 'serviceNumber',
    key: 'serviceNumber',
    width: 120,
    align: 'center',
    responsive: ['lg'] // 大屏幕显示
  },
  {
    title: '国家编码',
    dataIndex: 'countryNumber',
    key: 'countryNumber',
    width: 120,
    align: 'center',
    responsive: ['lg'] // 大屏幕显示
  },
  {
    title: '项目说明',
    dataIndex: 'serviceInfo',
    key: 'serviceInfo',
    width: 180,
    align: 'center',
    ellipsis: true
  },
  {
    title: '计价单位',
    dataIndex: 'serviceUnit',
    key: 'serviceUnit',
    width: 100,
    align: 'center'
  },
  {
    title: '价格(元)',
    dataIndex: 'servicePrice',
    key: 'servicePrice',
    width: 100,
    align: 'center',
    customRender: ({ text }: { text: number }) => text?.toFixed(2) || '0.00'
  },
  {
    title: '操作',
    key: 'action',
    width: 120,
    align: 'center',
    fixed: 'right'
  }
];

// 获取医疗服务列表数据
const fetchServiceList = async () => {
  try {
    const params: listMedicalInfoByPageUsingGETParams = {
      current: pagination.current,
      size: pagination.size,
      serviceName: searchParams.serviceName || undefined
    };

    const response = await listMedicalInfoByPageUsingGet(params);

    if (response.data.data) {
      serviceList.value = response.data.data.records || [];
      pagination.total = response.data.data.total || 0;

      // 检查哪些医疗服务已经添加
      await checkAddedServices();
    } else {
      console.error('获取医疗服务列表失败:', response.data.message);
    }
  } catch (error) {
    console.error('获取医疗服务列表时发生错误:', error);
  }
};

// 检查医疗服务是否已添加
const checkAddedServices = async () => {
  if (!patientId.value) return;

  const checks = serviceList.value.map(service => {
    const params: checkMedicalExistsUsingGETParams = {
      medicalId: service.id as number,
      patientId: patientId.value
    };
    return checkMedicalExistsUsingGet(params);
  });

  try {
    const results = await Promise.all(checks);
    addedServiceIds.value = new Set(
      results
        .map((res, index) => res.data.data ? serviceList.value[index].id as number : null)
        .filter(id => id !== null) as number[]
    );
  } catch (error) {
    console.error('检查医疗服务添加状态时发生错误:', error);
  }
};

// 添加医疗服务 - 打开表单对话框
const handleAddService = (serviceId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法添加医疗服务');
    return;
  }

  // 重置表单
  Object.assign(serviceForm, {
    medicalId: serviceId,
    patientId: patientId.value,
    doctorOrder: '',
    useMethod: '',
    orderNumber: 1,
    orderTime: ''
  });

  currentServiceId.value = serviceId;
  formVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  // 表单验证
  if (!serviceForm.doctorOrder || !serviceForm.useMethod ||
    !serviceForm.orderNumber || !serviceForm.orderTime) {
    message.error('请填写所有必填字段');
    return;
  }

  confirmLoading.value = true;

  try {
    const response = await addMedicalUsingPost(serviceForm);

    if (response.data.data) {
      message.success('添加医疗服务成功');
      addedServiceIds.value.add(serviceForm.medicalId as number);
      formVisible.value = false;
      fetchServiceList(); // 刷新列表
    } else {
      message.error(response.data.message || '添加医疗服务失败');
    }
  } catch (error) {
    console.error('添加医疗服务时发生错误:', error);
    message.error('添加医疗服务失败');
  } finally {
    confirmLoading.value = false;
  }
};

// 取消表单
const handleCancel = () => {
  formVisible.value = false;
};

// 移除医疗服务
const handleRemoveService = async (serviceId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法移除医疗服务');
    return;
  }

  try {
    const response = await deleteMedicalUsingPost({
      patientId: patientId.value,
      medicalId: serviceId
    });

    if (response.data.data) {
      message.success('移除医疗服务成功');
      addedServiceIds.value.delete(serviceId);
      fetchServiceList(); // 刷新列表
    } else {
      message.error(response.data.message || '移除医疗服务失败');
    }
  } catch (error) {
    console.error('移除医疗服务时发生错误:', error);
    message.error('移除医疗服务失败');
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchServiceList();
};

// 处理分页页码变化
const handlePageChange = (page: number) => {
  pagination.current = page;
  fetchServiceList();
};

// 处理分页大小变化
const handleSizeChange = (current: number, size: number) => {
  pagination.current = current;
  pagination.size = size;
  fetchServiceList();
};

// 页面加载时获取医疗服务列表
onMounted(() => {
  if (patientId.value) {
    fetchServiceList();
  }
});
</script>

<style scoped>
.service-orders-container {
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

.table-wrapper {
  min-width: 600px;
  overflow-x: auto;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .ant-table-cell {
    padding: 8px !important;
  }
}
</style>
