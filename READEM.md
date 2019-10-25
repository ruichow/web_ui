[TOC]

**梳理测试用例和编写测试脚本之前仔细阅读此文档**
# I.项目结构简介
项目采用 IDEA+Java+maven+Selenium+Testng+jedis+Jenkins持续集成
- test
  - listener
  - pc
  
  
- pc
  - util (工具类)
  - base (底层写有启动驱动关闭驱动等代码)
  - common (存放所有页面公共方法的地方，供page调用)
  - data (数据常量层做数据驱动，目前采用类来保存常量数据，动态数据用jedis存储，也可以考虑用Excel或properties文件存储)
  - locator (PO 思想中存放xpath定位，页面层类)
  - page (PO 思想中存放页面方法，PO 思想的页面类逻辑层，供testcase调用)
  - testcase (PO 思想的业务层)
  
之后 data=> locator => page => testcase中按照各组别以及各功能严格分配，拿testcase包进行举例说明
- testcase
  - tax 税务组
  - money 资金组
  - business 业务组
  - basics 基础组
  - accountingengine 会计引擎组
  
testcase 用例类会先依据是5个组别中哪个组负责分类，之后又会依据该组中属于哪种模块进行分类，以业务组举例说明：

- business 业务组
  - advancepaymentorder 预付款单模块
  - application 申请单模块
  - businesssetting 业务设置模块
  - contract 合同模块
  - cost 支付模块
  - expensewithholding 费用预提单模块
  - feeadjustment 费用调整单模块
  - feeapplication 费用申请单模块
  - payment 费用模块
  - projectmanagement 项目管理模块
  - paymentapplication 付款申请单模块
  
# II.项目编程标准
## I.命名标准
1. 所有包名文件名全部小写，ps:page,base
2. 所有类名全部大驼峰，ps:ProjectTypeDefinitionPage
3. 所有变量以及方法名全部小驼峰，ps:String currentTime
4. 所有常量名全部大写或大写下划线组合，ps:final String DEFAULT_VALUE
5. 所有Java类的命名全部英文
6. locator 包的类中常量命名尽量采用`描述_控件名`的形式，如下所示：
    ```aidl
    public static final By ACCOUNTSET_DIV=By.xpath("//div[@id='setOfBooksId']");
    ```
7. page中的方法请采用`某一被测功能的描述Module`的形式，如下所示：
    ```aidl
    public void newCreateModule(){}
    ```
8. testcase 中的方法请采用`被测功能描述Test`的形式，如下所示：
    ```aidl
    public void searchTest(){}
    ```
    
## II.结构标准
1. 包名有 base => data => locator => page => testcase,不同类型的Java类需要严格放在不同类型包下
2. 不同的功能模块也要严格放在对应的模块包中
3. 调用关系请遵守page中逻辑代码只能调用common中封装方法或自己在page中写的自己逻辑代码；testcase业务代码只能调用
page中的逻辑代码，在testcase中不允许写逻辑性代码；util为工具类的包，供page调用，目前工具类只有ms时间获取，也可以
根据其他需求封装成其他工具类
4. locator中xpath定位都统一封装成By的常量
5. page要求是能少加强制等待(sleep)和智能等待(wait.until)就少加，最好完全不加
6. 逻辑代码中涉及循环操作，特别是拿元素是否存在作为判断条件或类似的判断条件的循环，务必设置一个次数限制出口
7. testcase中不允许出现逻辑性代码，注入判断循环等，逻辑代码全部封装在页面类的操作方法中，此处只允许去调用页面类中的逻辑方法

## III.注释标准
1. 下载插件：Alibaba Java Coding Guidelines，便于编码时候自动检测编码规范
2. 每个类上加上类的注释，如下所示：
    ```aidl
    /**
     * 项目类型定义数据
     */
    ```
    ```aidl
    /**
     * 项目类型定义定位
     */
    ```
    ```aidl
    /**
     * 项目类型定义页面
     */
    ```
    ```aidl
    /**
     * 项目类型定义测试
     */
    ```
3. 每个方法上加上方法注释，如下所示：
    ```aidl
    /**
     * 新建功能
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    ```
4. 在data,locator,testcase中写在方法外的field变量需要添加变量的注释,如下所示：
    ```aidl
    /**
     * 账套定义的 div
     */
    ```
5. 代码行后不允许添加`//`,其他请全部按照Alibaba编码规范进行
6. 在page中的逻辑代码中可以通过添加‘//’来标明执行的操作，勤于添加规范的注释，便于维护
7. 在，data,locator,page中可以添加`/* ============== 搜索区域xpath定位 ============ */`将filed或
function进行分隔，便于阅读代码

## IV.编码标准
1. xpath定位依据id => name => class => 其他形式的定位，以唯一性强的优先定位的策略，不好定位的元素可以采用文本定位
2. testcase中的注解请添加分组、描述以及优先级，便于测试组合用例,如下所示：
    ```aidl
    @Test(groups = "create", description = "项目类型定义模块中新建功能校验", priority = 1)
    ```
3. 可以安装rainbow brackets插件在写代码过程中方便快速识别括号匹配
4. 在 page 类中的每个方法里头最前面请加上
   ```aidl
   log.info("此处是某某功能");
   ```
