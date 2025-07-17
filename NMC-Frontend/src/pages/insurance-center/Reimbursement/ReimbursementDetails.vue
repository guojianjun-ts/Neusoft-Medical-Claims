<template>
  <div class="reimbursement-details">
    <!-- 返回按钮和标题 -->
    <div class="header">
      <a-button type="primary" @click="goBack">
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

    <!-- 报销计算公式 - 添加浅蓝色背景 -->
    <div class="formula-section" style="background-color: #f0f8ff; padding: 10px; border-radius: 4px;">
      <div class="divider"></div>
      <h3 style="color: #1890ff; margin: 0;">
        医保报销费用 = 【( 甲类药品报销费用 + 乙类药品报销费用 + 丙类药品报销费用 + 其他费用) - 起付线 】* 报销比例
      </h3>
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

export default defineComponent({
  name: 'ReimbursementDetails',
  components: {
    ArrowLeftOutlined
  },
  setup() {
    const router = useRouter()

    // 患者基本信息
    const patientInfo = ref({
      patientName: '郭建军',
      workStatus: '在职',
      paymentType: '医保'
    })

    const totalCost = ref(
      (6665.99 + 38 + 45.72 + 20.13 + 400 + 250 + 15.6 + 3).toFixed(2)
    )

    // 药品分类费用 (根据图片数据)
    const drugCategoryCost = ref({
      '甲类': 38,       // 华夏2号淀粉米
      '乙类': 20.13,    // 葡萄糖酸钙
      '丙类': 6665.99 + 45.72  // 达诺瑞韦钠 + 头孢他美酯
    })

    // 药品报销比例
    const drugReimbursement = ref([
      { drugReimbursementType: '甲类', drugReimbursementProportion: 100 },
      { drugReimbursementType: '乙类', drugReimbursementProportion: 80 },
      { drugReimbursementType: '丙类', drugReimbursementProportion: 10 }
    ])

    // 药品数据 (根据图片数据)
    const classADrugs = ref([
      {
        drugName: '华夏2号淀粉米',
        drugSpec: '1000g',
        drugUnit: '袋',
        manufacturer: '上海津佳食品有限公司',
        drugPrice: 38,
        drugAmount: 1,
        drugType: '甲类'
      }
    ])

    const classBDrugs = ref([
      {
        drugName: '葡萄糖酸钙',
        drugSpec: '10ml:1.0g',
        drugUnit: '盒',
        manufacturer: '浙江天真健康科技有限公司',
        drugPrice: 20.13,
        drugAmount: 1,
        drugType: '乙类'
      }
    ])

    const classCDrugs = ref([
      {
        drugName: '达诺瑞韦钠',
        drugSpec: '0.1g',
        drugUnit: '瓶',
        manufacturer: '歌礼药业(浙江)有限公司',
        drugPrice: 6665.99,
        drugAmount: 1,
        drugType: '丙类'
      },
      {
        drugName: '头孢他美酯',
        drugSpec: '0.5g',
        drugUnit: '盒',
        manufacturer: '山东新时代药业有限公司',
        drugPrice: 45.72,
        drugAmount: 1,
        drugType: '丙类'
      }
    ])

    // 其他费用 (诊疗项目和医疗服务)
    const otherCostTotal = ref(400 + 250 + 15.6 + 3) // 根据图片数据计算
    const otherCosts = ref([
      // 诊疗项目
      {
        itemName: '使用腹腔镜加收',
        itemCode: '300000000/4',
        excludeContent: '',
        unit: '台次',
        itemPrice: 400,
        itemAmount: 1
      },
      {
        itemName: '使用关节镜加收',
        itemCode: '300000000/2',
        excludeContent: '',
        unit: '台次',
        itemPrice: 250,
        itemAmount: 1
      },
      // 医疗服务
      {
        itemName: '门诊诊查费(传染病、发热或心理门诊)(副主任医师及以上)',
        itemCode: '110200001-3/1',
        excludeContent: '',
        unit: '次',
        itemPrice: 15.6,
        itemAmount: 1
      },
      {
        itemName: '门诊诊查费(医师)',
        itemCode: '110200001-1',
        excludeContent: '',
        unit: '次',
        itemPrice: 3,
        itemAmount: 1
      }
    ])

// 报销规则 - 完全按照图片数据设置
    const reimbursementRules = ref([
      {
        minPayLevel: 1300,  // 起付线
        maxPayLevel: 30000,  // 等级线
        payProportion: 85    // 报销比例85%
      },
      {
        minPayLevel: 30000,
        maxPayLevel: 40000,
        payProportion: 90    // 报销比例90%
      },
      {
        minPayLevel: 40000,
        maxPayLevel: 100000,
        payProportion: 95    // 报销比例95%
      },
      {
        minPayLevel: 100000,
        maxPayLevel: 300000,
        payProportion: 85    // 报销比例85%
      }
    ])

    // 表格列定义
    const drugColumns = [
      {
        title: '药品名称',
        dataIndex: 'drugName',
        key: 'drugName'
      },
      {
        title: '规格',
        dataIndex: 'drugSpec',
        key: 'drugSpec'
      },
      {
        title: '单位',
        dataIndex: 'drugUnit',
        key: 'drugUnit'
      },
      {
        title: '生产厂家',
        dataIndex: 'manufacturer',
        key: 'manufacturer'
      },
      {
        title: '价格',
        dataIndex: 'amount',
        key: 'amount',
        align: 'right',
        customRender: ({ record }: { record: any }) =>
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
        title: '项目编码',
        dataIndex: 'itemCode',
        key: 'itemCode'
      },
      {
        title: '计价单位',
        dataIndex: 'unit',
        key: 'unit'
      },
      {
        title: '价格',
        dataIndex: 'amount',
        key: 'amount',
        align: 'right',
        customRender: ({ record }: { record: any }) =>
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

    onMounted(() => {
      // 模拟数据加载完成
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
