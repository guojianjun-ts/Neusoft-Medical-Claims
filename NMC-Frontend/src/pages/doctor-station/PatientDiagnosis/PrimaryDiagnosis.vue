<template>
  <div class="drug-orders-container">
    <!-- 页面标题 -->
    <div class="page-title">
      <FileAddOutlined />
      主要诊断患者：{{ patientName || '未选择患者' }}
    </div>

    <!-- 药品处方医嘱标题 -->
    <div class="section-title">
      <a-divider orientation="left">主要诊断疾病</a-divider>
    </div>

    <!-- 搜索和表格区域 -->
    <div class="table-section">
      <!-- 搜索框 -->
      <div class="search-container">
        <a-input
          v-model:value="searchParams.chineseName"
          placeholder="请输入疾病名称搜索"
          style="width: 300px"
          allow-clear
          @press-enter="handleSearch"
        >
          <template #suffix>
            <SearchOutlined @click="handleSearch" />
          </template>
        </a-input>
      </div>

      <!-- 疾病表格 -->
      <a-table
        :columns="columns"
        :data-source="diseaseList"
        row-key="id"
        :pagination="false"
        bordered
        style="margin-top: 16px"
      >
        <!-- 自定义操作列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button
              v-if="addedDiseaseIds.has(record.id as number)"
              type="link"
              danger
              @click="handleRemoveDisease(record.id as number)"
            >
              取消添加
            </a-button>
            <a-button
              v-else
              type="link"
              @click="handleAddDisease(record.id as number)"
            >
              添加疾病
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue';
import { useRoute } from 'vue-router';
import { FileAddOutlined, SearchOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue';
import {
  listDiseaseByPageUsingGet,
  listDiseaseInfoByPageUsingGet,
  checkDiseaseExistsUsingGet,
  addDiseaseUsingPost,
  deleteDiseaseUsingPost
} from '@/api/inpatientDiseaseController';

import type {
  DiseaseInfo,
  listDiseaseInfoByPageUsingGETParams,
  checkDiseaseExistsUsingGETParams,
  AddInpatientDiseaseRequest
} from '@/api/typings.d.ts';

// 路由实例
const route = useRoute();

// 从路由参数中获取疾病信息
const patientId = computed(() => Number(route.query.patientId));
const patientName = computed(() => route.query.patientName as string || '');

// 疾病列表数据
const diseaseList = ref<DiseaseInfo[]>([]);

// 已添加的疾病ID集合
const addedDiseaseIds = ref<Set<number>>(new Set());

// 搜索参数
const searchParams = reactive<Partial<listDiseaseInfoByPageUsingGETParams>>({
  chineseName: ''
});

// 分页参数
const pagination = reactive({
  current: 1,
  size: 5,  // 默认5条/页
  total: 0
});

// 表格列定义
const columns = [
  {
    title: '疾病ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '疾病编码',
    dataIndex: 'diseaseCode',
    key: 'diseaseCode',
    width: 150,
    align: 'center'
  },
  {
    title: '疾病名称',
    dataIndex: 'diseaseName',
    key: 'diseaseName',
    width: 120,
    align: 'center'
  },
  {
    title: '国际疾病ICD编码',
    dataIndex: 'diseaseICD',
    key: 'diseaseICD',
    width: 180,
    align: 'center'
  },
  {
    title: '疾病类型',
    dataIndex: 'diseaseCategory',
    key: 'diseaseCategory',
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

// 获取疾病列表数据
const fetchDiseaseList = async () => {
  try {
    const params:listDiseaseInfoByPageUsingGETParams= {
      current: pagination.current,
      size: pagination.size,
      chineseName: searchParams.chineseName || undefined
    };

    const response = await listDiseaseInfoByPageUsingGet(params);

    if (response.data.data) {
      diseaseList.value = response.data.data.records || [];
      pagination.total = response.data.data.total || 0;

      // 检查哪些疾病已经添加
      await checkAddedDisease();
    } else {
      console.error('获取疾病列表失败:', response.data.message);
    }
  } catch (error) {
    console.error('获取疾病列表时发生错误:', error);
  }
};

// 检查疾病是否已添加
const checkAddedDisease = async () => {
  if (!patientId.value) return;

  const checks = diseaseList.value.map(disease => {
    const params: checkDiseaseExistsUsingGETParams = {
      diseaseId: disease.id as number,
      patientId: patientId.value,
      diseaseType: 2 // 主要诊断
    };
    return checkDiseaseExistsUsingGet(params);
  });

  try {
    const results = await Promise.all(checks);
    addedDiseaseIds.value = new Set(
      results
        .map((res, index) => res.data.data ? diseaseList.value[index].id as number : null)
        .filter(id => id !== null) as number[]
    );
  } catch (error) {
    console.error('检查疾病添加状态时发生错误:', error);
  }
};

// 添加疾病
const handleAddDisease = async (diseaseId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法添加疾病');
    return;
  }

  try {
    const requestData: AddInpatientDiseaseRequest = {
      diseaseId,
      patientId: patientId.value,
      diseaseType: 2 // 主要诊断
    };

    const response = await addDiseaseUsingPost(requestData);

    if (response.data.data) {
      message.success('添加疾病成功');
      addedDiseaseIds.value.add(diseaseId);
      fetchDiseaseList(); // 刷新列表
    } else {
      message.error(response.data.message || '添加疾病失败');
    }
  } catch (error) {
    console.error('添加疾病时发生错误:', error);
    message.error('添加疾病失败');
  }
};

// 移除疾病
const handleRemoveDisease = async (diseaseId: number) => {
  if (!patientId.value) {
    message.error('未选择患者，无法移除疾病');
    return;
  }

  try {
    const response = await deleteDiseaseUsingPost({
      id: diseaseId
    });

    if (response.data.data) {
      message.success('移除疾病成功');
      addedDiseaseIds.value.delete(diseaseId);
      fetchDiseaseList(); // 刷新列表
    } else {
      message.error(response.data.message || '移除疾病失败');
    }
  } catch (error) {
    console.error('移除疾病时发生错误:', error);
    message.error('移除疾病失败');
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchDiseaseList();
};

// 处理分页页码变化
const handlePageChange = (page: number) => {
  pagination.current = page;
  fetchDiseaseList();
};

// 处理分页大小变化
const handleSizeChange = (current: number, size: number) => {
  pagination.current = current;
  pagination.size = size;
  fetchDiseaseList();
};

// 页面加载时获取疾病列表
onMounted(() => {
  if (patientId.value) {
    fetchDiseaseList();
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
