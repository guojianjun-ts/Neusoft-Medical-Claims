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

1. add 接口：`addPatient`
2. update 接口：`updatePatient`
3. listPage 分页查询接口：`listPatientVOByPage`
4. 根据 id 查接口：`listPatientByPage` ---> 之后需要重命名为：`getCurrentPatient`

## 入院诊断流程：

1. 点击查看某条患者信息后，页面下方需要展现患者的详细信息（`getCurrentPatient`）
2. 点击导航栏的增加入院诊断后，进入入院诊断页面。
3. 页面标题提供当前所选中病患的姓名
4. 之后是一个可以根据疾病名称（`diseaseName`）的支持分页查询的表格
5. 点击增加按钮后，根据页面的不同（入院诊断、主要诊断、其他诊断）传入一个 `diseaseType（tinyint）` 的值，且根据当前选中病患的信息，给出他的 `id（int）`，并且根据添加选中的疾病信息，给出一个 `diseaseId`，并且根据当前时间，给出一个 `orderTime（datetime）`，之后将这四个参数传入至后端接口中，后端存储该数据。
6. 如果是已经添加的疾病，那么就会显示 `取消添加`，点击之后，会在数据库中对该记录进行删除



## InpatientDrug开发流程

1. 在选择患者页面，选择患者xxx后，点击页面顶部的增加药品处方医嘱，跳转至药品处方医嘱。

2. 跳转至药品处方医嘱页面时，会拥有 患者id（`patientId`），在点击某一个药品进行添加后，会获取药品id（`drugId`），随后会弹出一个表单，需要填写 `AddInpatientDrugRequest`封装类的剩下的参数

   ```java
   package com.gjj.nmcbackend.model.dto.Inpatient;
   
   import lombok.Data;
   
   import java.util.Date;
   
   @Data
   public class AddInpatientDrugRequest {
   
       /**
        * 患者id
        */
       private Integer patientId;
   
       /**
        * 药品id
        */
       private Integer drugId;
   
   
       /**
        * 医嘱内容
        */
       private String doctorOrder;
   
       /**
        * 药品用法
        */
       private String useMethod;
   
   
       /**
        * 用药频率
        */
       private Integer orderNumber;
   
       /**
        * 用药起始时间
        */
       private Date startTime;
   
       /**
        * 用药结束时间
        */
       private Date endTime;
   
   }
   
   ```

3. 随后，记录通过 `InpatientDrugService` MyBatis-Plus 的 `save` 存储到数据库中 `InpatientDrug` 中

4. 药品处方医嘱页面需要分页（支持使用药品名称（`chinaName`）来分页查询），展示的数据为DrugInfo封装类：`InpatientDrugVO`

   ```java
   package com.gjj.nmcbackend.model.vo;
   
   public class InpatientDrugVO {
   
       /**
        * 药品id
        */
       private Integer id;
   
       /**
        * 药品中文名称
        */
       private String chinaName;
   
       /**
        * 规格
        */
       private String specifications;
   
       /**
        * 生产企业
        */
       private String drugManufacturer;
   
       /**
        * 价格
        */
       private String drugPrice;
   }
   
   ```

   



我要做一个患者医嘱模块中的患者选择页面，使用的是Vue3和ant-design-vue组件库：页面结构如下：

1. 上方为一个标题：医嘱患者选择：{{患者姓名}}
2. 标题下方是一个导航栏，选中了表格中的患者后（只能选中一个），可以点击导航栏中三个页面跳转按按钮，分别是：
   1. 增加药品处方医嘱
   2. 增加诊疗项目医嘱
   3. 增加医疗服务医嘱
3. 导航栏下方，是一个表格（有所有患者的粗略信息），这个表格的左上方是个支持根据患者姓名查询的搜索框，且表格支持分页查询
4. 选中了一个患者后，在表格的下方，会有一个患者的表单，显示着患者的详细信息

另外，patientRegistrationController.ts的api接口代码如下：



同时，关于后端实体类的相关api接口定义如下（typing.d.ts文件）



请你根据以上信息，开发一下：src/pages/doctor-station/MedicalOrders/PatientSelect.vue页面





#### 药品处方医嘱页面开发：

1. 顶部和原来患者选择差不多，展示着一个标题：医嘱患者：{{患者姓名}}

2. 接着有另一个标题：药品处方医嘱

3. 随后就是一个可以支持通过搜索药品名称的分页查询表格

4. 表格最右边是一个增加药品的列，列中是对当前药品添加的按钮，当然也可以取消添加（就是删除当前选中药品）

5. 这是我相关的后端接口API代码：你可以根据这个自己分析一下都提供了哪些供你使用的能力

