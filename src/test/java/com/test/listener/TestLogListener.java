package com.test.listener;

import com.hand.utils.ScreenShot;
import com.test.pc.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * 数据加载log日志
 *
 * @author：关河九州
 * @date：2019/12/5
 * @version：1.0
 */
@Slf4j
public class TestLogListener extends TestListenerAdapter {
    @Override
    public void onStart(ITestContext iTestContext) {
        super.onStart( iTestContext );
        log.info( String.format( "====================%s测试开始====================", iTestContext.getName() ) );
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        super.onTestStart( iTestResult );
        log.info( String.format( "========%s.%s测试开始========", iTestResult.getInstanceName(), iTestResult.getName()) );
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        super.onTestSuccess( iTestResult );
        log.info( String.format( "========%s.%s测试通过========", iTestResult.getInstanceName(), iTestResult.getName()) );
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        WebDriver driver = BaseTest.driverBase.getDriver();
        System.out.print( "report_driver_fail:" + driver );
        super.onTestFailure( iTestResult );
        log.error( String.format( "========%s.%s测试失败,失败原因如下：\n%s========", iTestResult.getInstanceName(), iTestResult.getName(), iTestResult.getThrowable() ));
        // 失败截图
        ScreenShot.takeScreenShot( driver,iTestResult);
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        super.onTestSkipped( iTestResult );
        log.info( String.format( "========%s.%s跳过测试========", iTestResult.getInstanceName(), iTestResult.getName()) );
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        super.onFinish( iTestContext );
        log.info( String.format( "====================%s测试结束====================", iTestContext.getName() ) );
    }
}

