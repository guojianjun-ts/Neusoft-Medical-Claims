<template>
  <div class="reimbursement-details">
    <!-- 返回按钮和标题 -->
    <div class="header">
      <a-button type="text" @click="goBack">
        <template #icon><arrow-left-outlined /></template>
        返回
      </a-button>
      <h1>参保人员费用报销</h1>
    </div>

    <!-- 患者基本信息 -->
    <div class="patient-info">
      <div class="info-item">
        <span class="label">姓名：</span>
        <span class="value">{{ patientInfo.patientName }}</span>
      </div>
      <div class="info-item">
        <span class="label">人员类别：</span>
        <span class="value">{{ patientInfo.workStatus }}</span>
      </div>
      <div class="info-item">
        <span class="label">结算类别：</span>
        <span class="value">{{ patientInfo.paymentType }}</span>
      </div>
      <div class="info-item">
        <span class="label">总费用：</span>
        <span class="value">{{ totalCost }} 元</span>
      </div>
    </div>

    <!-- 四个卡片 -->
    <div class="cards-container">
      <!-- 甲类药品卡片 -->
      <a-card title="甲类药品" class="cost-card">
        <div class="card-header">
          <span>报销比例：{{ drugReimbursement[0]?.drugReimbursementProportion }}%</span>
          <span>总费用：{{ drugCategoryCost['甲类'] || 0 }} 元</span>
        </div>
        <a-table
          :columns="drugColumns"
          :data-source="classADrugs"
          :pagination="false"
          size="small"
        />
      </a-card>

      <!-- 乙类药品卡片 -->
      <a-card title="乙类药品" class="cost-card">
        <div class="card-header">
          <span>报销比例：{{ drugReimbursement[1]?.drugReimbursementProportion }}%</span>
          <span>总费用：{{ drugCategoryCost['乙类'] || 0 }} 元</span>
        </div>
        <a-table
          :columns="drugColumns"
          :data-source="classBDrugs"
          :pagination="false"
          size="small"
        />
      </a-card>

      <!-- 丙类药品卡片 -->
      <a-card title="丙类药品" class="cost-card">
        <div class="card-header">
          <span>报销比例：{{ drugReimbursement[2]?.drugReimbursementProportion }}%</span>
          <span>总费用：{{ drugCategoryCost['丙类'] || 0 }} 元</span>
        </div>
        <a-table
          :columns="drugColumns"
          :data-source="classCDrugs"
          :pagination="false"
          size="small"
        />
      </a-card>

      <!-- 其他费用卡片 -->
      <a-card title="其他费用" class="cost-card">
        <div class="card-header">
          <span>诊疗和医疗服务总费用：{{ otherCostTotal }} 元</span>
        </div>
        <a-table
          :columns="otherColumns"
          :data-source="otherCosts"
          :pagination="false"
          size="small"
        />
      </a-card>
    </div>

    <!-- 报销计算公式 -->
    <div class="formula-section">
      <div class="divider"></div>
      <h3>医保报销费用 = 【( 甲类药品报销费用 + 乙类药品报销费用 + 丙类药品报销费用 + 其他费用) - 起付线 】* 报销比例</h3>
    </div>

    <!-- 医院报销数据表格 -->
    <div class="reimbursement-rules">
      <a-table
        :columns="rulesColumns"
        :data-source="reimbursementRules"
        :pagination="false"
        size="middle"
      />
    </div>

    <!-- 底部报销费用和确认按钮 -->
    <div class="footer">
      <div class="reimbursement-total">
        <span>报销费用：</span>
        <span class="total-amount">{{ calculatedReimbursement }} 元</span>
      </div>
      <a-button type="primary">确认</a-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import {
  getTotalCostUsingGet,
  getDrugCategoryCostUsingGet,
  getAllCategoryCostUsingGet
} from '@/api/insuranceCostController'
import { listAllReimbursementUsingGet } from '@/api/drugReimbursementController'
import { listDrugCostByPatientIdUsingGet } from '@/api/insuranceController'
import { getHospitalReimbursementListUsingGet } from '@/api/insuranceController'
import { message } from 'ant-design-vue'

