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



## 住院医生板块

一共开发9个页面：

1. 入院登记
   1. 后端开发：
      1. 只与住院患者库表相关：先后端建表
      2. 之后开发后端的增接口
   2. 前端页面开发：
      1. 只是一个表单
2. 患者选择：
   1. 后端开发：
      1. 开发可以根据患者姓名查询接口的分页查询接口
      2. 并且点击查看按钮，可以看到具体的单一患者详细信息
   2. 前端开发：
      1. 导航栏：分别可以添加不同的诊断
      2. 可以根据名字查询患者的Table（分页）
      3. 点击具体某一行，可以返回其详细信息
3. 入院诊断---主要诊断---其它诊断：
   1. 后端开发：
      1. 开发疾病信息表（`disease_info`）与患者诊断疾病表（`inpatient_disease`）
      2. 开发分页且可以根据疾病名称查与添加到患者疾病表的接口
   2. 前端：
      1. 一个Table表格
      2. 表格上方需要根据当前患者的姓名展示标题
      3. 其它两个页面直接复用代码就行
4. 患者医嘱中型模块
   1. 后端开发：
      1. 此模块的患者选择与患者诊断中模块的患者选择一样，不用开发
      2. 开发8、患者医嘱药品信息表、9、患者医嘱诊疗项目信息表、10、患者医嘱医疗服务项目信息表这三个数据库表的接口
   2. 前端开发：
      1. 复用 2 的页面代码，修改`Content`导航栏的跳转路由与字段
      2. 三个不同的xxx医嘱页面
         - 就是可以根据药品名称查询的表格，且支持分页展示数据
         - 另外两个直接复用第一个页面代码





### 患者信息表相关接口：

1. add 接口
2. update 接口
3. listPage 分页查询接口
4. 根据 id 查接口













