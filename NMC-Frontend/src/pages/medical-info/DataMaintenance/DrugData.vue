<template>
  <div class="drug-info-container">
    <!-- 搜索区域 -->
    <div class="search-bar">
      <a-input-search
        v-model:value="searchText"
        placeholder="输入药品名称搜索"
        enter-button="搜索"
        style="width: 400px"
        @search="handleSearch"
      />
    </div>

    <a-divider style="margin: 12px 0" />

    <!-- 操作按钮区域 -->
    <div class="action-buttons">
      <a-space>
        <a-button type="primary" @click="showAddModal">
          <template #icon><plus-outlined /></template>
          新增药品
        </a-button>
        <a-button
          danger
          @click="handleBatchDelete"
          :disabled="!selectedRowKeys.length"
        >
          <template #icon><delete-outlined /></template>
          删除选中
        </a-button>
      </a-space>
    </div>

    <!-- 数据表格 -->
    <a-table
      :columns="columns"
      :data-source="dataSource"
      :row-key="record => record.id"
      :pagination="pagination"
      :loading="loading"
      :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
      @change="handleTableChange"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-space>
            <a-button size="small" @click="handleEdit(record)">修改</a-button>
            <a-button size="small" danger @click="handleDelete(record.id)">删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 新增/编辑药品模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      width="800px"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        :model="currentDrug"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        ref="drugFormRef"
      >
        <a-form-item label="药品类型" name="insuranceType" :rules="[{ required: true, message: '请选择药品类型' }]">
          <a-select v-model:value="currentDrug.insuranceType" placeholder="请选择药品类型">
            <a-select-option value="甲类">甲类</a-select-option>
            <a-select-option value="乙类">乙类</a-select-option>
            <a-select-option value="丙类">丙类</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="药品名称" name="chinaName" :rules="[{ required: true, message: '请输入药品名称' }]">
          <a-input v-model:value="currentDrug.chinaName" placeholder="请输入药品名称" />
        </a-form-item>
        <a-form-item label="品名" name="goodsName">
          <a-input v-model:value="currentDrug.goodsName" placeholder="请输入品名" />
        </a-form-item>
        <a-form-item label="规格" name="specifications" :rules="[{ required: true, message: '请输入规格' }]">
          <a-input v-model:value="currentDrug.specifications" placeholder="请输入规格" />
        </a-form-item>
        <a-form-item label="单位" name="drugUnit" :rules="[{ required: true, message: '请选择单位' }]">
          <a-select v-model:value="currentDrug.drugUnit" placeholder="请选择单位">
            <a-select-option value="盒">盒</a-select-option>
            <a-select-option value="瓶">瓶</a-select-option>
            <a-select-option value="罐">罐</a-select-option>
            <a-select-option value="袋">袋</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="价格" name="drugPrice" :rules="[{ required: true, message: '请输入价格' }]">
          <a-input-number
            v-model:value="currentDrug.drugPrice"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="请输入价格"
          />
        </a-form-item>
        <a-form-item label="生产厂家" name="drugManufacturer" :rules="[{ required: true, message: '请输入生产厂家' }]">
          <a-input v-model:value="currentDrug.drugManufacturer" placeholder="请输入生产厂家" />
        </a-form-item>
        <a-form-item label="备注" name="remarks">
          <a-textarea v-model:value="currentDrug.remarks" placeholder="请输入备注" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {
  PlusOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue'
import {
  listDrugInfoByPageUsingGet,
  addDrugInfoUsingPost,
  updateDrugInfoUsingPut,
  deleteDrugInfoUsingDelete,
  deleteDrugInfoByIdsUsingDelete
} from '@/api/drugInfoController'

// 表格列配置
const columns = [
  {
    title: '序号',
    dataIndex: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '药品类型',
    dataIndex: 'insuranceType',
    width: 120,
    filters: [
      { text: '甲类', value: '甲类' },
      { text: '乙类', value: '乙类' },
      { text: '丙类', value: '丙类' }
    ],
    onFilter: (value, record) => record.insuranceType.includes(value)
  },
  {
    title: '药品名称',
    dataIndex: 'chinaName',
    width: 150
  },
  {
    title: '品名',
    dataIndex: 'goodsName',
    width: 150
  },
  {
    title: '规格',
    dataIndex: 'specifications',
    width: 200
  },
  {
    title: '单位',
    dataIndex: 'drugUnit',
    width: 100
  },
  {
    title: '价格',
    dataIndex: 'drugPrice',
    width: 120,
    sorter: (a, b) => a.drugPrice - b.drugPrice
  },
  {
    title: '生产厂家',
    dataIndex: 'drugManufacturer',
    width: 250
  },
  {
    title: '操作',
    key: 'operation',
    width: 150,
    align: 'center'
  }
]

// 响应式数据
const dataSource = ref([])
const loading = ref(false)
const searchText = ref('')
const selectedRowKeys = ref([])
const modalVisible = ref(false)
const modalTitle = ref('新增药品')
const drugFormRef = ref(null)

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  pageSizeOptions: ['10', '20', '50', '100'],
  showTotal: total => `共 ${total} 条`
})