6. 之后是一个相关类声明：

   请你通过以上的信息，给我写那个跳转页面，vue3+antdesigvue4



#### 参保人员信息管理页面：

1. 里面用 带搜索框的分页表格展示着所有参保（支付类型为医保）人员的 VO封装数据（我要起个封装类名字 InsurancePatientVO怎么样）
   1. 参保人员id
   2. 住院号
   3. 姓名
   4. 身份证
   5. 性别
   6. 年龄
2. 选中某一个参保人员，右边会浮现起一个卡片，卡片会展示用户的详细信息，同时也可对用户信息进行修改。可修改字段如下：
   - 姓名
   - 身份证号
   - 年龄
   - 出生日期
   - 性别
   - 家庭住址
   - 结算类别
   - 工作状态

3. 在实时的表单中，点击了修改后，会调用后端，修改用户的信息



## 参保人员费用查询页面开发： [CostQuery.vue](NMC-Frontend\src\pages\insurance-center\CostQuery.vue) 



1. 后端：通过选中的病患id（`patientId`）来返回 由 `inpatient_diagnosis（患者诊疗项目表）`，`inpatient_drug（患者药品处方表）`，
   `inpatient_medical（患者医疗服务表）`这三个表（关联表，关联`patient_registration（病患信息表）`和`diagnosis_treatment（诊疗项目信息表）`，
   `drug_info（药品信息表）`，`medical_service（医疗服务信息表）`这三个
2. 前端：已经完成



## 参保人员费用报销页面开发：

### 1.费用报销的参保人员信息查询页面开发（需要你想个src/pages/insurance-center/Reimbursement目录下的一个Vue文件）：

前端部分：

- 第一行是个标题：费用报销的参保人员信息查询：
- 第二行是表格的搜索栏，可以根据参保人员姓名进行查询（）
- 搜索栏下面是个表格，有以下的字段：
  - 序号
  - 住院号
  - 姓名
  - 性别
  - 身份证号
  - 家庭住址
  - 入院日期
  - 工作状态
  - 结算类别
  - 操作（参保人员费用详情 和 参保人员费用报销的跳转按钮）

后端部分：

- 直接使用`InsuranceController.java`中下面的接口方法就好

- ```java
  
      /**
       * 根据患者id分页查询参保人员药品
       */
      @GetMapping("/cost/drug/list")
      public BaseResponse<Page<DrugCostVO>> listDrugCostByPatientId(
              @RequestParam Integer patientId,
              @RequestParam(defaultValue = "1") Integer current,
              @RequestParam(defaultValue = "5") Integer size) {
          return ResultUtils.success(inpatientDrugService.listDrugCostByPatientId(patientId, current, size));
      }
  ```

- 给你我的 [insuranceController.ts](NMC-Frontend\src\api\insuranceController.ts) 和 [typings.d.ts](NMC-Frontend\src\api\typings.d.ts) 代码

- 另外，我一般获取数据使用的是 res.data.data这样



### 2.参保人员费用详情（src/pages/insurance-center/Reimbursement/CostDetails.vue）：

前端结构：

- 第一行是个标题：参保人员费用报销详情

- 第二行是由参保人员的多个字段组成的一行，例如：
  - 姓名：
  - 性别：
  - 住院号：以上姓名`patientName`、性别`gender`、住院号`caseNumber`字段通过`src/api/patientRegistrationController.ts`中的`getCurrentPatientUsingGet`可以获得

  - 总费用：调用获取参保人员总费用的方法得到：`src/api/insuranceCostController.ts`中的`getTotalCostUsingGet`方法

- 第三行是由参保人员的多个诊断组成的：入院诊断、主要诊断和其它诊断：通过`src/api/insuranceController.ts`中的`listDiseaseByPatientIdUsingGet`，并对 `BaseResponsePagePatientDiseaseVO_.PagePatientDiseaseVO_.PatientDiseaseVO[].diseaseType（1/2/3）`进行类型判断 可以获得三者有什么

- 页面接下来的部分中，左侧是药品费用图表（讲述着参保人员甲类、乙类、丙类药品各个的总金额和占比）：通过调用`src/api/insuranceCostController.ts`中的`getDrugCategoryCostUsingGet`可以获得以下的数据，然后我需要你用ECharts图表对它进行可视化饼状图展示

  ```json
  {
    "code": 0,
    "data": {
      "甲类": 20.13,
      "乙类": 45.72,
      "丙类": 6703.99
    },
    "message": "ok"
  }
  ```

  

