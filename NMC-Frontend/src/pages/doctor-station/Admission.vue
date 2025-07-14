<template>
  <div id="admissionPage">
    <!-- 页面标题 -->
    <div class="title">
      <FileAddOutlined />
      <span class="text">入院登记</span>
    </div>

    <!-- 操作按钮 -->
    <div style="margin-bottom: 16px">
      <a-space>
        <a-button type="primary" @click="submitForm">登记</a-button>
        <a-button @click="resetForm">清空</a-button>
      </a-space>
    </div>

    <!-- 基本信息标题 -->
    <h2>基本信息</h2>
    <div style="background-color: #f5f5f5; padding: 16px; border-radius: 4px; margin-bottom: 16px;">
      <a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <!-- 第一行：住院号、姓名、身份证号 -->
        <a-row :gutter="16">
          <a-col :span="8">
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
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="年龄" name="page">
              <a-input v-model:value="formData.page" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="出生日期" name="birthday">
              <a-select v-model:value="formData.birthday">
                <a-select-option value="2000-01-01">2000-01-01</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="性别" name="gender">
              <a-select v-model:value="formData.gender">
                <a-select-option value="男">男</a-select-option>
                <a-select-option value="女">女</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第三行：家庭住址 -->
        <a-row>
          <a-col :span="24" >
            <a-form-item label="家庭住址" name="homeAddress">
              <a-input v-model:value="formData.homeAddress" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 登记信息标题 -->
    <h2>登记信息</h2>
    <div style="background-color: #f5f5f5; padding: 16px; border-radius: 4px;">
      <a-form :model="formData" :rules="formRules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <!-- 第四行：结算类别、工作状态、入院时间 -->
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="结算类别" name="paymentType">
              <a-select v-model:value="formData.paymentType">
                <a-select-option value="类别1">类别1</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工作状态" name="workStatus">
              <a-select v-model:value="formData.workStatus">
                <a-select-option value="在职">在职</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="入院时间" name="visitDate">
              <a-input v-model:value="formData.visitDate" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import type { FormInstance, Rule } from 'ant-design-vue/es/form';
import { useRouter } from 'vue-router';
import { addPatientUsingPost } from '@/api/patientRegistrationController';
import { FileAddOutlined } from '@ant-design/icons-vue';

const router = useRouter();

const formRef = ref<FormInstance>();

// 表单数据
const formData = reactive<API.PatientRegistration>({
  ageType: '',
  birthday: '',
  cardNumber: '',
  caseNumber: '',
  gender: '',
  homeAddress: '',
  id: 0,
  page: 0,
  patientName: '',
  paymentType: '',
  visitDate: '',
  workStatus: '',
});

// 表单验证规则
const formRules: Record<string, Rule[]> = {
  caseNumber: [
    { required: true, message: '请输入住院号' },
  ],
  patientName: [
    { required: true, message: '请输入姓名' },
  ],
  cardNumber: [
    { required: true, message: '请输入身份证号' },
  ],
  age: [
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
    { required: true, message: '请输入入院时间' },
  ],
};

// 提交表单
const submitForm = async () => {
  try {
    // 验证表单字段
    await formRef.value?.validate();
    // 调用 API 进行数据提交
    const response = await addPatientUsingPost(formData);
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
  formData.birthday = '';
  formData.cardNumber = '';
  formData.caseNumber = '';
  formData.gender = '';
  formData.homeAddress = '';
  formData.id = 0;
  formData.page = 0;
  formData.patientName = '';
  formData.paymentType = '';
  formData.visitDate = '';
  formData.workStatus = '';
};
</script>

<style scoped>
#admissionPage {
  padding: 16px;
}
.title{
  display: flex;
  align-items: center;
  height: 80px;
  margin-bottom: 16px;
  border-bottom: 1px solid #444;
  font-size: 28px;
}
.title svg {
  margin-right: 8px;
  font-size: 22px;
}
/* 新增样式：使标签左对齐 */
.ant-form-item-label {
  text-align: left !important;
}
</style>
