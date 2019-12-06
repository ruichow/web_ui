package com.test.pc.testcase;

import com.test.pc.base.BaseTest;
import org.testng.annotations.Test;

/**
 * 登录页面测试
 *
 * @author：关河九州
 * @date：2019/12/5 20:45
 * @version：1.0
 */
public class LoginTest extends BaseTest {
    @Test(groups = "other",description = "登录模块中登录功能校验")
    public void LoginFunction(){
        //已经通过@BeforeClass 注解实现了登录操作
        //请见 D:\web_ui\src\test\java\com\test\pc\base\
    }
}