- 右侧是报销详情图表（讲述着参保人员的保险药品、诊疗项目、医疗服务各个的总金额和百分比）：通过调用`src/api/insuranceCostController.ts`中的`getAllCategoryCostUsingGet`可以获得以下的数据，然后我需要你用ECharts图表对它进行可视化饼状图展示。

- 请你给我写一下这个前端页面呢

后端：

1. 构建一个新的接口层 [InsuranceCostController.java](NMC-Backend\src\main\java\com\gjj\nmcbackend\controller\InsuranceCostController.java) 
2. 需要提供计算当前参保人员所花费的总费用接口（药品+诊疗项目+医疗服务）
3. 需要提供根据 [InsuranceController.java](NMC-Backend\src\main\java\com\gjj\nmcbackend\controller\InsuranceController.java) 中`listDrugCostByPatientId`方法获取到的药品id，根据甲乙丙类型的药品进行一个分类，计算出各类的费用和占比，用于制作ECharts图表
4. 需要再根据需要提供根据 [InsuranceController.java](NMC-Backend\src\main\java\com\gjj\nmcbackend\controller\InsuranceController.java) 中`listDrugCostByPatientId`方法获取到的药品id，`listDiagnosisCostByPatientId`获取到的诊疗项目id，`listServiceCostByPatientId`获取到的医疗服务id来进行一个分类，并计算出各类的费用和占比，用于制作ECharts图表



1.  public BigDecimal calculateTotalCost(Integer patientId) { }开发

2. 我的思路，根据 patientId，去搜索用户的药品花费，诊疗项目花费，医疗服务花费，都花了多少钱，然后加在一起计算得出总的钱。

3. 另外：例如获取诊疗项目花了多少钱，我在InpatientDiagnosisService.java中定义了下面的listDiagnosisCostByPatientId方法

4. ```java
       @Resource
       private DiagnosisTreatmentService diagnosisInfoService;
   
       @Resource
       private DiagnosisTreatmentMapper diagnosisTreatmentMapper;
   
   	@Override
       public Page<DiagnosisCostVO> listDiagnosisCostByPatientId(Integer patientId, Integer current, Integer size) {
           // 1. 查询患者诊疗项目记录
           QueryWrapper<InpatientDiagnosis> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("patientId", patientId)
                   .eq("status", 1); // 只查询正常执行的医嘱
   
           Page<InpatientDiagnosis> inpatientDiagnosisPage = this.page(new Page<>(current, size), queryWrapper);
   
           // 2. 获取诊疗项目ID列表
           List<Integer> diagnosisIds = inpatientDiagnosisPage.getRecords().stream()
                   .map(InpatientDiagnosis::getDiagnosisId)
                   .collect(Collectors.toList());
   
           if (diagnosisIds.isEmpty()) {
               return new Page<>(current, size, 0);
           }
   
           // 3. 批量查询诊疗项目信息
           QueryWrapper<DiagnosisTreatment> diagnosisQueryWrapper = new QueryWrapper<>();
           diagnosisQueryWrapper.in("id", diagnosisIds);
           List<DiagnosisTreatment> diagnosisTreatments = diagnosisTreatmentMapper.selectList(diagnosisQueryWrapper);
   
           // 4. 构建诊疗项目信息映射表
           Map<Integer, DiagnosisTreatment> diagnosisTreatmentMap = diagnosisTreatments.stream()
                   .collect(Collectors.toMap(DiagnosisTreatment::getId, Function.identity()));
   
           // 5. 构建返回结果
           List<DiagnosisCostVO> diagnosisCostVOs = new ArrayList<>();
           for (InpatientDiagnosis inpatientDiagnosis : inpatientDiagnosisPage.getRecords()) {
               DiagnosisTreatment treatment = diagnosisTreatmentMap.get(inpatientDiagnosis.getDiagnosisId());
               if (treatment != null) {
                   DiagnosisCostVO diagnosisCostVO = new DiagnosisCostVO();
                   // 设置诊疗项目ID
                   diagnosisCostVO.setDiagnosisId(treatment.getId());
                   // 复制诊疗项目信息
                   diagnosisCostVO.setDiagnosisName(treatment.getTreatmentName());
                   diagnosisCostVO.setDiagnosisCode(treatment.getTreatmentNumber());
                   diagnosisCostVO.setExcludeContent(treatment.getTreatmentExclude());
                   diagnosisCostVO.setUnit(treatment.getTreatmentUnit());
                   diagnosisCostVO.setPrice(treatment.getTreatmentPrice());
                   diagnosisCostVO.setExcludeContent(treatment.getTreatmentExclude());
   
                   diagnosisCostVOs.add(diagnosisCostVO);
               }
           }
   
           // 6. 构建分页结果
           Page<DiagnosisCostVO> resultPage = new Page<>(
                   inpatientDiagnosisPage.getCurrent(),
                   inpatientDiagnosisPage.getSize(),
                   inpatientDiagnosisPage.getTotal()
           );
           resultPage.setRecords(diagnosisCostVOs);
   
           return resultPage;
       }
   ```

