<template>
  <div id="medicalServiceManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="医疗服务名称">
        <a-input v-model:value="searchParams.serviceName" placeholder="输入医疗服务名称" allow-clear />
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
          <plus-outlined /> 新增服务
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
        <template v-if="column.dataIndex === 'servicePrice'">
          ¥ {{ record.servicePrice?.toFixed(2) }}
        </template>
        <template v-else-if="column.dataIndex === 'serviceType'">
          <a-tag :color="getServiceTypeColor(record.serviceType)">
            {{ record.serviceType }}
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
        :model="currentService"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        :rules="formRules"
      >
        <a-form-item label="项目类别" name="serviceType">
          <a-select v-model:value="currentService.serviceType" placeholder="请选择项目类别">
            <a-select-option value="C">C</a-select-option>
            <a-select-option value="D">D</a-select-option>
            <a-select-option value="I">I</a-select-option>
            <a-select-option value="E">E</a-select-option>
            <a-select-option value="F">F</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="项目编码" name="serviceNumber">
          <a-input v-model:value="currentService.serviceNumber" placeholder="请输入项目编码" />
        </a-form-item>
        <a-form-item label="项目国家编码" name="countryNumber">
          <a-input v-model:value="currentService.countryNumber" placeholder="请输入项目国家编码" />
        </a-form-item>
        <a-form-item label="项目名称" name="serviceName">
          <a-input v-model:value="currentService.serviceName" placeholder="请输入项目名称" />
        </a-form-item>
        <a-form-item label="项目说明" name="serviceInfo">
          <a-input v-model:value="currentService.serviceInfo" placeholder="请输入项目说明" />
        </a-form-item>
        <a-form-item label="除外内容" name="serviceExclude">
          <a-input v-model:value="currentService.serviceExclude" placeholder="请输入除外内容" />
        </a-form-item>
        <a-form-item label="计价单位" name="serviceUnit">
          <a-select v-model:value="currentService.serviceUnit" placeholder="请选择计价单位">
            <a-select-option value="盒">盒</a-select-option>
            <a-select-option value="瓶">瓶</a-select-option>
            <a-select-option value="袋">袋</a-select-option>
            <a-select-option value="支">支</a-select-option>
            <a-select-option value="片">片</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="价格" name="servicePrice">
          <a-input-number
            v-model:value="currentService.servicePrice"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="请输入价格"
          />
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="currentService.remark" placeholder="请输入备注" :rows="3" />
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
  listMedicalServiceByPageUsingGet,
  addMedicalServiceUsingPost,
  updateMedicalServiceUsingPut,
  deleteMedicalServiceUsingDelete,
  deleteMedicalServiceByIdsUsingDelete
} from '@/api/medicalServiceController'

// 表格列配置
const columns = [
  {
    title: '序号',
    dataIndex: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '项目类别',
    dataIndex: 'serviceType',
    width: 150,
  },
  {
    title: '项目编码',
    dataIndex: 'serviceNumber',
    width: 180
  },
  {
    title: '项目国家编码',
    dataIndex: 'countryNumber',
    width: 200
  },
  {
    title: '项目名称',
    dataIndex: 'serviceName',
    width: 240
  },
  {
    title: '项目说明',
    dataIndex: 'serviceInfo',
    width: 150
  },
  {
    title: '除外内容',
    dataIndex: 'serviceExclude',
    width: 150
  },
  {
    title: '计价单位',
    dataIndex: 'serviceUnit',
    width: 150
  },
  {
    title: '价格',
    dataIndex: 'servicePrice',
    width: 120,
    sorter: true
  },
  {
    title: '备注',
    dataIndex: 'remark',
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
  serviceName: undefined as string | undefined
})

// 数据列表
const dataList = ref<API.MedicalService[]>([])
const total = ref(0)
const selectedRowKeys = ref<number[]>([])
const modalVisible = ref(false)
const modalTitle = ref('新增服务')
const confirmLoading = ref(false)
const formRef = ref<FormInstance>()

// 当前操作的医疗服务数据
const currentService = reactive<API.MedicalService>({
  id: undefined,
  serviceType: 'C',
  serviceNumber: '',
  countryNumber: '',
  serviceName: '',
  serviceInfo: '',
  serviceExclude: '',
  serviceUnit: '盒',
  servicePrice: undefined,
  remark: ''
})

// 表单验证规则
const formRules = reactive({
  serviceType: [{ required: true, message: '请选择项目类别' }],
  serviceNumber: [{ required: true, message: '请输入项目编码' }],
  countryNumber: [{ required: true, message: '请输入项目国家编码' }],
  serviceName: [{ required: true, message: '请输入项目名称' }],
  serviceInfo: [{ required: true, message: '请输入项目说明' }],
  serviceExclude: [{ required: true, message: '请输入除外内容' }],
  serviceUnit: [{ required: true, message: '请选择计价单位' }],
  servicePrice: [
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

// 获取项目类型标签颜色
const getServiceTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    'C': 'red',
    'D': 'orange',
    'I': 'green',
    'E': 'blue',
    'F': 'gray'
  }
  return colors[type] || 'purple'
}

// 获取数据（统一处理分页和搜索）
const fetchData = async () => {
  try {
    const params = {
      current: searchParams.current,
      size: searchParams.pageSize,
      serviceName: searchParams.serviceName || undefined // 传undefined会被过滤
    }

    const res = await listMedicalServiceByPageUsingGet(params)

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
  modalTitle.value = '新增服务'
  Object.assign(currentService, {
    id: undefined,
    serviceType: 'C',
    serviceNumber: '',
    countryNumber: '',
    serviceName: '',
    serviceInfo: '',
    serviceExclude: '',
    serviceUnit: '盒',
    servicePrice: undefined,
    remark: ''
  })
  modalVisible.value = true
}

// 编辑服务
const handleEdit = (record: API.MedicalService) => {
  modalTitle.value = '编辑服务'
  Object.assign(currentService, JSON.parse(JSON.stringify(record)))
  modalVisible.value = true
}

// 删除服务
const doDelete = async (id: number) => {
  try {
    await deleteMedicalServiceUsingDelete({ id })
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
    await deleteMedicalServiceByIdsUsingDelete(selectedRowKeys.value)
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
      servicePrice: Number(currentService.servicePrice)
    }

    if (currentService.id) {
      // 更新操作
      const success = await updateMedicalServiceUsingPut(submitData)
      if (success) {
        message.success('更新成功')
      } else {
        message.error('更新失败')
        return
      }
    } else {
      // 新增操作
      const success = await addMedicalServiceUsingPost(submitData)
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
#medicalServiceManagePage {
  padding: 24px;
  background: #fff;
  border-radius: 4px;
}

.ant-form-inline .ant-form-item {
  margin-right: 16px;
  margin-bottom: 16px;
}

</style>
