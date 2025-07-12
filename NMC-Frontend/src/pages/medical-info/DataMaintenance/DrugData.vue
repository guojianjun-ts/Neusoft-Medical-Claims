<template>
  <div id="drugManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="药品名称">
        <a-input v-model:value="searchParams.chinaName" placeholder="输入药品名称" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <div style="margin-bottom: 16px" />

    <!-- 操作按钮 -->
    <div style="margin-bottom: 16px">
      <a-space>
        <a-button type="primary" @click="showAddModal">
          <plus-outlined /> 新增药品
        </a-button>
        <a-button danger :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
          <delete-outlined /> 批量删除
        </a-button>
      </a-space>
    </div>

    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
      :row-key="record => record.id"
      @change="doTableChange"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'drugPrice'">
          ¥ {{ record.drugPrice?.toFixed(2) }}
        </template>
        <template v-else-if="column.dataIndex === 'insuranceType'">
          <a-tag :color="getDrugTypeColor(record.insuranceType)">
            {{ record.insuranceType }}
          </a-tag>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button size="small" @click="handleEdit(record)">编辑</a-button>
            <a-button size="small" danger @click="doDelete(record.id)">删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 新增/编辑模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      width="800px"
      :confirm-loading="confirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="formRef"
        :model="currentDrug"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        :rules="formRules"
      >
        <a-form-item label="药品类型" name="insuranceType">
          <a-select v-model:value="currentDrug.insuranceType" placeholder="请选择药品类型">
            <a-select-option value="甲类">甲类</a-select-option>
            <a-select-option value="乙类">乙类</a-select-option>
            <a-select-option value="丙类">丙类</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="药品名称" name="chinaName">
          <a-input v-model:value="currentDrug.chinaName" placeholder="请输入药品名称" />
        </a-form-item>
        <a-form-item label="品名" name="goodsName">
          <a-input v-model:value="currentDrug.goodsName" placeholder="请输入品名" />
        </a-form-item>
        <a-form-item label="规格" name="specifications">
          <a-input v-model:value="currentDrug.specifications" placeholder="请输入规格" />
        </a-form-item>
        <a-form-item label="单位" name="drugUnit">
          <a-select v-model:value="currentDrug.drugUnit" placeholder="请选择单位">
            <a-select-option value="盒">盒</a-select-option>
            <a-select-option value="瓶">瓶</a-select-option>
            <a-select-option value="袋">袋</a-select-option>
            <a-select-option value="支">支</a-select-option>
            <a-select-option value="片">片</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="价格" name="drugPrice">
          <a-input-number
            v-model:value="currentDrug.drugPrice"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="请输入价格"
          />
        </a-form-item>
        <a-form-item label="备注" name="remarks">
          <a-textarea v-model:value="currentDrug.remarks" placeholder="请输入备注" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { message, type FormInstance } from 'ant-design-vue'
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'
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
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '药品类型',
    dataIndex: 'insuranceType',
    width: 120
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
    sorter: true
  },
  {
    title: '生产厂家',
    dataIndex: 'drugManufacturer',
    width: 200
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    align: 'center'
  }
]

// 搜索参数
const searchParams = reactive({
  current: 1,
  pageSize: 10,
  chinaName: undefined as string | undefined
})

// 数据列表
const dataList = ref<API.DrugInfo[]>([])
const total = ref(0)
const selectedRowKeys = ref<number[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增药品')
const confirmLoading = ref(false)
const formRef = ref<FormInstance>()

// 当前操作的药品数据
const currentDrug = reactive<API.DrugInfo>({
  id: undefined,
  insuranceType: '丙类',
  chinaName: '',
  goodsName: '',
  specifications: '',
  drugUnit: '盒',
  drugManufacturer: '',
  drugPrice: undefined,
  remarks: ''
})

// 表单验证规则
const formRules = reactive({
  insuranceType: [{ required: true, message: '请选择药品类型' }],
  chinaName: [{ required: true, message: '请输入药品名称' }],
  specifications: [{ required: true, message: '请输入规格' }],
  drugUnit: [{ required: true, message: '请选择单位' }],
  drugManufacturer: [{ required: true, message: '请输入生产厂家' }],
  drugPrice: [
    { required: true, message: '请输入价格' },
    { type: 'number', min: 0, message: '价格必须大于0' }
  ]
})

// 分页配置
const pagination = computed(() => ({
  current: searchParams.current,
  pageSize: searchParams.pageSize,
  total: total.value,
  showSizeChanger: true,
  showTotal: total => `共 ${total} 条`,
  pageSizeOptions: ['10', '20', '50', '100']
}))

// 获取药品类型标签颜色
const getDrugTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    '甲类': 'red',
    '乙类': 'orange',
    '丙类': 'green'
  }
  return colors[type] || 'purple'
}

// 获取数据（统一处理分页和搜索）
const fetchData = async () => {
  try {
    const params = {
      current: searchParams.current,
      size: searchParams.pageSize,
      chinaName: searchParams.chinaName || undefined // 传undefined会被过滤
    }

    const res = await listDrugInfoByPageUsingGet(params)

    if (res.data.data) {
      dataList.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    } else {
      message.error(res.data.message || '获取数据失败')
    }
  } catch (error) {
    message.error('获取数据失败')
  }
}

// 搜索
const doSearch = () => {
  searchParams.current = 1
  fetchData()
}

// 表格变化
const doTableChange = (pag: any) => {
  searchParams.current = pag.current
  searchParams.pageSize = pag.pageSize
  fetchData()
}

// 行选择变化
const onSelectChange = (selectedKeys: number[]) => {
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
    drugPrice: undefined,
    drugManufacturer: '',
    remarks: ''
  })
  modalVisible.value = true
}

// 编辑药品
const handleEdit = (record: API.DrugInfo) => {
  modalTitle.value = '编辑药品'
  Object.assign(currentDrug, JSON.parse(JSON.stringify(record)))
  modalVisible.value = true
}

// 删除药品
const doDelete = async (id: number) => {
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
  if (!selectedRowKeys.value.length) {
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
    await formRef.value?.validateFields()
    confirmLoading.value = true

    const submitData = {
      ...currentDrug,
      drugPrice: Number(currentDrug.drugPrice)
    }

    if (currentDrug.id) {
      // 更新操作
      const success = await updateDrugInfoUsingPut(submitData)
      if (success) {
        message.success('更新成功')
      } else {
        message.error('更新失败')
        return
      }
    } else {
      // 新增操作
      const success = await addDrugInfoUsingPost(submitData)
      if (success) {
        message.success('新增成功')
      } else {
        message.error('新增失败')
        return
      }
    }

    modalVisible.value = false
    fetchData()
  } catch (error) {
    console.error('保存失败:', error)
    if (!(error as any).errorFields) {
      message.error('保存失败，请重试')
    }
  } finally {
    confirmLoading.value = false
  }
}

// 模态框取消
const handleModalCancel = () => {
  formRef.value?.resetFields()
  modalVisible.value = false
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
#drugManagePage {
  padding: 24px;
  background: #fff;
  border-radius: 4px;
}

.ant-form-inline .ant-form-item {
  margin-right: 16px;
  margin-bottom: 16px;
}
</style>