5. 它可以根据患者id得到它所有的诊疗项目条目，然后我希望你对他进行修改，使得不需要获取分页后的那么多信息，只需要获取所有诊疗项目的钱各个是多少，可以用一个list<bigDemical>方法存起来，然后通过调用这个方法，我们可以通过得到它所有的价钱数组，然后全部加一起，就是所有诊疗项目的钱了，因此，请你给我写一下呢





### 2.参保人员费用详情（src/pages/insurance-center/Reimbursement/CostDetails.vue）：

前端结构：

- 第一行是个标题：参保人员费用报销详情

- 第二行是由参保人员的多个字段组成的一行，例如：

  - 姓名：
  - 性别：
  - 住院号：以上姓名`patientName`、性别`gender`、住院号`caseNumber`字段通过`src/api/patientRegistrationController.ts`中的`getCurrentPatientUsingGet`可以获得

  - 总费用：调用获取参保人员总费用的方法得到：`src/api/insuranceCostController.ts`中的`getTotalCostUsingGet`方法

- 第三行是由参保人员的多个诊断组成的：入院诊断、主要诊断和其它诊断：通过`src/api/insuranceController.ts`中的`listDiseaseByPatientIdUsingGet`，并对 `BaseResponsePagePatientDiseaseVO_.PagePatientDiseaseVO_.PatientDiseaseVO[].diseaseType（1/2/3）`进行类型判断 可以获得三者有什么

- 页面接下来的部分中，左侧是药品费用图表（讲述着参保人员甲类、乙类、丙类药品各个的总金额和占比）：通过调用`src/api/insuranceCostController.ts`中的`getDrugCategoryCostUsingGet`可以获得以下的数据，然后我需要你用ECharts图表对它进行可视化饼状图展示

  

- 右侧是报销详情图表（讲述着参保人员的保险药品、诊疗项目、医疗服务各个的总金额和百分比）：通过调用`src/api/insuranceCostController.ts`中的`getAllCategoryCostUsingGet`可以获得以下的数据，然后我需要你用ECharts图表对它进行可视化饼状图展示。

- 请你给我写一下这个前端页面呢



### 3.参保人员费用报销详情（src/pages/insurance-center/Reimbursement/ReimbursementDetails.vue）：

1. 前端：右上角：返回按钮
   1. 第一行：一个标题：参保人员费用报销

   2. 第二行：显示着参保人员的姓名`patientName`、人员类别`workStatus`、结算类别`paymentType`、总费用（可以通过调用`src/api/insuranceCostController.ts`中的`getTotalCostUsingGet`方法获取）

   3. 接下来是四个卡片：
      1. 第一个卡片：
         - 第一行：药品类型（其实就是甲类药品）：报销比例（可以通过`src/api/drugReimbursementController.ts`中的 `listAllReimbursementUsingGet`方法获得三种类型的报销比例<List<DrugReimbursement>>类型的）返回数据例如：
         
           ```
           {
             "code": 0,
             "data": [
               {
                 "id": 1,
                 "drugReimbursementType": "甲类",
                 "drugReimbursementProportion": 100,
                 "drugReimbursementInfo": "基本医疗范畴已全部覆盖，100％可以报销",
                 "drugStatus": 1
               },
               {
                 "id": 2,
                 "drugReimbursementType": "乙类",
                 "drugReimbursementProportion": 80,
                 "drugReimbursementInfo": "基本医疗范畴未全部覆盖，一般需要个人自付部分",
                 "drugStatus": 1
               },
               {
                 "id": 3,
                 "drugReimbursementType": "丙类",
                 "drugReimbursementProportion": 10,
                 "drugReimbursementInfo": "基本医疗未覆盖",
                 "drugStatus": 1
               }
             ],
             "message": "ok"
           }
           ```
         
           ，该类药品总费用通过`src/api/insuranceCostController.ts`中的`getDrugCategoryCostUsingGet`方法获得各个类药品的花费情况：
           返回数据例如：
         
           ```
           {
             "code": 0,
             "data": {
               "甲类": 20.13,
               "乙类": 45.72,
               "丙类": 6703.99
             },
             "message": "ok"
           }
           ```
         
         - 第二行及之后：一个小表格，有患者购买的该类药品名称，单价，价格三列
      2. 第二个卡片：同第一个卡片（乙类药品），根据药品类型不同而不同
      3. 第三个卡片：同第一个卡片（丙类药品），根据药品类型不同而不同
      4. 第四个卡片：
         - 第一行：写着 其它标题：诊疗项目和医疗服务的总费用
         - 第二行及之后：一个小表格有该参保人员的项目名称，单价，价格
      
   4. 四个小卡片之后，是一个横线标题写着：医保报销费用 = 【( 甲类药品报销费用 + 乙类药品报销费用 + 丙类药品报销费用 + 其他费用) - 起付线 】* 报销比例
   
   5. 接下来是展示医院报销数据的表格：
      1. 有三列分别是：起付线（minPayLevel），等级线（maxPayLevel），报销比例（payProportion要自己前端加个%）
         - 这个需要根据患者的工作状态`workStatus（在职或退休两个字段）`，并结合医院类型`hospitalLevel（默认输入"一级医院"）`，与`peopleTypeDesc（退休人员或在职人员两个字段）`进行分页查询展示出来。（我刚写好这个接口，是`insuranceController.ts`中的`getHospitalReimbursementListUsingGet`）
   
   6. 最后一行左侧是 报销费用：患者的总报销费用，右侧是 确认的按钮（但不需要有任何的动作）（目前这个接口我开发失败了，请）
   
      1. 关于总报销费用，需要用那个横线挑剔的计算公式来进行计算
   
