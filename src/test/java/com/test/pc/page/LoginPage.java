package com.test.pc.page;

import com.test.pc.common.PageCommon;
import com.test.pc.data.LoginData;
import com.test.pc.locator.LoginLocator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * 平台登录页面
 *
 * @author：关河九州
 * @date：2019/11/21 19:36
 * @version：1.0
 */

@Slf4j
public class LoginPage extends PageCommon{
    /**
     * 构造器
     */
    public LoginPage(WebDriver driver){
        //调用父类构造器
        super(driver);
    }

    /**
     * 登录（UI方式）
     * @param user 用户名
     * @param pwd 密码
     */
    public void loginHZERO_UI(String user,String pwd) throws Exception{
        log.info("通过界面登录");
        //进入HZero平台首页
        driver.get(LoginData.URL);
        //等到页面登录input可见
        for (int i = 0; i < 1800; i++){
            Thread.sleep(100);
            if(isElementContained("username")) {
                break;
            }
        }
            //等到用户名的input可见之后再去输入
            inputData(LoginLocator.USER_INPUT,user);
            //等到按钮可见之后再去输入
            inputData(LoginLocator.PWD_INPUT,pwd);
            //等到登录按钮可见之后再点击
            clickButton(LoginLocator.LOGIN_BUTTON);
            for(int i = 0; i < 1800; i++){
                Thread.sleep(100);
                if(isElementContained("anticon anticon-home")){
                    break;
                }
            }
            log.info("登录成功！");
    }
}
