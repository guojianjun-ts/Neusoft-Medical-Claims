<template>
  <div id="drugRates">
    <!-- 操作按钮 -->
    <div style="margin-bottom: 16px">
      <a-space>
        <a-button type="primary" @click="showAddModal">
          <plus-outlined /> 增加药品报销比例
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
      class="no-wrap-header"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
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
        :model="currentService"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        :rules="formRules"
      >
        <a-form-item label="序号" name="id">
          <a-input v-model:value="currentService.id" placeholder="请输入序号" disabled />
        </a-form-item>
        <a-form-item label="药品报销级别" name="drugReimbursementType">
          <a-select v-model:value="currentService.drugReimbursementType" placeholder="请选择药品报销级别" allowClear showSearch>
            <a-select-option value="甲类">甲类</a-select-option>
            <a-select-option value="乙类">乙类</a-select-option>
            <a-select-option value="丙类">丙类</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="报销比例" name="drugReimbursementProportion">
          <a-input-number
            v-model:value="currentService.drugReimbursementProportion"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="请输入报销比例"
          />
        </a-form-item>
        <a-form-item label="状态" name="drugStatus">
          <a-radio-group v-model:value="currentService.drugStatus">
            <a-radio :value="1">状态1</a-radio>
            <a-radio :value="2">状态2</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="说明" name="drugReimbursementInfo">
          <a-textarea v-model:value="currentService.drugReimbursementInfo" placeholder="请输入说明" :rows="3" />
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
  addDrugReimbursementUsingPost,
  deleteDrugReimbursementUsingPost,
  listAllReimbursementUsingGet,
  updateDrugReimbursementUsingPost
} from '@/api/drugReimbursementController'

// 表格列配置
const columns = [
  {
    title: '序号',
    dataIndex: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '药品报销级别',
    dataIndex: 'drugReimbursementType',
    width: 150,
  },
  {
    title: '报销比例',
    dataIndex: 'drugReimbursementProportion',
    width: 180
  },
  {
    title: '状态',
    dataIndex: 'drugStatus',
    width: 150
  },
  {
    title: '说明',
    dataIndex: 'drugReimbursementInfo',
    width: 150
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
  serviceName: undefined as string | undefined
})

// 数据列表
const dataList = ref<API.DrugReimbursement[]>([])
const total = ref(0)
const selectedRowKeys = ref<number[]>([])
const modalVisible = ref(false)
const modalTitle = ref('增加药品报销比例')
const confirmLoading = ref(false)
const formRef = ref<FormInstance>()

// 当前操作的药品数据
const currentService = reactive({
  id: undefined,
  drugReimbursementType: '甲类',
  drugReimbursementProportion: undefined,
  drugStatus: 1,
  drugReimbursementInfo: ''
})

// 表单验证规则
const formRules = reactive({
  drugReimbursementType: [{ required: true, message: '请选择药品报销级别' }],
  drugReimbursementProportion: [
    { required: true, message: '请输入报销比例' },
    { type: 'number', min: 0, message: '报销比例必须大于0' }
  ],
  drugStatus: [{ required: true, message: '请选择状态' }]
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

// 获取数据（统一处理分页和搜索）
const fetchData = async () => {
  try {
    const res = await listAllReimbursementUsingGet()

    // 修改点1：适配不同的响应结构
    const responseData = res.data?.data || res.data
    if (responseData) {
      dataList.value = Array.isArray(responseData) ? responseData : []
      total.value = dataList.value.length
    } else {
      message.error(res.data?.message || '获取数据失败')
    }
  } catch (error) {
    message.error('获取数据失败')
    console.error('获取数据失败:', error)
  }
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
  modalTitle.value = '增加药品报销比例'
  Object.assign(currentService, {
    id: undefined,
    drugReimbursementType: '甲类',
    drugReimbursementProportion: undefined,
    drugStatus: 1,
    drugReimbursementInfo: ''
  })
  modalVisible.value = true
}

// 编辑药品
const handleEdit = (record: any) => {
  modalTitle.value = '修改药品'
  Object.assign(currentService, JSON.parse(JSON.stringify(record)))
  modalVisible.value = true
}

// 删除药品
const doDelete = async (id: number) => {
  try {
    const res = await deleteDrugReimbursementUsingPost({ id })
    // 修改点4：更灵活的删除响应判断
    if (res.data.data) {
      message.success('删除成功')
      fetchData()
    } else {
      message.error(res.data?.message || '删除失败')
    }
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
    for (const id of selectedRowKeys.value) {
      await deleteDrugReimbursementUsingPost({ id })
    }
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
      ...currentService,
      drugReimbursementProportion: Number(currentService.drugReimbursementProportion),
      drugStatus: Number(currentService.drugStatus)
    }

    if (currentService.id) {
      // 更新操作
      const res = await updateDrugReimbursementUsingPost(submitData)
      // 修改点2：更灵活的响应判断
      if (res.data.data) {
        message.success('更新成功')
        modalVisible.value = false
        fetchData()
      } else {
        message.error(res.data?.message || '更新失败')
      }
    } else {
      // 新增操作
      const res = await addDrugReimbursementUsingPost(submitData)
      // 修改点3：更灵活的响应判断
      if (res.data.data) {
        message.success('新增成功')
        modalVisible.value = false
        fetchData()
      } else {
        message.error(res.data?.message || '新增失败')
      }
    }
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
#drugRates {
  padding: 24px;
  background: #fff;
  border-radius: 4px;
}

.ant-form-inline .ant-form-item {
  margin-right: 16px;
  margin-bottom: 16px;
}
</style>
