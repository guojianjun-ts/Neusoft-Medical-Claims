<template>
  <div class="admission-container">
    <!-- 页面标题 -->
    <div class="page-title">
      <FileAddOutlined />
      入院登记
    </div>

    <!-- 操作按钮 -->
    <div class="nav-buttons">
      <a-space>
        <a-button type="primary" @click="submitForm">登记</a-button>
        <a-button @click="resetForm">清空</a-button>
      </a-space>
    </div>

    <!-- 登记信息 -->
    <a-divider orientation="left" class="section-title">患者信息登记</a-divider>
    <div class="patient-detail">
      <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
        <!-- 第一行：住院号、姓名、身份证号 -->
        <a-row :gutter="24">
          <a-col :span="8"> <!-- 24/3=8，三个输入框均分一行 -->
            <a-form-item label="住院号" name="caseNumber">
              <a-input v-model:value="formData.caseNumber" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="姓名" name="patientName">
              <a-input v-model:value="formData.patientName" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="身份证号" name="cardNumber">
              <a-input v-model:value="formData.cardNumber" />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第二行：年龄、出生日期、性别 -->
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="年龄" name="page">
              <a-input v-model:value="formData.page" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="出生日期" name="birthday">
              <a-date-picker v-model:value="formData.birthday" format="YYYY-MM-DD" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="性别" name="gender">
              <a-select v-model:value="formData.gender" style="width: 100%">
                <a-select-option value="男">男</a-select-option>
                <a-select-option value="女">女</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第三行：结算类别、工作状态、入院时间（三个一行） -->
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="结算类别" name="paymentType">
              <a-select v-model:value="formData.paymentType" style="width: 100%">
                <a-select-option value="医保">医保</a-select-option>
                <a-select-option value="自费">自费</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工作状态" name="workStatus">
              <a-select v-model:value="formData.workStatus" style="width: 100%">
                <a-select-option value="在职">在职</a-select-option>
                <a-select-option value="在职">退休</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="入院时间" name="visitDate">
              <a-date-picker
                v-model:value="formData.visitDate"
                format="YYYY-MM-DD HH:mm:ss"
                :disabled="true"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第四行：家庭住址（独占一行） -->
        <a-row>
          <a-col :span="24">
            <a-form-item label="家庭住址" name="homeAddress">
              <a-input v-model:value="formData.homeAddress" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import type { FormInstance, Rule } from 'ant-design-vue/es/form';
import { useRouter } from 'vue-router';
import { addPatientUsingPost } from '@/api/patientRegistrationController';
import { FileAddOutlined } from '@ant-design/icons-vue';
import moment from 'moment';

const router = useRouter();

const formRef = ref<FormInstance>();

// 表单数据
const formData = reactive<API.PatientRegistration>({
  ageType: '',
  birthday: undefined,
  cardNumber: '',
  caseNumber: '',
  gender: '',
  homeAddress: '',
  id: 0,
  page: 0,
  patientName: '',
  paymentType: '',
  visitDate: moment(), // 使用moment对象初始化
  workStatus: '',
});

// 身份证号验证函数
const validateCardNumber = (rule: Rule, value: string) => {
  if (!value) {
    return Promise.reject('请输入身份证号');
  }
  const cardReg = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if (!cardReg.test(value)) {
    return Promise.reject('请输入有效的18位身份证号');
  }
  return Promise.resolve();
};

// 表单验证规则
const formRules: Record<string, Rule[]> = {
  caseNumber: [
    { required: true, message: '请输入住院号' },
  ],
  patientName: [
    { required: true, message: '请输入姓名' },
  ],
  cardNumber: [
    { required:true,validator: validateCardNumber, trigger: 'blur' },
  ],
  page: [
    { required: true, message: '请输入年龄' },
  ],
  birthday: [
    { required: true, message: '请选择出生日期' },
  ],
  gender: [
    { required: true, message: '请选择性别' },
  ],
  paymentType: [
    { required: true, message: '请选择结算类别' },
  ],
  workStatus: [
    { required: true, message: '请选择工作状态' },
  ],
  visitDate: [
    { required: true, message: '入院时间将由系统自动生成' },
  ],
};

// 页面加载时自动填充当前时间
onMounted(() => {
  formData.visitDate = moment();
});

// 提交表单
const submitForm = async () => {
  try {
    // 验证表单字段
    await formRef.value?.validate();

    // 不需要提交visitDate，由后端自动生成
    const formDataToSubmit = { ...formData };
    delete formDataToSubmit.visitDate;

    // 调用 API 进行数据提交
    const response = await addPatientUsingPost(formDataToSubmit);
    if (response.code === 200) {
      message.success('登记成功');
      // 登记成功后跳转到患者选择页面
      router.push('/doctor-station/diagnosis/patient-select');
    } else {
      message.error('登记失败，请稍后重试');
    }
  } catch (error) {
    message.error('登记失败，请检查输入信息');
  }
};

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields();
  formData.ageType = '';
  formData.birthday = undefined;
  formData.cardNumber = '';
  formData.caseNumber = '';
  formData.gender = '';
  formData.homeAddress = '';
  formData.id = 0;
  formData.page = 0;
  formData.patientName = '';
  formData.paymentType = '';
  formData.visitDate = moment();
  formData.workStatus = '';
};
</script>

<style scoped>
.admission-container {
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

.page-title svg {
  margin-right: 8px;
  font-size: 22px;
}

.nav-buttons {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
}

.patient-detail {
  margin-top: 24px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 4px;
}

/* 患者详细信息表单：禁用状态文字黑色 */
.patient-detail :deep(.ant-form-item-control-input-content .ant-input-disabled) {
  color: #000 !important;
  background-color: #fff !important;
}

/* 表单标签左对齐 */
:deep(.ant-form-item-label) {
  text-align: left !important;
}

/* 章节标题加粗 */
.section-title {
  font-weight: bold;
}
</style>
