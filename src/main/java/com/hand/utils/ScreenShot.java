package com.hand.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author：关河九州
 * @date：2019/11/14 17:18
 * @version：1.0
 */
public class ScreenShot {
    private static String soreenShotDirPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-output" + File.separator + "errorScreenShot";

    public static void takeScreenShot(WebDriver driver, ITestResult iTestResult) {
        Object currentClass = iTestResult.getInstance(); //获取当前Object类的测试类
        //获取包名
        String packageName = currentClass.getClass().getPackage().getName();
        System.out.print("packageName:" + packageName);
        String currentClassName = currentClass.toString();

        //正则匹配
        Pattern pattern = Pattern.compile(packageName + ".(.*?)@");//此处为正则表达式
        Matcher matcher = pattern.matcher(currentClassName);//匹配正则表达式
        while (matcher.find()) {
            currentClassName = matcher.group(1);
            System.out.print("currentClassName:" + currentClassName);
        }
        File screenShotDir=new File(soreenShotDirPath);
        if (!screenShotDir.exists()&& !screenShotDir.isDirectory()){
            screenShotDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH点mm分ss秒");
        String time=simpleDateFormat.format(new Date());
        try {
            File source_file=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            //另存截图
            FileUtils.copyFile(source_file,new File(screenShotDir+File.separator+currentClassName+time+".png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
