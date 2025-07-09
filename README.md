# Nesoft-Medical-Claims 开发日志

## 医疗基本信息维护模块

### 前端：

#### 数据信息维护

1. 医保药品数据维护（table+BasicLayout）
2. 诊疗项目数据维护
3. 医疗服务设施数据维护

#### 报销比例维护

1. 药品报销比例
2. 三级医院报销比例
3. 二级医院报销比例
4. 一级医院报销比例

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
   5. listDrugInfoByPage（分页获取药品信息）
   
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
