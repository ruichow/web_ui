package com.test.pc.base;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * @author：关河九州
 * @date：2019/11/20 19:18
 * @version：1.0
 */

@Slf4j
public class ElementBaseOpt extends BaseTest {
    //访问页面-通过页面标题是否包含给定值验证
    public Boolean openPage(String pageUrl, String pageName, String pageTitle) {
        driver.get(pageUrl);
        Boolean flag = ifTitleContains(pageTitle);
        openPageLog(flag, pageName);
        return ifTitleContains(pageTitle);
    }

    //访问页面-通过页面元素text是否与给定值一致验证
    public Boolean openPage(String pageUrl, String pageName, By locator, String text) {
        driver.get(pageUrl);
        Boolean flag = ifTextExists(locator, text);//locator-定位器
        openPageLog(flag, pageName);
        return ifTextExists(locator, text);
    }

    //访问页面的日志信息
    public void openPageLog(Boolean flag, String pageName) {
        if (flag) {
            log.info(String.format("成功访问%s页面！", pageName));
        } else {
            log.info(String.format("访问%s页面失败！", pageName));
        }
    }

    //输入框内输入内容
    public WebElement sendKeysToInputbox(By locator, String content) {
        WebElement inputElement = locatedElement(locator);
        inputElement.clear();//清除输入框
        inputElement.sendKeys(content);//输入框输入内容（content）
        return inputElement;
    }

    //点击按钮
    public void clickBtn(By locator) {
        locatedElement(locator).click();
    }

    /**
     * presence_of_element_located： 当我们不关心元素是否可见，只关心元素是否存在在页面中。
     * visibility_of_element_located： 当我们需要找到元素，并且该元素也可见
     *
     * @param locator
     * @return
     */
    //根据指定定位器（locator）返回WebElement对象
    public WebElement locatedElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    //判断当前页面的某一个元素的text是否为预期值
    public Boolean ifTextExists(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    //判断当前页面标题是否包含
    public Boolean ifTitleContains(String titleContent) {
        return wait.until(ExpectedConditions.titleContains(titleContent));
    }

    //标题当前页面标题是否等于精确值
    public Boolean ifTitleIs(String title) {
        return wait.until(ExpectedConditions.titleIs(title));
    }
}