// 当前操作的药品数据
const currentDrug = reactive({
  id: undefined,
  insuranceType: '丙类',
  chinaName: '',
  goodsName: '',
  specifications: '',
  drugUnit: '盒',
  drugPrice: 0,
  drugManufacturer: '',
  remarks: ''
})

// 生命周期钩子
onMounted(() => {
  fetchData()
})

// 获取药品数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.pageSize,
      chineseName: searchText.value
    }
    const res = await listDrugInfoByPageUsingGet(params)
    if (res.code === 200) {
      dataSource.value = res.data.records
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error('获取药品数据失败:', error)
    message.error('获取药品数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 表格变化处理
const handleTableChange = (pag, filters, sorter) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchData()
}

// 选择行变化
const onSelectChange = selectedKeys => {
  selectedRowKeys.value = selectedKeys
}

// 显示新增模态框
const showAddModal = () => {
  modalTitle.value = '新增药品'
  Object.assign(currentDrug, {
    id: undefined,
    insuranceType: '丙类',
    chinaName: '',
    goodsName: '',
    specifications: '',
    drugUnit: '盒',
    drugPrice: 0,
    drugManufacturer: '',
    remarks: ''
  })
  modalVisible.value = true
}

// 显示编辑模态框
const handleEdit = record => {
  modalTitle.value = '修改药品'
  Object.assign(currentDrug, JSON.parse(JSON.stringify(record)))
  modalVisible.value = true
}

// 删除药品
const handleDelete = async id => {
  try {
    await deleteDrugInfoUsingDelete({ id })
    message.success('删除成功')
    fetchData()
  } catch (error) {
    console.error('删除失败:', error)
    message.error('删除失败')
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning('请至少选择一条记录')
    return
  }

  try {
    await deleteDrugInfoByIdsUsingDelete(selectedRowKeys.value)
    message.success('批量删除成功')
    selectedRowKeys.value = []
    fetchData()
  } catch (error) {
    console.error('批量删除失败:', error)
    message.error('批量删除失败')
  }
}

// 模态框确认
const handleModalOk = async () => {
  try {
    await drugFormRef.value.validateFields()

    if (currentDrug.id) {
      // 更新
      await updateDrugInfoUsingPut(currentDrug)
      message.success('更新成功')
    } else {
      // 新增
      await addDrugInfoUsingPost(currentDrug)
      message.success('新增成功')
    }

    modalVisible.value = false
    fetchData()
  } catch (error) {
    console.error('保存失败:', error)
    if (error.errorFields) {
      message.warning('请填写完整表单')
    }
  }
}

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}
</script>

<style scoped>
.drug-info-container {
  padding: 24px;
  background: #fff;
}

.search-bar {
  margin-bottom: 16px;
}

.action-buttons {
  margin-bottom: 16px;
}
</style>
