<template>
  <div class="level1-hospital-container">
    <!-- 搜索和操作区域 -->
    <a-card :bordered="false">
      <a-form layout="inline" :model="searchForm">
        <a-form-item style="margin-left: auto">
          <a-button type="primary" @click="handleAdd">
            <template #icon>
              <plus-outlined />
            </template>
            新增报销比例
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 数据表格 -->
    <a-card style="margin-top: 16px">  <!-- 修改了这里的margin-top -->
      <a-table
        :columns="columns"
        :data-source="tableData"
        :loading="loading"
        :pagination="false"
        bordered
        row-key="id"
      >
        <template #statusDesc="{ record }">  <!-- 修改了插槽名称 -->
          <a-tag :color="record.statusDesc === '正常' ? 'green' : 'red'">
            {{ record.statusDesc }}
          </a-tag>
        </template>
        <template #action="{ record }">
          <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
          <a-button
            type="link"
            size="small"
            danger
            @click="handleDelete(record.id)"
          >
            删除
          </a-button>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑对话框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      width="40%"
      :after-close="resetForm"
      @ok="submitForm"
    >
      <a-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="起付金额" name="minPayLevel">
          <a-input-number
            v-model:value="formData.minPayLevel"
            :min="0"
            :step="100"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="封顶金额" name="maxPayLevel">
          <a-input-number
            v-model:value="formData.maxPayLevel"
            :min="formData.minPayLevel + 1"
            :step="100"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="人员类型" name="peopleType">
          <a-radio-group v-model:value="formData.peopleType">
            <a-radio :value="1">在职人员</a-radio>
            <a-radio :value="0">退休人员</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="报销比例(%)" name="payProportion">
          <a-input-number
            v-model:value="formData.payProportion"
            :min="0"
            :max="100"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-switch
            v-model:checked="formData.status"
            :checked-value="1"
            :un-checked-value="0"
            checked-children="启用"
            un-checked-children="禁用"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import type { FormInstance, Rule } from 'ant-design-vue/es/form'
import { PlusOutlined } from '@ant-design/icons-vue'
import {
  getByLevelUsingGet,
  addUsingPost,
  updateUsingPut,
  deleteUsingDelete
} from '@/api/hospitalReimbursementController'

// 表格列配置
const columns = [
  {
    title: '费用区间',
    dataIndex: 'payRange',
    width: 220,
    align: 'center'
  },
  {
    title: '人员类型',
    dataIndex: 'peopleTypeDesc',
    width: 220,
    align: 'center'
  },
  {
    title: '报销比例',
    dataIndex: 'proportionDesc',
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'statusDesc',  // 使用statusDesc字段
    width: 220,
    slots: { customRender: 'statusDesc' },  // 修改为statusDesc
    align: 'center'
  },
  {
    title: '操作',
    width: 300,
    fixed: 'right',
    slots: { customRender: 'action' },
    align: 'center'
  }
]

// 表格数据
const tableData = ref<API.HospitalReimbursementVO[]>([])
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  peopleType: undefined as number | undefined
})

// 对话框相关
const modalVisible = ref(false)
const modalTitle = ref('')
const formRef = ref<FormInstance>()
const formData = reactive({
  id: undefined as number | undefined,
  minPayLevel: 0,
  maxPayLevel: 0,
  peopleType: 1,
  payProportion: 0,
  status: 1,
  hospitalLevel: '一级医院'
})

// 表单验证规则
const formRules: Record<string, Rule[]> = {
  minPayLevel: [
    { required: true, message: '请输入起付金额' },
    {
      validator: (_, value) => {
        if (value >= formData.maxPayLevel) {
          return Promise.reject('起付金额必须小于封顶金额')
        }
        return Promise.resolve()
      }
    }
  ],
  maxPayLevel: [
    { required: true, message: '请输入封顶金额' }
  ],
  payProportion: [
    { required: true, message: '请输入报销比例' }
  ]
}

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const params = {
      hospitalLevel: '一级医院',
      peopleType: searchForm.peopleType || undefined
    }

    const res = await getByLevelUsingGet(params)

    if (res.data?.code === 0) {
      tableData.value = res.data.data || []
    } else {
      message.error(res.data?.message || '获取数据失败')
    }
  } catch (error) {
    message.error('获取数据失败')
    console.error('请求错误:', error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.peopleType = undefined
  fetchData()
}

// 新增
const handleAdd = () => {
  modalTitle.value = '新增报销比例'
  modalVisible.value = true
}

// 编辑
const handleEdit = (record: API.HospitalReimbursementVO) => {
  modalTitle.value = '编辑报销比例'
  formData.id = record.id
  if (record.payRange) {
    const [min, max] = record.payRange.split(' ~ ').map(Number)
    formData.minPayLevel = min
    formData.maxPayLevel = max
  }
  formData.peopleType = record.peopleTypeDesc === '在职人员' ? 1 : 0
  if (record.proportionDesc) {
    formData.payProportion = parseInt(record.proportionDesc)
  }
  formData.status = record.statusDesc === '正常' ? 1 : 0
  modalVisible.value = true
}

// 删除
const handleDelete = (id: number) => {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除该报销比例吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        const res = await deleteUsingDelete({ id })
        if (res.data?.code === 0) {
          message.success('删除成功')
          fetchData()
        } else {
          message.error(res.data?.message || '删除失败')
        }
      } catch (error) {
        message.error('删除失败')
      }
    }
  })
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value?.validate()

    let res
    if (formData.id) {
      res = await updateUsingPut(formData)
    } else {
      res = await addUsingPost(formData)
    }

    if (res.data?.code === 0) {
      message.success(formData.id ? '更新成功' : '新增成功')
      modalVisible.value = false
      fetchData()
    } else {
      message.error(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('表单提交错误:', error)
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  formData.id = undefined
  formData.minPayLevel = 0
  formData.maxPayLevel = 0
  formData.peopleType = 1
  formData.payProportion = 0
  formData.status = 1
}

// 初始化加载数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.level1-hospital-container {
  padding: 16px;
  background: #f0f2f5;
  margin-bottom: 8px;
}

:deep(.ant-card) {
  border-radius: 4px;
  margin-bottom: 2px;  /* 减小底部间距 */
}

:deep(.ant-table) {
  border-radius: 4px;
  margin-bottom: 8px;  /* 减小底部间距 */
}
</style>