interface DrugCost {
  drugName: string
  drugPrice: number
  drugAmount: number
  drugType: string
}

interface OtherCost {
  itemName: string
  itemPrice: number
  itemAmount: number
}

interface ReimbursementRule {
  minPayLevel: number
  maxPayLevel: number
  payProportion: number
}

export default defineComponent({
  name: 'ReimbursementDetails',
  components: {
    ArrowLeftOutlined
  },
  setup() {
    const router = useRouter()
    const patientId = ref(router.currentRoute.value.query.patientId as string)

    // 患者基本信息
    const patientInfo = ref({
      patientName: '张三',
      workStatus: '在职',
      paymentType: '医保'
    })

    // 总费用
    const totalCost = ref(0)

    // 药品分类费用
    const drugCategoryCost = ref<Record<string, number>>({})

    // 药品报销比例
    const drugReimbursement = ref<any[]>([])

    // 药品数据
    const classADrugs = ref<DrugCost[]>([])
    const classBDrugs = ref<DrugCost[]>([])
    const classCDrugs = ref<DrugCost[]>([])

    // 其他费用
    const otherCostTotal = ref(0)
    const otherCosts = ref<OtherCost[]>([])

    // 报销规则
    const reimbursementRules = ref<ReimbursementRule[]>([])

    // 表格列定义
    const drugColumns = [
      {
        title: '药品名称',
        dataIndex: 'drugName',
        key: 'drugName'
      },
      {
        title: '单价',
        dataIndex: 'drugPrice',
        key: 'drugPrice',
        align: 'right',
        customRender: ({ text }: { text: number }) => `${text} 元`
      },
      {
        title: '价格',
        dataIndex: 'amount',
        key: 'amount',
        align: 'right',
        customRender: ({ record }: { record: DrugCost }) =>
          `${(record.drugPrice * record.drugAmount).toFixed(2)} 元`
      }
    ]

    const otherColumns = [
      {
        title: '项目名称',
        dataIndex: 'itemName',
        key: 'itemName'
      },
      {
        title: '单价',
        dataIndex: 'itemPrice',
        key: 'itemPrice',
        align: 'right',
        customRender: ({ text }: { text: number }) => `${text} 元`
      },
      {
        title: '价格',
        dataIndex: 'amount',
        key: 'amount',
        align: 'right',
        customRender: ({ record }: { record: OtherCost }) =>
          `${(record.itemPrice * record.itemAmount).toFixed(2)} 元`
      }
    ]

    const rulesColumns = [
      {
        title: '起付线',
        dataIndex: 'minPayLevel',
        key: 'minPayLevel',
        align: 'center',
        customRender: ({ text }: { text: number }) => `${text} 元`
      },
      {
        title: '等级线',
        dataIndex: 'maxPayLevel',
        key: 'maxPayLevel',
        align: 'center',
        customRender: ({ text }: { text: number }) => `${text} 元`
      },
      {
        title: '报销比例',
        dataIndex: 'payProportion',
        key: 'payProportion',
        align: 'center',
        customRender: ({ text }: { text: number }) => `${text}%`
      }
    ]

    // 返回上一页
    const goBack = () => {
      router.go(-1)
    }

    // 计算报销总额
    const calculatedReimbursement = computed(() => {
      if (reimbursementRules.value.length === 0) return 0

      const rule = reimbursementRules.value[0]
      const classACost = drugCategoryCost.value['甲类'] || 0
      const classBCost = drugCategoryCost.value['乙类'] || 0
      const classCCost = drugCategoryCost.value['丙类'] || 0
      const otherCost = otherCostTotal.value || 0

      const classAReimburse = classACost * (drugReimbursement.value[0]?.drugReimbursementProportion || 0) / 100
      const classBReimburse = classBCost * (drugReimbursement.value[1]?.drugReimbursementProportion || 0) / 100
      const classCReimburse = classCCost * (drugReimbursement.value[2]?.drugReimbursementProportion || 0) / 100

      const totalBeforeDeductible = classAReimburse + classBReimburse + classCReimburse + otherCost
      const afterDeductible = Math.max(0, totalBeforeDeductible - rule.minPayLevel)

      return (afterDeductible * rule.payProportion / 100).toFixed(2)
    })

// 修改后的loadData函数
    const loadData = async () => {
      try {
        // 获取总费用
        const totalRes = await getTotalCostUsingGet({ patientId: patientId.value })
        if (totalRes.data.code === 0) {
          totalCost.value = totalRes.data.data || 0
        }

        // 获取药品分类费用
        const drugCategoryRes = await getDrugCategoryCostUsingGet({ patientId: patientId.value })
        if (drugCategoryRes.data.code === 0) {
          drugCategoryCost.value = drugCategoryRes.data.data || {}
        }

        // 获取药品报销比例
        const reimbursementRes = await listAllReimbursementUsingGet()
        if (reimbursementRes.data.code === 0) {
          drugReimbursement.value = reimbursementRes.data.data || []
        }

        // 获取药品明细
        const drugListRes = await listDrugCostByPatientIdUsingGet({
          patientId: patientId.value,
          current: 1,
          size: 100
        })
        if (drugListRes.data.code === 0 && drugListRes.data.data?.records) {
          classADrugs.value = drugListRes.data.data.records.filter((item: any) => item.drugType === '甲类')
          classBDrugs.value = drugListRes.data.data.records.filter((item: any) => item.drugType === '乙类')
          classCDrugs.value = drugListRes.data.data.records.filter((item: any) => item.drugType === '丙类')
        }

        // 获取其他费用
        const otherCostRes = await getAllCategoryCostUsingGet({ patientId: patientId.value })
        if (otherCostRes.data.code === 0) {
          // 模拟其他费用数据
          otherCostTotal.value = 500 // 模拟数据
          otherCosts.value = [
            { itemName: '诊疗项目A', itemPrice: 100, itemAmount: 2 },
            { itemName: '医疗服务B', itemPrice: 150, itemAmount: 1 },
            { itemName: '诊疗项目C', itemPrice: 80, itemAmount: 3 }
          ]
        }

        // 获取报销规则
        const rulesRes = await getHospitalReimbursementListUsingGet({
          hospitalLevel: '一级医院',
          peopleTypeDesc: patientInfo.value.workStatus === '在职' ? '在职人员' : '退休人员',
          current: 1,
          size: 10
        })
        if (rulesRes.data.code === 0 && rulesRes.data.data) {
          reimbursementRules.value = rulesRes.data.data.map((item: any) => ({
            minPayLevel: item.minPayLevel,
            maxPayLevel: item.maxPayLevel,
            payProportion: item.payProportion
          }))
        }
      } catch (error) {
        message.error('数据加载失败')
        console.error(error)
      }
    }

    onMounted(() => {
      loadData()
    })

    return {
      patientInfo,
      totalCost,
      drugCategoryCost,
      drugReimbursement,
      classADrugs,
      classBDrugs,
      classCDrugs,
      otherCostTotal,
      otherCosts,
      reimbursementRules,
      drugColumns,
      otherColumns,
      rulesColumns,
      calculatedReimbursement,
      goBack
    }
  }
})
</script>

<style scoped>
.reimbursement-details {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  margin-left: 20px;
  margin-bottom: 0;
}

.patient-info {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item .label {
  font-weight: bold;
  margin-right: 5px;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.cost-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-weight: bold;
}

.formula-section {
  margin: 30px 0;
  text-align: center;
}

.formula-section .divider {
  border-top: 1px solid #e8e8e8;
  margin-bottom: 15px;
}

.reimbursement-rules {
  margin: 30px 0;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e8e8e8;
}

.reimbursement-total {
  font-size: 18px;
}

.reimbursement-total .total-amount {
  font-weight: bold;
  color: #1890ff;
  font-size: 20px;
}
</style>
