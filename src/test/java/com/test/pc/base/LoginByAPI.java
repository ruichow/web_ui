package com.test.pc.base;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * @author：关河九州
 * @date：2019/11/27 20:57
 * @version：1.0
 */
@Slf4j
public class LoginByAPI {

    /**
     * 登录方法
     * 调用方式：String access_token=login("CNPROD",driver,"15545682714", "POFMw7d1Ku0fkYatFWodY4DYMcL1lGIrcPizcOh4tcJUJgae/Hr529PEU/tcnOdl276SNWYvnAQ7cUwjmDkDiVJLvvHQyunrvsjKaJEZaAdHQmMXZZY8ziVykMTN4G4j47nUlaC6S0tGgMbOI2dBA/6juF7ujQPkm6mcTsVZcBI=", "1")
     *
     * @param environment  环境
     * @param driver       驱动
     * @param mobile
     * @param pwd
     * @param browseNumber
     * @return
     */
    public String login(String environment, String language, WebDriver driver, String mobile, String pwd, String browseNumber) {
        String access_token = "";
        String mobileEnvironment = "";
        log.info("browseNumber为：" + browseNumber);
        if (language.contains("HK") || language.contains("GB")){
            mobileEnvironment="86"+mobile;
            log.info(environment+"环境手机号码格式为："+mobileEnvironment);
        }else{
            mobileEnvironment=mobile;
        }
        if (browseNumber.equals("5")){
            access_token=LoginH5(environment,language,driver,mobileEnvironment,pwd);
        }else{
            access_token=LoginPC(environment,language,driver,mobileEnvironment,pwd);
        }
        return access_token;
    }

    /**
     * 返回接口url方法
     * @param env
     * @param language
     * @return
     */
    public Map<String,String> returnEnvUrl(String env,String language){}
}
