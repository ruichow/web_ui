package com.test.pc.base;

import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

/**
 * @author：关河九州
 * @date：2019/11/25 20:34
 * @version：1.0
 */
public class PageBaseOpt {
    //驱动
    protected WebDriver driver;
    protected WebDriverWait wait;
    //环境，表示是生产、测试还是uat
    public String environment;
    //语言,zh_CN zh_HK en_GB对应着简体、繁体、英文
    public String language;
    //数据驱动数组下标，num1表示是生产还是uat，num2表示是大陆还是香港繁体或者香港英文
    public int num1;
    public int num2;
    //每个页面保存至其中的数据
    public JSONObject jsonObject;

    /**
     * 构造器
     *
     * @param driver   驱动
     * @param language 语言
     */
    public PageBaseOpt(WebDriver driver, String environment, String language) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 20);
        this.environment = environment;
        this.language = language;
        this.jsonObject = new JSONObject();
        if (environment.equals("prod")) {
            num1 = 0;
            if (language.equals("zh_CN")) {
                num2 = 0;
            } else if (language.equals("zh_HK")) {
                num2 = 1;
            } else if (language.equals("en_GB")) {
                num2 = 2;
            }
        } else if (environment.equals("uat")) {
            num1 = 1;
            if (language.equals("zh_CN")) {
                num2 = 0;
            } else if (language.equals("zh_HK")) {
                num2 = 1;
            } else if (language.equals("en_GB")) {
                num2 = 2;
            }
        }
    }

    /**
     * 构造器
     * 有的页面在跳转时存在页面加载中存在元素点击不到的情况，可以使用此构造器（多传一个wait,wait和driver类似），在base包的方法前加上显式等待即可
     *
     * @param driver      驱动
     * @param wait        显式等待
     * @param environment 环境
     * @param language    语言
     */
    public PageBaseOpt(WebDriver driver, WebDriverWait wait, String environment, String language) {
        this.driver = driver;
        this.wait = wait;
        this.environment = environment;
        this.language = language;
        this.jsonObject = new JSONObject();
        if (environment.equals("prod")) {
            num1 = 0;
            if (language.equals("zh_CN")) {
                num2 = 0;
            } else if (language.equals("zh_HK")) {
                num2 = 1;
            } else if (language.equals("en_GB")) {
                num2 = 2;
            }
        } else if (environment.equals("uat")) {
            num1 = 1;
            if (language.equals("zh_CN")) {
                num2 = 0;
            } else if (language.equals("zh_HK")) {
                num2 = 1;
            } else if (language.equals("en_GB")) {
                num2 = 2;
            }
        }

    }
    /* ========== 页面刷新跳转操作 ========== */

    /**
     * 获取当前page的url
     *
     * @return String
     */
    public String getCurrnetPageUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * 通过url进入某个页面
     *
     * @param url
     */
    public void enterByUrl(String url) {
        try {
            driver.navigate().to(url);
        } catch (TimeoutException e) {
            freshPage();
        }
        driver.get(url);
    }

    /**
     * 刷新页面
     */
    protected void freshPage() {
        driver.navigate().refresh();
    }

    /* ==========页面滑动操作========== */

    /**
     * 滑动到最底端
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scroll(0,document.body.scrollHeight)");
    }

    /**
     * 滑动到最顶端
     */
    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
    }

    /**
     * 滑动滚动条，让元素底端和页面底端对齐
     *
     * @param by 需要对齐的元素
     */
    protected void scrollElementBottomToBottom(By by) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", driver.findElement(by));
    }

    /* ========== 页面单元素的操作 ==========*/

    /**
     * input 输入框输入字符串
     *
     * @param by     input 输入框定位
     * @param string 需要输入的字符串
     */
    protected void inputText(By by, String string) {
        //找到input输入框
        WebElement webElement = driver.findElement(by);
        //清空input输入框
        webElement.clear();
        //input输入框中输入相应字符
        webElement.sendKeys(string);
    }

    /**
     * @param by
     * @return void
     * @Author 关河九州
     * @Description 元素单击事件
     */
    protected void click(By by) {
        //找到元素
        WebElement webElement = driver.findElement(by);
        //点击元素
        webElement.click();
    }

    /**
     * 元素是否存在，返回布尔值，元素找不到会抛出NoSuchElementException而非不可点击异常
     * 只有NoSuchElementException异常可以被捕获，其他异常会被监听到并且测试结果报红
     * 第二层如果用到元素判断是否存在，第二层调用此方法时方法名可以用verify...来命名
     *
     * @param by 元素定位
     * @return 元素存在true，元素不存在false
     */
    protected boolean elementExists(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * 判断元素是否可点击
     *
     * @param by 是否可点击的按钮
     * @return 返回true或false
     */
    protected boolean elementClickable(By by) {
        WebElement webElement = driver.findElement(by);
        return webElement.isEnabled();
    }

    /**
     * 移动鼠标到需要移动的元素上
     *
     * @param by 需要移动到的元素定位
     */
    protected void moveOnElement(By by) {
        //依据当前驱动创建鼠标属性方法
        Actions actions = new Actions(driver);
        //鼠标移动到想要悬停的元素上
        actions.moveToElement(driver.findElement(by)).perform();
    }

    /* ========== 页面多元素的操作 ========= */

    /**
     * 单个input输入框输入字符并且点击按钮
     *
     * @param inputBy input 输入框
     * @param string  要输入的字段
     * @param btnBy   btn 点击按钮
     */
    protected void inputAndClick(By inputBy, String string, By btnBy) {
        wait.until(ExpectedConditions.elementToBeClickable(inputBy));
        //找到input输入框，清空，输入字符
        inputText(inputBy, string);
        wait.until(ExpectedConditions.elementToBeClickable(btnBy));
        //点击按钮
        driver.findElement(btnBy).click();
    }

    /**
     * 点击单个box框然后再点击按钮
     *
     * @param boxBy
     * @param btnBy
     */
    protected void boxAndClick(By boxBy, By btnBy) {
        //找到并点击box框
        driver.findElement(boxBy).click();
        //点击btn按钮
        driver.findElement(btnBy).click();
    }

    /* ========== 页面UI登录操作 ==========*/

    /**
     * 常规用户密码登录
     *
     * @param username        用户名
     * @param password        密码
     * @param usernameLocator 用户名input定位
     * @param passwordLocator 密码input定位
     * @param btnLocator      登录btn定位
     */
    public void LoginByUI(String username, String password, By usernameLocator, By passwordLocator, By btnLocator) {
        //用户名框输入用户名
        WebElement usernameElement = driver.findElement(usernameLocator);
        usernameElement.clear();
        usernameElement.sendKeys(username);
        //密码框输入密码
        WebElement passwordElement = driver.findElement(passwordLocator);
        passwordElement.clear();
        passwordElement.sendKeys(password);
        //点击登录按钮
        driver.findElement(btnLocator).click();
    }

    /* ========== 页面API登录操作 ========== */

    /**
     * api接口登录返回 access token
     *
     * @param phoneNumber  手机号码（不带区号）
     * @param pwd          公钥加密后密码的密文
     * @param browseNumber 浏览器版本（1 谷歌， 5 谷歌 H5）
     * @return 返回access_token
     */
    public String loginByAPI(String phoneNumber, String pwd, String browseNumber) {
        LoginByAPI loginByAPI = new LoginByAPI();
        return loginByAPI.login(environment, language, driver, phoneNumber, pwd, browseNumber);
    }

    /* ========== 跳转到第几页的操作 ==========*/

    /**
     * 操作页面跳转，可以跳转到第几个页面
     *
     * @param inputBy 跳转到第几个页面的input框，输入数字表示要跳转到第几个页面
     * @param string  需要输入第几个页面的数字文本
     * @param btnBy   旁边的确定转向页面的按钮
     */
    public void jumpToPage(By inputBy, String string, By btnBy) {
        //找到input输入框，清空，然后输入第几页
        inputText(inputBy, string);
        //点击确定按钮
        driver.findElement(btnBy).click();
    }

    /* ========== 编辑地址簿操作 ========== */

    /**
     * 新增收货地址填写操作（有手机号）
     * 收货人：XXX;所在地区下拉框都为第二行地区；详细地址：北京中南海；邮编为空；手机号存在；固定电话：0000-00000000
     *
     * @param string   只需传入“新增收货地址”这个div进行xpath定位的String类型
     * @param language 是中文还是繁体还是英文
     */
    public void fillInShippingAddress(String string, String language) {
        //收货人input框输入
        WebElement consigneetInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[1]//input"));
        consigneetInput.clear();
        consigneetInput.sendKeys("XXX");
        //所在地区下拉框选择，默认选择第一行
        //省份下拉框
        WebElement provinceSelect = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[2]/div/div[1]/div"));
        provinceSelect.click();
        WebElement firstProvince = driver.findElement(By.xpath("//div[@class='popup-container']/div/ul/li[2]"));
        firstProvince.click();
        // 市区下拉框
        WebElement regionSelect = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[2]/div/div[2]/div"));
        regionSelect.click();
        WebElement firstRegion = driver.findElement(By.xpath("//div[@class='popup-container']/div/ul/li[2]"));
        firstRegion.click();
        // 区域下拉框
        WebElement districtSelect = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[2]/div/div[3]/div"));
        districtSelect.click();
        WebElement firstDistrict = driver.findElement(By.xpath("//div[@class='popup-container']/div/ul/li[2]"));
        firstDistrict.click();
        /* 填写详细地址 */
        WebElement detailedAddressInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[3]//input"));
        detailedAddressInput.clear();
        detailedAddressInput.sendKeys("北京中南海");
        /* 手机号码 */
        WebElement mobileNumInput = driver.findElement(By.xpath(string + "//div[@class='body']/form/div[5]/div[1]//input[@name='mobilenumber']"));
        mobileNumInput.clear();
        String text = "";
        if (language.equals("zh_CN")) {
            text = "13311111111";
        } else if (language.equals("zh_HK") || language.equals("en_GB")) {
            text = "22222222";
        }
        mobileNumInput.sendKeys(text);
        /* 固定电话 */
        // 电话区号输入
        WebElement areaCodeInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[5]/div[2]/div/div/div[1]/input"));
        areaCodeInput.clear();
        areaCodeInput.sendKeys("0000");
        // 电话号码输入
        WebElement telNumInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[5]/div[2]/div/div/div[2]/input"));
        telNumInput.clear();
        telNumInput.sendKeys("0000000");
        /* 按回车保存收货地址 */
        telNumInput.sendKeys(Keys.ENTER);
    }

    /**
     * 新增收货地址填写操作（无手机号）
     * 收货人：优衣库；所在地区下拉框都为第二行地区；详细地址：北京中南海；邮编为空；手机号空；固定电话：0000-0000000
     *
     * @param string 只需传入“新增收货地址”这个 div 进行 xpath 定位的 String 类型
     */
    public void fillInShippingAddress(String string) {
        /* 收货人 input 框填写 */
        WebElement consigneeInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[1]//input"));
        consigneeInput.clear();
        consigneeInput.sendKeys("优衣库");
        /* 所在地区下拉框选择，默认选择第一行 */
        // 省份下拉框
        WebElement provinceSelect = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[2]/div/div[1]/div"));
        provinceSelect.click();
        WebElement firstProvince = driver.findElement(By.xpath("//div[@class='popup-container']/div/ul/li[2]"));
        firstProvince.click();
        // 市区下拉框
        WebElement regionSelect = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[2]/div/div[2]/div"));
        regionSelect.click();
        WebElement firstRegion = driver.findElement(By.xpath("//div[@class='popup-container']/div/ul/li[2]"));
        firstRegion.click();
        // 区域下拉框
        WebElement districtSelect = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[2]/div/div[3]/div"));
        districtSelect.click();
        WebElement firstDistrict = driver.findElement(By.xpath("//div[@class='popup-container']/div/ul/li[2]"));
        firstDistrict.click();
        /* 填写详细地址 */
        WebElement detailedAddressInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[3]//input"));
        detailedAddressInput.clear();
        detailedAddressInput.sendKeys("北京中南海");
        /* 固定电话 */
        // 电话区号输入
        WebElement areaCodeInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[5]/div[2]/div/div/div[1]/input"));
        areaCodeInput.clear();
        areaCodeInput.sendKeys("0000");
        // 电话号码输入
        WebElement telNumInput = driver.findElement(By.xpath(string + "/div[@class='body']/form/div[5]/div[2]/div/div/div[2]/input"));
        telNumInput.clear();
        telNumInput.sendKeys("0000000");
        /* 按回车保存收货地址 */
        telNumInput.sendKeys(Keys.ENTER);
    }

    /* ========== 判断文本是否是出现在元素中的文本 ==========*/

    /**
     * 判断字符串是否是该元素中的文本
     *
     * @param by     可能会包含某一文本的元素
     * @param string 可能会出现的文本
     * @return 字符串在元素中存在就返回true，不存在返回false
     */
    public boolean textExistsInElement(By by, String string) {
        //父串
        String sStr = driver.findElement(by).getText();
        //子串
        String dStr = string;
        /** KMP算法开始 */
        int sLength = sStr.length();
        int dLength = dStr.length();
        int sIndex = 0, dIndex = 0;
        /** 子串的next数组开始 */
        int[] nextArr = new int[dStr.length()];
        nextArr[0] = -1;
        int k = -1, j = 0;
        while (j < dStr.length() - 1) {
            if (k == -1 || (dStr.charAt(j) == dStr.charAt(k))) {
                ++k;
                ++j;
                nextArr[j] = k;
            } else {
                k = nextArr[j];
            }
        }
        int[] next = nextArr;
        /** 子串的next数组结束 */
        while (sIndex < sLength && dIndex < dLength) {
            //当前字符匹配
            if (dIndex == -1 || sStr.charAt(sIndex) == dStr.charAt(dIndex)) {
                //父串和子串同时后移一个字符
                sIndex++;
                dIndex++;
            } else {//不匹配sIndex不变dIndex取next[j]
                dIndex = next[dIndex];
            }
        }
        if (dIndex == dLength) {
            //文本在元素中存在返回true
            return true;
        }
        //文本在元素中不存在返回false
        return false;
        /** KMP算法结束 */
    }

    /* ========== 获取元素中是文本或者属性值 ==========*/

    /**
     * 获取元素的某一个属性值
     *
     * @param by     元素
     * @param string 属性值
     * @return 返回该元素中的一个属性值
     */
    protected String getElementAttribute(By by, String string) {
        WebElement webElement = driver.findElement(by);
        return webElement.getAttribute(string);
    }

    /**
     * 获取存入的json数据
     * return JSONObject
     */
    public JSONObject getIfo() {
        return jsonObject;
    }

    /**
     * 获取元素的文本值
     *
     * @param by 元素
     * @return 返回该元素的文本值
     */
    protected String getElementText(By by) {
        WebElement webElement = driver.findElement(by);
        return webElement.getText();
    }

    /* ========== 返回DOM中有几个这样的元素 ==========*/

    /**
     * 返回该元素在DOM中出现几次
     *
     * @param by 元素
     * @return 返回该元素在DOM中出现几次
     */
    protected int elementNumInDom(By by) {
        if (elementExists(by)) {
            List<WebElement> elements = driver.findElements(by);
            return elements.size();
        } else {
            return 0;
        }
    }

    /**
     * 切换窗口
     *
     * @param originHandle 原窗口的handle
     */
    protected void switchHandle(String originHandle) {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!originHandle.equals(handle)) {
                driver.switchTo().window(handle);
            }
        }
    }

    /**
     * 点击事件
     *
     * @param xpath xpath
     * @throws InterruptedException sleep
     */
    protected void clickElement(Integer timeout, String xpath) throws InterruptedException {
        Thread.sleep(timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))).click();
    }

}
