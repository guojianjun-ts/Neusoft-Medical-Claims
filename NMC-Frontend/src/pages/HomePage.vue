<template>
  <div class="container">
    <header class="header">
      <h1 class="title">东软医疗保险报销平台</h1>
      <p class="subtitle">高效 · 安全 · 便捷的医保服务解决方案</p>
    </header>

    <div class="welcome-section">
      <p class="welcome-text">欢迎使用东软医疗保险报销平台，本平台主要服务于三个核心平台，为您提供全方位的医保服务支持。</p>
    </div>

    <div class="platform-cards">
      <!-- 医疗保险基本信息维护 -->
      <div class="platform-card">
        <div class="card-header bg-blue">
          <h2>医疗保险基本信息维护</h2>
          <i class="icon">📋</i>
        </div>
        <div class="card-content">
          <p>该平台主要负责与医保密切相关的基本信息维护工作，包括国家医疗三大目录维护以及报销参数设置等。</p>
          <ul>
            <li><i class="list-icon">💊</i> 医保药品数据维护</li>
            <li><i class="list-icon">🏥</i> 诊疗项目数据维护</li>
            <li><i class="list-icon">🛏️</i> 医疗服务设施维护</li>
            <li><i class="list-icon">📊</i> 药品报销比例设置</li>
            <li><i class="list-icon">🏨</i> 医院等级报销比例设置</li>
          </ul>
        </div>
      </div>

      <!-- 医院住院医生站医嘱处理 -->
      <div class="platform-card">
        <div class="card-header bg-green">
          <h2>医院住院医生站医嘱处理</h2>
          <i class="icon">👨‍⚕️</i>
        </div>
        <div class="card-content">
          <p>此平台主要用于医生对患者的相关医疗信息进行录入和管理操作。</p>
          <ul>
            <li><i class="list-icon">🩺</i> 患者疾病诊断管理</li>
            <li><i class="list-icon">💊</i> 患者药品处方医嘱</li>
            <li><i class="list-icon">🧪</i> 患者诊疗项目医嘱</li>
            <li><i class="list-icon">🏥</i> 患者医疗服务医嘱</li>
            <li><i class="list-icon">📝</i> 完整诊疗记录管理</li>
          </ul>
        </div>
      </div>

      <!-- 医保中心报销管理 -->
      <div class="platform-card">
        <div class="card-header bg-purple">
          <h2>医保中心报销管理</h2>
          <i class="icon">💰</i>
        </div>
        <div class="card-content">
          <p>该平台主要围绕参保人员的保险信息及费用报销开展工作。</p>
          <ul>
            <li><i class="list-icon">👤</i> 参保人员信息维护</li>
            <li><i class="list-icon">🔍</i> 参保人员费用查询</li>
            <li><i class="list-icon">📈</i> 报销详情可视化报表</li>
            <li><i class="list-icon">🧮</i> 智能报销比例计算</li>
            <li><i class="list-icon">✅</i> 一键报销操作</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 这里可以添加一些逻辑，如果后续需要的话
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
  color: #333;
}

.header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px 0;
  background: linear-gradient(135deg, #1e5799 0%, #207cca 100%);
  color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
}

.welcome-section {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.welcome-text {
  font-size: 1.1rem;
  color: #555;
  margin: 0;
}

.platform-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
  margin-bottom: 40px;
}

.platform-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.platform-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  color: white;
}

.bg-blue {
  background: linear-gradient(135deg, #3498db 0%, #2c3e50 100%);
}

.bg-green {
  background: linear-gradient(135deg, #2ecc71 0%, #27ae60 100%);
}

.bg-purple {
  background: linear-gradient(135deg, #9b59b6 0%, #8e44ad 100%);
}

.card-header h2 {
  margin: 0;
  font-size: 1.4rem;
}

.icon {
  font-size: 1.8rem;
}

.card-content {
  padding: 20px;
}

.card-content p {
  margin-top: 0;
  margin-bottom: 15px;
  color: #555;
  line-height: 1.6;
}

.card-content ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.card-content li {
  padding: 8px 0;
  display: flex;
  align-items: center;
  color: #555;
}

.list-icon {
  margin-right: 10px;
  font-size: 1.2rem;
}

@media (max-width: 768px) {
  .platform-cards {
    grid-template-columns: 1fr;
  }

  .title {
    font-size: 2rem;
  }
}
</style>