2. 后端：因此根据以上前端的分析，我后端可能是需要去建立两个接口：

   1. 根据患者id的工作状态`workStatus`去匹配医院类型`hospitalLevel`与`peopleTypeDesc`，进行一个分页查询
   2. 一个计算总报销费用的接口：医保报销费用 = 【( 甲类药品报销费用 + 乙类药品报销费用 + 丙类药品报销费用（可以使用insuranceCostService.calculateTotalCost(patientId)获得各个类药品的花费费用再乘以对应药品类型报销比例就是各个类别药品的报销费用了） + 其他费用（可以使用`insuranceCostService.calculateAllCategoryCost(patientId)`方法获取药品、诊疗项目、医疗服务减去药品的费用获得二者相加获得）) - 起付线 （总费用离下面最近的起付线）】* 报销比例





### 后端技术框架

|     技术框架名称      | 作用                                                   |
| :-------------------: | ------------------------------------------------------ |
|  Spring Boot (2.7.6)  | 提供核心框架支持，简化配置和开发流程                   |
|    Spring Web MVC     | 处理HTTP请求和响应，实现RESTful API                    |
| MyBatis-Plus (3.5.9)  | 增强型ORM框架，简化数据库操作，提供CRUD封装            |
| MyBatis-Plus 逻辑删除 | 实现软删除功能（通过isDelete字段标记）                 |
| MyBatis-Plus 分页插件 | 简化分页查询实现                                       |
|   MySQL Connector/J   | 提供MySQL数据库连接支持                                |
|    Knife4j (4.4.0)    | 生成和展示API文档（Swagger增强版）                     |
|        Lombok         | 通过注解自动生成getter/setter/构造方法等，减少样板代码 |
|    Hutool (5.8.26)    | 提供各种Java工具类集合                                 |
|      Spring AOP       | 支持面向切面编程，可用于日志、权限等横切关注点         |
| Spring Boot DevTools  | 开发时热部署支持                                       |
|         Maven         | 项目构建和依赖管理                                     |





## 前端端技术框架

|   技术框架名称    | 作用                                                       |
| :---------------: | ---------------------------------------------------------- |
|       Vue 3       | 前端核心框架，提供响应式组件化开发能力                     |
|    TypeScript     | JavaScript超集，提供静态类型检查，提高代码健壮性和可维护性 |
|       Pinia       | Vue状态管理库，用于集中管理应用状态                        |
|    Vue Router     | Vue官方路由管理器，实现单页面应用的路由控制                |
|  Ant Design Vue   | 企业级UI组件库，提供丰富的预设组件和设计规范               |
|      ECharts      | 百度开源可视化库，用于实现各类数据图表展示                 |
|       Axios       | HTTP客户端，处理前后端数据请求和响应                       |
|       Vite        | 新一代前端构建工具，提供极快的开发服务器启动和热更新       |
|      Vue-TSC      | Vue TypeScript类型检查工具，保证类型安全                   |
| ESLint + Prettier | 代码质量检查与格式化工具，保持代码风格统一                 |
|   Vite DevTools   | Vue开发调试工具，提供组件树查看、状态调试等功能            |
|     Moment.js     | 日期处理库，提供日期格式化、计算等功能                     |
