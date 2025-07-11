# Nesoft-Medical-Claims 开发日志

## 医疗基本信息维护模块

#### 一、数据信息维护

1. 医保药品数据维护

2. 诊疗项目数据维护

3. 医疗服务设施数据维护

#### 二、报销比例维护

1. 药品报销比例
2. 三级医院报销比例
3. 二级医院报销比例
4. 一级医院报销比例

## 医院住院医生站医嘱处理模块

#### 一、入院登记

#### 二、患者诊断

1. 患者选择
2. 入院诊断
3. 主要诊断
4. 其它诊断

#### 三、患者医嘱

1. 患者选择
1. 药品处方医嘱
1. 治疗项目医嘱
1. 医疗服务医嘱

## 医保中心报销管理模块

#### 一、参保人员信息管理

#### 二、参保人员费用查询

#### 三、参保人员费用报销

1. 参保人员费用详情
2. 参保人员报销详情

```bash
src/Pages/
├── medical-info/                  # 医疗基本信息维护模块
│   ├── DataMaintenance/           # 数据信息维护
│   │   ├── DrugData.vue           # 医保药品数据维护
│   │   ├── TreatmentItems.vue     # 诊疗项目数据维护
│   │   └── MedicalServices.vue    # 医疗服务设施数据维护
│   │
│   └── ReimbursementRates/        # 报销比例维护
│       ├── DrugRates.vue          # 药品报销比例
│       ├── Level3Hospital.vue     # 三级医院报销比例
│       ├── Level2Hospital.vue     # 二级医院报销比例
│       └── Level1Hospital.vue     # 一级医院报销比例
│
├── doctor-station/                # 医院住院医生站医嘱处理模块
│   ├── Admission.vue              # 入院登记
│   │
│   ├── Diagnosis/                # 患者诊断
│   │   ├── PatientSelect.vue      # 患者选择
│   │   ├── AdmissionDiagnosis.vue # 入院诊断
│   │   ├── PrimaryDiagnosis.vue  # 主要诊断
│   │   └── OtherDiagnosis.vue    # 其它诊断
│   │
│   └── MedicalOrders/             # 患者医嘱
│       ├── PatientSelect.vue      # 患者选择
│       ├── DrugOrders.vue         # 药品处方医嘱
│       ├── TreatmentOrders.vue    # 治疗项目医嘱
│       └── ServiceOrders.vue      # 医疗服务医嘱
│
└── insurance-center/              # 医保中心报销管理模块
    ├── InsuredInfo.vue            # 参保人员信息管理
    ├── CostQuery.vue              # 参保人员费用查询
    └── Reimbursement/             # 参保人员费用报销
        ├── CostDetails.vue        # 参保人员费用详情
        └── ReimbursementDetails.vue # 参保人员报销详情
```



### 后端：

#### 医保药品数据维护接口开发

1. 建立 `drugInfo` 数据库表，并插入 `sql` 中数据
2. 建立 `DrugInfo` 实体类
3. 完成 `DrugInfoService` 层的开发
4. 完成 `DrugInfoController` 层的开发
   1. addDrugInfo（新增）:ok:
   2. deleteDrugInfo（删除）:ok:
   3. updateDrugInfo（修改）:ok:
   4. getDrugInfoByName（根据`chineseName`查询药品信息）:ok:
   5. listDrugInfoByPage（分页获取药品信息）:ok:

      1. 开发时，这里有`bug` 

         使用分页查询时，不能正常分页查询，而是查询了整个数据库的数据出来，经过问题分析，发现是v3.5.9 版本之后，必须独立安装 Mybatis 分页查询插件

      2. 解决方案：在`pom.xml`文件中添加分页插件

      ```xml
      <!-- MyBatis Plus 分页插件 -->
      <dependency>
          <groupId>com.baomidou</groupId>
          <artifactId>mybatis-plus-jsqlparser-4.9</artifactId>
      </dependency>
      
      <!-- 补充 MyBatis-Plus-bom -->
      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-dependencies</artifactId>
                  <version>${spring-boot.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
              <dependency>
                  <groupId>com.baomidou</groupId>
                  <artifactId>mybatis-plus-bom</artifactId>
                  <version>3.5.9</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>
      
      ```

      c. 最后在`config`包下，新建`MyBatisPlusConfig`拦截器配置

      ```java
      @Configuration
      @MapperScan("com.gjj.nmcbackend.mapper")
      public class MyBatisPlusConfig {
      
          /**
           * 拦截器配置
           *
           * @return {@link MybatisPlusInterceptor}
           */
          @Bean
          public MybatisPlusInterceptor mybatisPlusInterceptor() {
              MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
              // 分页插件
              interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
              return interceptor;
          }
      }
      
      ```

   6. listAllDrugInfo（获取选中全部药品 `id` ）
   7. deleteDrugInfoByIds（批量删除）
5. 接口文档调试
6. 用umiOpenAPI生成前端调用后端 api 请求代码













```ts
import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from '@/pages/user/userLoginPage.vue'
import HomePage from '@/pages/HomePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage
    },
    {
      path: '/user/login',
      name: '用户登录',
      component: UserLoginPage
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router

```
