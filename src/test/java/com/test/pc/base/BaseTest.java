package com.test.pc.base;

import com.hand.base.DriverBase;
import com.hand.base.JedisBase;
import com.hand.utils.PropertyReader;
import com.test.pc.page.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import redis.clients.jedis.Jedis;

import java.util.Properties;

/**
 * BaseTest包含驱动driverbase的调用，redis的益工具类的调用，配置文件的读取,三个被调用的工具分别封装成工具
 * 涵盖了驱动初始化，redis配置初始化，读取配置文件进行参数构建项目
 *
 * @author：关河九州
 * @date：2019/11/20 19:07
 * @version：1.0
 */

@Slf4j
public class BaseTest {
    public static Properties properties; //读取Java的配置文件，Java中配置文件常为.properties文件
    public static DriverBase driverBase = new DriverBase();
    public WebDriver driver;
    public WebDriverWait wait;
    public static ElementBaseOpt elementPage = new ElementBaseOpt();
    public static TimeBaseOpt timePage = new TimeBaseOpt();
    //需要用到Redis则定义
    public static JedisBase jedisBase = new JedisBase();
    public Jedis jedis;



    //参数化传参
    @BeforeTest(alwaysRun = true)
    @Parameters({"propertiesUrl", "browserNumber", "remoteIP", "browserVersion"})
    public void setUp(@Optional("src/test/resources/config/config.properties") String propertiesUrl,
                      @Optional("1") int browserNumber, @Optional() String remoteIP, @Optional("192.168.11.221") String browserVersion) {
        try {
            properties = PropertyReader.getProperties(propertiesUrl);//读取配置文件
            driverBase.randomOpenBrowse(browserNumber, remoteIP, browserVersion);
            //需要用到Redis时调用
            jedisBase.getJedisPool();
            jedisBase.setJedisThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClassInit() throws Exception{
        driver = driverBase.getDriver();
        timePage.setTimeouts(driver);
        wait = timePage.getWait();
        //需要用到Redis时调用
        jedis = jedisBase.getJedis();
    }

    /**
     * 运行时要先登录
     *
     * @param user 用户名
     * @param pwd  密码
     */
    @BeforeClass(alwaysRun = true)
    @Parameters({"user", "pwd"})
    public void loginClass(@Optional("23902") String user, @Optional("Admin@1234") String pwd) throws Exception {
        //初始化页面类
        LoginPage loginPage = new LoginPage(driver);
        /* 登录平台 */
        loginPage.loginHZERO_UI(user,pwd);
        //loginPage.loginHZERO_API(user, pwd);
    }

    @AfterTest(alwaysRun = true)
    public void stop() {
        driverBase.stopDriver();
        timePage.releaseWait();
        //需要用到Redis时调用
        jedisBase.returnJedis();
    }


}
