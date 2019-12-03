package com.test.pc.locator;

import org.openqa.selenium.By;

/**
 * 登录页的元素定位
 *
 * @author：关河九州
 * @date：2019/12/3 20:21
 * @version：1.0
 */
public class LoginLocator {
    /**
     * 用户名输入框
     */
    public static final By USER_INPUT = By.id("username");
    /**
     * 密码输入框
     */
    public static final By PWD_INPUT = By.id("password");
    /**
     * 登录按钮
     */
    public static final By LOGIN_BUTTON = By.className("btn btn-primary btn-raised login-account-login-btn");
    /**
     * 登录之后页面的工作台字段的定位
     */
    public static final By WORKBENCH_DIV = By.className("anticon anticon-home");


}