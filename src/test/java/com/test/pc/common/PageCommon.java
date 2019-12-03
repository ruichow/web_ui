package com.test.pc.common;

import com.hand.constant.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * 页面公用方法封装
 *
 * @author：关河九州
 * @date：2019/11/29 15:09
 * @version：1.0
 */
@Slf4j
public class PageCommon {
    /**
     * 驱动
     */
    protected WebDriver driver;

    /**
     * 驱动等待
     */
    protected WebDriverWait wait;

    /**
     * 构造器
     *
     * @param driver 子类传入的驱动
     */
    public PageCommon(WebDriver driver) {
        //驱动
        this.driver = driver;
        //驱动等待
        this.wait = new WebDriverWait(driver, BaseConstant.TWENTY);
    }

    /* =========================== 页面基本操作 ===========================*/
    /**
     *通过url进行页面跳转
     * @param url 指定功能模块的url
     */
    protected void jumpByUrl(String url){
        //通过url进行跳转
        driver.get(url);
    }

    /**
     *浏览器滑动到最顶端
     */
    protected void scrollToTop(){
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
    }

    /**
     * 浏览器滑动到最底部
     */
    protected void scrollToBottom(){
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight");
    }

    /**
     * 滚动滚动条，让元素顶端和页面顶端对齐
     * @param by 需要对齐的元素
     */
    protected void scrollElementTopToTop(By by){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);",driver.findElement(by));
    }

    /**
     * 点击顶部的可关闭的第一个tab
     */
    protected void closeFirstTab() throws Exception{
        clickButton(By.xpath("//div[@class='ant-tabs-nav-scroll']/div/div/div[2]//i"));
    }

    /* =========================== 基本元素操作 ===========================*/
    /**
     *点击按钮操作，封装延时500ms
     * @param by 点击按钮的定位
     */
    protected void clickButton(By by) throws Exception{
        //点击之前保证页面上所有的转圈是加载完的
        for(int i = 0; i < 300; i++){
            if(!isElementContained("//span[@class='ant-spin-dot ant-spin-dot-spin']")){
                break;
            }
            Thread.sleep(100);
        }
        //等到页面加载存在并可见并且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement buttonElement=driver.findElement(by);
        buttonElement.click();
    }

    /**
     * 点击按钮操作，外部延时传参
     *
     * @param beforeTime ms 时间表示点击之前等待多少时延
     * @param by 点击按钮的定位
     * @param afterTime ms 时间表示点击之后等待多时时延
     */
    protected void clickButton(long beforeTime,By by,long afterTime) throws Exception{
        //点击之前保证页面上所有的转圈加载是加载完成的
        for (int i = 0; i < 300; i++){
            if (!isElementContained("//span[@class='ant-spin-dot ant-spin-dot-spin']")){
                break;
            }
            Thread.sleep(100);
        }
        //点击之前延时
        Thread.sleep(beforeTime);
        //等到页面加载存在并可见并且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement buttonElement = driver.findElement(by);
        buttonElement.click();
        //点击之后延时
        Thread.sleep(afterTime);
    }

    /**
     * input框出现后就去点击先清空然后输入
     *
     * @param by input的by定位
     * @param keysToSend 要输入的数据
     *
     */
    protected void inputData(By by,CharSequence... keysToSend) throws Exception{
        //点击之前保证页面上所有的转圈加载是加载完成的
        for (int i = 0; i < 300; i++){
            if (!isElementContained("//span[@class='ant-spin-dot ant-spin-dot-spin']")){
                break;
            }
            Thread.sleep(100);
        }
        //等到页面加载存在input并可见且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement inputElement = driver.findElement(by);
        Actions actions = new Actions(driver);
        actions.click(inputElement).perform();
        //清空input，输入
        inputElement.clear();
        inputElement.sendKeys(keysToSend);
    }

    /**
     * input框出现后就去点击，先清空，然后输入
     *
     * @param element input的元素
     * @param keysToSend 要输入的数据
     */
    protected void inputData(WebElement element,CharSequence... keysToSend) throws Exception{
        //点击之前保证页面上所有的转圈加载是加载完成的
        for (int i = 0; i < 300; i++){
            if (!isElementContained("//span[@class='ant-spin-dot ant-spin-dot-spin")){
                break;
            }
            Thread.sleep(100);
        }
        //等到页面加载存在input并可见且可点击
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        //清空input，输入
        element.clear();
        element.sendKeys(keysToSend);
    }

    /**
     * 下拉框点击并选中所需要的选项
     * @param by 下拉框定位
     * @param ulBy 下拉列表ul定位
     * @param liStr 下拉列表中需要选中的一行中的文本
     */
    protected void selectDropBox(By by,By ulBy,String liStr) throws Exception{
        //等到下拉框存在并可见且可点击
        clickButton(by);
        //等到下拉列表（ul）出现后，就在此列表中进行滑动到顶端的操作
        wait.until(ExpectedConditions.visibilityOfElementLocated(ulBy));
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(ulBy));
        scrollElementTopToTop(By.xpath(liStr));
        //等到需要选中的文本可见并且可被点击的时候进行操作
        clickButton(By.xpath(liStr));
    }

    /* ==================== 简单共用方法操作 ====================*/

    /**
     * 判断元素是否存在，在页面加载完全后可用
     *
     * @param str 元素xpath定位
     * @return 返回元素是否存在的布尔值
     */
    private boolean isElementContained(String str) {
        //将隐式等待关闭，或者设置成等待时间非常小
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean flag = true;
        try {
            //若找不到元素，此处抛异常
            driver.findElement(By.xpath(str));
        }catch (Exception e){
            //如果抛出异常，即找不到元素
            flag = false;
        }finally {
            //还原隐式等待延时
            driver.manage().timeouts().implicitlyWait(BaseConstant.TWENTY,TimeUnit.SECONDS);
            return flag;
        }
    }

    /**
     * 新建或者搜索中一系列的输入框进行输入
     * 注意：此方法为复杂方法，建议使用，通过传一个字符数组对所有可以更改的输入框进行输入
     *
     * @param boxType 新建或者搜索框的类型，是滑动框slide，还是横态框modal,还是一个页面显示tab
     * @param data "" 表示为新建操作，有值表示搜索操作
     * @throws Exception 当输入的boxType类型有问题时抛出异常
     */
    protected void inputAllData(String boxType, String data[]) throws Exception {
        /* ========== 多种类型框的 xpath 定位 ========== */
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";
        // 里头块级元素个数
        int divNum = 0;
        // 下拉框（非默认勾选）
        String selectBox = "";
        // 普通输入框
        String inputBox = "";
        // 开关按钮
        String switchBox = "";
        // 日期输入框
        String dateBox = "";
        // 数量输入框
        String numBox = "";
        // 日期选择框
        String dateBox2 = "";
        // 弹出框
        String modalBox = "";
        /* ========== 判断弹出框是滑动框还是模态框还是标签框 ========== */
        // 如果匹配到 slide 滑动框
        if(boxType.equals("slide")){
            log.info("现在匹配到滑动框");
            parentDiv = "//div[@class='slide-frame animated slideInRight']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 modal 模态框
        else if(boxType.equals("modal")){
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 tab 标签框
        else if(boxType.equals("tab")){
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 或者什么都没匹配到
        else{
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        /* ========== 遍历各个 div 模块查找其中各种类型元素 ========== */
        // 遍历框中这几个块级元素
        for(int i = 1; i<= divNum; i++){
            // 下拉框
            selectBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-enabled') and not(contains(@class,'ant-select-no-arrow'))]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[@placeholder='请输入' and not(@disabled)]";
            // 开关按钮
            switchBox = "(" + childDiv + ")[" + i + "]//button[@class='ant-switch']";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input-number-input' and not(@disabled)]";
            // 日期选择框
            dateBox2 = "" + childDiv + "";
            // 下拉框在此块级元素中存在
            if(isElementContained(selectBox)){
                // 点击下拉框
                clickButton(0, By.xpath(selectBox), 0);
                // Actions
                Actions actions = new Actions(driver);
                // controls
                String controls = driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]//span[@class='ant-form-item-children']/div/div")).getAttribute("aria-controls");
                // 等 10 s 直到下拉框中空白或者转圈消失
                for(int index = 0; index < 100; index++){
                    if(!isElementContained("//div[@id='" + controls + "']//div[@class='ant-empty-image']") && !isElementContained("//div[@id='" + controls + "']//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")){
                        break;
                    }
                    Thread.sleep(100);
                }
                // 如果数组中该文本数据为空，默认选取第一行
                if(data[i-1].equals("")){
                    actions.sendKeys(Keys.ENTER).perform();
                }
                // 若文本不为空
                else {
                    clickButton(0, By.xpath("//div[@id='" + controls + "']/ul/li[text()='" + data[i - 1] + "']"), 0);
                }
                // 如果下拉框还没消失
                String ulBox = "//div[contains(@class,'ant-select-dropdown-hidden')]" + "/div[@id='" + controls + "']/ul";
                if(!isElementContained(ulBox)){
                    // 避免一种 select 框选择行下拉框无法弹回
                    actions = new Actions(driver);
                    actions.sendKeys(Keys.ESCAPE).perform();
                }
            }
            // 若常规输入框在此块级元素中存在
            else if(isElementContained(inputBox)){
                // 点击输入框并输入数据
                inputData(By.xpath(inputBox), data[i-1]);
            }
            // 若开关按钮在此块级元素中存在
            else if(isElementContained(switchBox)){
                // 点击开关按钮
                clickButton(By.xpath(switchBox));
            }
            // 若日期输入框在此块级元素中存在
            else if(isElementContained(dateBox)){
                // 目前用点击后输入日期回车来处理
                clickButton(By.xpath(dateBox));
                Actions actions = new Actions(driver);
                actions.sendKeys(data[i-1], Keys.ENTER).perform();
            }
            // 若数量输入框在此块级元素中存在
            else if(isElementContained(numBox)){
                // 目前暂时用 1 来输入
                inputData(By.xpath(numBox), data[i-1]);
            }
        }
    }

    /**
     * 判断搜索框中数据是否被清空，返回布尔类型
     *
     * @param boxType 新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @return 返回各个框中数据是否被清空的布尔类型
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected boolean isCleared(String boxType) throws Exception {
        /* ========== 多种类型框的 xpath 定位 ========== */
        // 返回的布尔值
        boolean flag = true;
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";
        // 里头块级元素个数
        int divNum = 0;
        // 下拉框（非默认勾选）
        String selectBox = "";
        // 普通输入框
        String inputBox = "";
        // 开关按钮
        String switchBox = "";
        // 日期输入框
        String dateBox = "";
        // 数量输入框
        String numBox = "";
        // 日期选择框
        String dateBox2 = "";
        // 弹出框
        String modalBox = "";
        /* ========== 判断弹出框是滑动框还是模态框还是标签框 ========== */
        // 如果匹配到 slide 滑动框
        if(boxType.equals("slide")){
            log.info("现在匹配到滑动框");
            parentDiv = "//div[@class='slide-frame animated slideInRight']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 modal 模态框
        else if(boxType.equals("modal")){
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 tab 标签框
        else if(boxType.equals("tab")){
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 或者什么都没匹配到
        else{
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        /* ========== 遍历各个 div 模块查找其中各种类型元素 ========== */
        // 遍历框中这几个块级元素
        for(int i = 1; i<= divNum; i++){
            // 下拉框
            selectBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-enabled')]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[@placeholder='请输入' and not(@disabled)]";
            // 开关按钮
            switchBox = "(" + childDiv + ")[" + i + "]//button[@class='ant-switch']";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input-number-input']";
            // 日期选择框
            dateBox2 = "" + childDiv + "";
            // 下拉框在此块级元素中存在
            if(isElementContained(selectBox)){
                // 如果是不被允许清空的下拉框，即判断有一个红色的星号
                if(driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")){
                    String controls = driver.findElement(By.xpath(selectBox + "/div[1]")).getAttribute("aria-controls");
                    String text = driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText();
                    clickButton(0, By.xpath(selectBox), 500);
                    String li1Box = driver.findElement(By.xpath("//div[@id='"+ controls +"']//ul/li[1]")).getText();
                    if(!text.equals(li1Box)){
                        flag = false;
                    }
                    Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.ESCAPE).perform();
                    Thread.sleep(500);
                }
                // 如果是被允许清空的下拉框
                else{
                    if(isElementContained(selectBox + "//div[@class='ant-select-selection-selected-value']")){
                        flag = false;
                    }
                }
            }
            // 若常规输入框在此块级元素中存在
            else if(isElementContained(inputBox)){
                if(!(driver.findElement(By.xpath(inputBox)).getText().equals("") || driver.findElement(By.xpath(inputBox)).getText() == null)){
                    flag = false;
                }
            }
            // 若日期输入框在此块级元素中存在
            else if(isElementContained(dateBox)){
                if(!(driver.findElement(By.xpath(dateBox)).getText().equals("") || driver.findElement(By.xpath(dateBox)).getText() == null)){
                    flag = false;
                }
            }
            // 若数量输入框在此块级元素中存在
            else if(isElementContained(numBox)){
                if(!(driver.findElement(By.xpath(numBox)).getText().equals("") || driver.findElement(By.xpath(numBox)).getText() == null)){
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 模态框中数据进行输入操作，搜索操作，勾选搜索到数据的操作，最后点击模态框最下面的确定按钮
     *
     * @param data 输入数据数组
     * @throws Exception inputAllData 中抛出
     */
    protected void inputModal(String data[]) throws Exception {
        // 模态框中输入数据
        inputAllData("modal", data);
        // 点击搜索按钮
        clickButton(0, By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-body']//form//button[@class='ant-btn ant-btn-primary']"), 500);
        // table 中点击第一行第一列
        clickButton(By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']//table/tbody/tr[1]/td[1]"));
        // 点击 footer 中的蓝色确定按钮
        clickButton(0, By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary']"), 500);
    }

    /**
     * 页码选择操作
     *
     * @param value 参数一（非必传）：boxType 可传可不传，tab，slide，model，传的话定位更准确一些
     *               参数二：rowsPerPage 多少条数据/每页，为 "" 表示默认当前
     *               参数三：page 第几页，此处可以为 ""，表示 input 框不去输入
     * @exception Exception  跳转第几页的 input 不允许为空
     */
    protected void choosePageNum(String... value) throws Exception {
        // 页码所在框的类型，默认是 tab
        String boxType = "";
        // 多少条数据/每页 的文本
        String rowsPerPage;
        // 第几页的文本
        String page;
        // 若参数传了 null
        if(value != null){
            // 若没有传参
            if(value.length != 2 && value.length != 3){
                throw new Exception("参数要么传 2 个要么传 3 个值！");
            }
            // 若传了 2 个值
            else if(value.length == 2){
                rowsPerPage = value[0];
                page = value[1];
            }
            // 若传了 3 个值
            else{
                boxType = value[0];
                rowsPerPage = value[1];
                page = value[2];
            }
        }
        // 否则抛出参数没传的异常
        else{
            throw new Exception("参数不允许为 null！");
        }

        // 父框 div，默认是 tab
        String parentDiv = "";
        // 如果匹配到 slide 滑动框
        if(boxType.equals("slide")){
            log.info("现在匹配到滑动框");
            parentDiv = "//div[@class='slide-frame animated slideInRight']";
        }
        // 如果匹配到 modal 模态框
        else if(boxType.equals("modal")){
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']";
        }
        // 如果匹配到 tab 标签框
        else if(boxType.equals("tab")){
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
        }
        // 或者什么都没匹配到
        else if(!boxType.equals("")){
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }

        // 定位到选择每页多少条的下拉框
        String selectBox = parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[1]/div";
        // 定位到下拉框是哪一个 li
        String liBox;
        // 定位到跳转到哪一页的 input 框
        String inputBox = parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[2]/input";
        // 只有当 rowsPerPage 不为 "" 或者不为 null 时候才去选择每页多少条数据，否则使用默认
        if(rowsPerPage != null && !rowsPerPage.equals("")){
            liBox = parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[last()]//ul/li[text()='" + rowsPerPage + "']";
            // 点击选择多少条的下拉框
            clickButton(0, By.xpath(selectBox), 500);
            // 点击指定的 li
            clickButton(0, By.xpath(liBox), 500);
        }
        // input 中输入需要跳转第几页，其传值可以为 ""，但是不允许为 null
        if(page == null){
            throw new Exception("跳转第几页的 input 不允许为 null");
        }
        // 如果 input 框存在，就点击跳转到第几页的 input 框再按下回车
        if(isElementContained(inputBox)) {
            inputData(By.xpath(inputBox), page, Keys.ENTER);
            Thread.sleep(500);
        }
    }

    /**
     * 传入 tab 或者 modal 或者 slide，一般都是传入 tab，然后返回该页面列表中可以显示的行数
     *
     * @param boxType boxType 新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @return 返回该页面中列表的行数 int 类型
     * @throws Exception 你输入的弹框类型有误或者暂不支持！
     */
    protected int rowsOfTable(String boxType) throws Exception {
        // 外框定位
        String parentDiv = "";
        // table 中 row 定位
        String tableRowsBox = "";
        // 该页面 table 中 row 数量
        int tableRowsNum;
        // 如果匹配到 tab 标签页
        if(boxType.equals("tab")){
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
        }
        // 如果匹配到 modal 模态框
        else if(boxType.equals("modal")){
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
        }
        // 如果匹配到 slide 滑动框
        else if(boxType.equals("slide")){
            log.info("现在匹配到滑动框");
            parentDiv = "//div[@class='slide-frame animated slideInRight']";
        }
        // 或者什么都没匹配到
        else{
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        tableRowsBox = parentDiv + "//div[@class='ant-table-wrapper']//table/tbody/tr";
        tableRowsNum = driver.findElements(By.xpath(tableRowsBox)).size();
        return tableRowsNum;
    }

    /**
     * 传当前框类型，被检测数据数组，需要匹配的列数组，返回是否匹配成功的布尔值
     *
     * @param boxType tab，modal 或者 slide
     * @param data 需要和 table 列表中第几列作比较的 String 数组数据
     * @param td 需要和 data[] 数组中数据作比较的列数 int 数组。PS：data["001", "张三", "男"]，td[1, 3, 6]，即表示检测"001"
     * 是否在第 1 列中被检测到，并且"张三"是否在第 3 列中被检测到，并且"男"是否在第 6 列被检测到，返回布尔类型
     * @return 返回 data[] 数据是否在 td[] 这些列中被检测到的布尔值
     * @exception Exception 你输入的弹框类型有误或者暂不支持
     */
    protected boolean isDataMatchTable(String boxType, String[] data, int[] td) throws Exception {
        boolean flag = true;
        // 外框定位
        String parentDiv = "";
        // 当前 table 中是第几行
        String tableRowBox = "";
        // 当前 table 中是第几列
        String tableColumnBox = "";
        // 如果匹配到 tab 标签页
        if(boxType.equals("tab")){
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
        }
        // 如果匹配到 modal 模态框
        else if(boxType.equals("modal")){
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
        }
        // 如果匹配到 slide 滑动框
        else if(boxType.equals("slide")){
            log.info("现在匹配到滑动框");
            parentDiv = "//div[@class='slide-frame animated slideInRight']";
        }
        // 或者什么都没匹配到
        else{
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        // 滑动使得列表和顶端对齐
        scrollElementTopToTop(By.xpath(parentDiv + "//div[@class='ant-table-wrapper']//table/tbody"));
        // 循环列表中所有的行
        for(int i = 1; i <= rowsOfTable(boxType); i++){
            // 若 flag 不真直接跳出
            if(!flag){
                break;
            }
            // 当前在哪一行
            tableRowBox = parentDiv + "//div[@class='ant-table-wrapper']//table/tbody/tr[" + i + "]";
            // 循环 td[] 中指定的列
            for(int j = 0; j < td.length; j++){
                // 若 flag 不真直接跳出
                if(!flag){
                    break;
                }
                // 当前在哪一列
                tableColumnBox = tableRowBox + "/td[" + td[j] + "]";
                // 若该行该列值不匹配，则 flag 置为 false
                if(!driver.findElement(By.xpath(tableColumnBox)).getText().contains(data[j])){
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 传入"success"或者"error"，判断 Toast 弹框类型是否符合要求的布尔值
     *
     * @param toastStatus Toast 弹框状态，可填"success"或者"error"
     * @return 返回弹框类型是否符合的布尔值
     * @throws Exception 目前暂不支持的 Toast 状态！
     */
    protected boolean isToastCorrect(String toastStatus) throws Exception {
        boolean flag;
        // 弹框 xpath
        String toastBox = "//div[@class='ant-message-notice-content']/div";
        // 等到弹框可见
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(toastBox)));
        // 如果是 success
        if(toastStatus.equals("success")){
            flag = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("success");
            for(int i = 0; i < 50; i++){
                Thread.sleep(100);
                if(!isElementContained("//div[@class='ant-message-notice-content']/div")){
                    break;
                }
            }
            return flag;
        }
        else if(toastStatus.equals("error")){
            flag = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("error");
            for(int i = 0; i < 50; i++){
                Thread.sleep(100);
                if(!isElementContained("//div[@class='ant-message-notice-content']/div")){
                    break;
                }
            }
            return flag;
        }
        else{
            throw new Exception("目前暂不支持的 Toast 状态！");
        }
    }
}
