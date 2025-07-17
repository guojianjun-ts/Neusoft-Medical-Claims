<template>
  <div class="drug-orders-container">
    <!-- 页面标题 -->
    <div class="page-title">
      <FileAddOutlined />
      医嘱患者：{{ patientName || '未选择患者' }}
      <a-button type="primary" @click="goBack">
        <template #icon><arrow-left-outlined /></template>
        返回
      </a-button>
    </div>

    <!-- 药品处方医嘱标题 -->
    <div class="section-title">
      <a-divider orientation="left">药品处方医嘱</a-divider>
    </div>

    <!-- 搜索和表格区域 -->
    <div class="table-section">
      <!-- 搜索框 -->
      <div class="search-container">
        <a-input
          v-model:value="searchParams.chineseName"
          placeholder="请输入药品名称搜索"
          style="width: 300px"
          allow-clear
          @press-enter="handleSearch"
        >
          <template #suffix>
            <SearchOutlined @click="handleSearch" />
          </template>
        </a-input>
      </div>

      <!-- 药品表格 -->
      <a-table
        :columns="columns"
        :data-source="drugList"
        row-key="id"
        :pagination="false"
        bordered
        style="margin-top: 16px"
      >
        <!-- 自定义操作列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button
              v-if="addedDrugIds.has(record.id as number)"
              type="link"
              danger
              @click="handleRemoveDrug(record.id as number)"
            >
              取消添加
            </a-button>
            <a-button
              v-else
              type="link"
              @click="handleAddDrug(record.id as number)"
            >
              添加药品
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

    <!-- 添加药品表单对话框 -->
    <a-modal
      v-model:visible="formVisible"
      title="添加药品医嘱"
      :confirm-loading="confirmLoading"
      @ok="handleSubmit"
      @cancel="handleCancel"
    >
      <a-form
        :model="drugForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="医嘱内容">
          <a-input v-model:value="drugForm.doctorOrder" placeholder="请输入医嘱内容" />
        </a-form-item>
        <a-form-item label="药品用法" required>
          <a-select v-model:value="drugForm.useMethod" placeholder="请选择药品用法">
            <a-select-option value="口服">口服</a-select-option>
            <a-select-option value="外用">外用</a-select-option>
            <a-select-option value="静脉注射">静脉注射</a-select-option>
            <a-select-option value="肌肉注射">肌肉注射</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="用药频率" required>
          <a-input-number
            v-model:value="drugForm.orderNumber"
            :min="1"
            placeholder="请输入用药频率"
          />
        </a-form-item>
        <!-- 修改日期选择器部分 -->
        <a-form-item label="起始时间" required>
          <a-date-picker
            v-model:value="drugForm.startTime"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="结束时间" required>
          <a-date-picker
            v-model:value="drugForm.endTime"
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
import { useRouter } from 'vue-router'
import { ArrowLeftOutlined, FileAddOutlined, SearchOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue';
import {
  listDrugInfoByPageUsingGet1,
  checkDrugExistsUsingGet,
  addDrugUsingPost,
  deleteDrugUsingPost
} from '@/api/inpatientDrugController';

import type {
  DrugInfo,
  listDrugInfoByPageUsingGET1Params,
  checkDrugExistsUsingGETParams,
  AddInpatientDrugRequest
} from '@/api/typings.d.ts';

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

// 药品列表数据
const drugList = ref<DrugInfo[]>([]);

// 已添加的药品ID集合
const addedDrugIds = ref<Set<number>>(new Set());

// 搜索参数
const searchParams = reactive<Partial<listDrugInfoByPageUsingGET1Params>>({
  chineseName: ''
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
const currentDrugId = ref<number | null>(null);

// 表单数据
const drugForm = reactive<AddInpatientDrugRequest>({
  drugId: 0,
  patientId: 0,
  doctorOrder: '',
  useMethod: '',
  orderNumber: 1,
  startTime: '',
  endTime: ''
});

// 表格列定义
const columns = [
  {
    title: '药品ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '药品处方名称',
    dataIndex: 'chinaName',
    key: 'chinaName',
    width: 150,
    align: 'center'
  },
  {
    title: '规格',
    dataIndex: 'specifications',
    key: 'specifications',
    width: 120,
    align: 'center'
  },
  {
    title: '生产厂家',
    dataIndex: 'drugManufacturer',
    key: 'drugManufacturer',
    width: 180,
    align: 'center'
  },
  {
    title: '价格',
    dataIndex: 'drugPrice',
    key: 'drugPrice',
    width: 100,
    align: 'center'
  },
  {
    title: '操作',
    key: 'action',
    width: 120,
    align: 'center'
  }
];


// 获取药品列表数据
const fetchDrugList = async () => {
  try {
    const params: listDrugInfoByPageUsingGET1Params = {
      current: pagination.current,
      size: pagination.size,
      chineseName: searchParams.chineseName || undefined
    };

    const response = await listDrugInfoByPageUsingGet1(params);

    if (response.data.data) {
      drugList.value = response.data.data.records || [];
      pagination.total = response.data.data.total || 0;

      // 检查哪些药品已经添加
      await checkAddedDrugs();
    } else {
      console.error('获取药品列表失败:', response.data.message);
    }
  } catch (error) {
    console.error('获取药品列表时发生错误:', error);
  }
};

// 检查药品是否已添加
const checkAddedDrugs = async () => {
  if (!patientId.value) return;

  const checks = drugList.value.map(drug => {
    const params: checkDrugExistsUsingGETParams = {
      drugId: drug.id as number,
      patientId: patientId.value
    };
    return checkDrugExistsUsingGet(params);
  });

  try {
    const results = await Promise.all(checks);
    addedDrugIds.value = new Set(
      results
        .map((res, index) => res.data.data ? drugList.value[index].id as number : null)
        .filter(id => id !== null) as number[]
    );
  } catch (error) {
    console.error('检查药品添加状态时发生错误:', error);
  }
};

// 添加药品 - 打开表单对话框
const handleAddDrug = (drugId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法添加药品');
    return;
  }

  // 重置表单
  Object.assign(drugForm, {
    drugId,
    patientId: patientId.value,
    doctorOrder: '',
    useMethod: '',
    orderNumber: 1,
    startTime: '',
    endTime: ''
  });

  currentDrugId.value = drugId;
  formVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  // 表单验证
  if (!drugForm.doctorOrder || !drugForm.useMethod || !drugForm.orderNumber ||
    !drugForm.startTime || !drugForm.endTime) {
    message.error('请填写所有必填字段');
    return;
  }

  if (new Date(drugForm.endTime) <= new Date(drugForm.startTime)) {
    message.error('结束时间必须晚于起始时间');
    return;
  }

  confirmLoading.value = true;

  try {
    const response = await addDrugUsingPost(drugForm);

    if (response.data.data) {
      message.success('添加药品成功');
      addedDrugIds.value.add(drugForm.drugId);
      formVisible.value = false;
      fetchDrugList(); // 刷新列表
    } else {
      message.error(response.data.message || '添加药品失败');
    }
  } catch (error) {
    console.error('添加药品时发生错误:', error);
    message.error('添加药品失败');
  } finally {
    confirmLoading.value = false;
  }
};

// 取消表单
const handleCancel = () => {
  formVisible.value = false;
};

// 移除药品
const handleRemoveDrug = async (drugId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法移除药品');
    return;
  }

  try {
    const response = await deleteDrugUsingPost({
      patientId: patientId.value,
      drugId: drugId
    });

    if (response.data.data) {
      message.success('移除药品成功');
      addedDrugIds.value.delete(drugId);
      fetchDrugList(); // 刷新列表
    } else {
      message.error(response.data.message || '移除药品失败');
    }
  } catch (error) {
    console.error('移除药品时发生错误:', error);
    message.error('移除药品失败');
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchDrugList();
};

// 处理分页页码变化
const handlePageChange = (page: number) => {
  pagination.current = page;
  fetchDrugList();
};

// 处理分页大小变化
const handleSizeChange = (current: number, size: number) => {
  pagination.current = current;
  pagination.size = size;
  fetchDrugList();
};

// 页面加载时获取药品列表
onMounted(() => {
  if (patientId.value) {
    fetchDrugList();
  }
});
</script>

<style scoped>
.drug-orders-container {
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
