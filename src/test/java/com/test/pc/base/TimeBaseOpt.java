package com.test.pc.base;


import com.hand.constant.BaseConstant;
import com.hand.utils.ThreadLocalUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * @author：关河九州
 * @date：2019/11/20 19:19
 * @version：1.0
 */
public class TimeBaseOpt extends BaseTest {
    private static ThreadLocalUtil<WebDriverWait> waitThreadLocalUtil = new ThreadLocalUtil<WebDriverWait>();
    private static ThreadLocal<WebDriverWait> threadWait = new ThreadLocal<WebDriverWait>();

    public void setWait(WebDriver webDriver) {
        waitThreadLocalUtil.setThreadValue(threadWait, new WebDriverWait(webDriver, BaseConstant.EIGHT_THOUSANG));
    }

    public WebDriverWait getWait() {
        return threadWait.get();
    }

    //移除threadWait中的值
    public void releaseWait() {
        threadWait.remove();
    }

    //超时设置
    public void setTimeouts(WebDriver webDriver) {
        if (webDriver != null) {
            setWait(webDriver);
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(BaseConstant.TWENTY, TimeUnit.SECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(BaseConstant.FORTY, TimeUnit.SECONDS);
            webDriver.manage().timeouts().setScriptTimeout(BaseConstant.FORTY, TimeUnit.SECONDS);
        }
    }
}
