package com.test.pc.common;

import com.hand.constant.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    private boolean isElementContained(String s) {
    }
}
