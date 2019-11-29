package com.test.pc.base;


import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lei.shi06@hand-china.com
 * @version 1.0.0
 * @date 2019/9/9
 */
@Slf4j
public class LoginByAPI {


    /**
     * 登录方法
     * 调用方式：String access_token = login("CNPROD", driver, "15545682714", "POFMw7d1Ku0fkYatFWodY4DYMcL1lGIrcPizcOh4tcJUJgae/Hr529PEU/tcnOdl276SNWYvnAQ7cUwjmDkDiVJLvvHQyunrvsjKaJEZaAdHQmMXZZY8ziVykMTN4G4j47nUlaC6S0tGgMbOI2dBA/6juF7ujQPkm6mcTsVZcBI=", "1");
     *
     * @param environment
     * @param driver
     * @param mobile
     * @param pwd
     * @param browseNumber
     * @return
     */
    public String login(String environment, String language, WebDriver driver, String mobile, String pwd, String browseNumber) {
        String access_token = "";

        String mobileEnvironment = "";
        log.info("browseNumber为：" + browseNumber);
        //System.out.println("browseNumber为：" + browseNumber);

        if (language.contains("HK") || language.contains("GB")) {
            mobileEnvironment = "+86" + mobile;
            log.info(environment + "环境手机号码格式为：" + mobileEnvironment);
            //System.out.println(environment + "环境手机号码格式为：" + mobileEnvironment);
        } else {
            mobileEnvironment = mobile;
        }

        if (browseNumber.equals("5")) {
            access_token = LoginH5(environment, language, driver, mobileEnvironment, pwd);
        } else {
            access_token = LoginPC(environment, language, driver, mobileEnvironment, pwd);
        }

        return access_token;
    }

    /**
     * 返回接口url方法
     *
     * @param env
     * @param language
     * @return
     */
    public Map<String, String> returnEnvUrl(String env, String language) {
        Map<String, String> map = new HashMap<>();
        String url1 = "";
        String url2 = "";
        String url = "";
        if (env.equals("prod") && language.contains("CN")) {//CNPROD
            //loginNormal接口
            url1 = "https://a.uniqlo.cn/hmall-ur-service/login/normal";
            //usr接口
            url2 = "https://a.uniqlo.cn/auth/user";
            url = "https://www.uniqlo.cn/";
        }
        if (env.equals("uat") && language.contains("CN")) {//CNUAT
            //loginNormal接口
            url1 = "https://auat.uniqlo.cn/aapi/hmall-ur-service/login/normal";
            //usr接口
            url2 = "https://auat.uniqlo.cn/aapi/auth/user";
            url = "https://suat.uniqlo.cn/";
        }
        if (env.equals("prod") && (language.contains("HK") || language.contains("GB"))) {//HKPROD
            //loginNormal接口
            url1 = "https://d.uniqlo.com.hk/hmall-ur-service/login/normal";
            //usr接口
            url2 = "https://d.uniqlo.com.hk/auth/user";
            url = "https://www.uniqlo.com.hk/";
        }
        if(env.equals("uat")&&(language.contains("HK") || language.contains("GB"))){//HKUAT
            //loginNormal接口
            url1 = "http://azeaecag01-t.eastasia.cloudapp.azure.com/hmall-ur-service/login/normal";
            //usr接口
            url2 = "http://azeaecag01-t.eastasia.cloudapp.azure.com/auth/user";
            url = "http://azeaecag02-t.eastasia.cloudapp.azure.com/pc/zh_HK/";
        }

        map.put("url1", url1);
        map.put("url2", url2);
        map.put("url", url);
        return map;
    }

    /**
     * PC登录方法
     *
     * @param environment
     * @param language
     * @param driver
     * @param mobile
     * @param pwd
     * @return
     */
    public String LoginPC(String environment, String language, WebDriver driver, String mobile, String pwd) {
        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();
        Map<String, String> map3 = new HashMap<>();
        map3 = returnEnvUrl(environment, language);
        //获取url与URL
        String url1 = map3.get("url1");
        String url2 = map3.get("url2");
        String url = map3.get("url");

        //进入官网首页
        driver.get(url);
        //最大化窗口
        driver.manage().window().maximize();
        //调用登录接口
        map1 = interfaceLoginNormal(mobile, pwd, url1);
        //获取接口返回信息
        String access_token = map1.get("access_token");
        String refresh_token = map1.get("refresh_token");
        String env = map1.get("env");
        //调用usr接口
        map2 = interfaceUsr(access_token, url2);
        //获取接口返回信息
        String username = map2.get("username");
        String userId = map2.get("userId");
        String email = map2.get("email");

        //编写cookie登录，上面需要先进入登录页面
        Cookie cookie1 = new Cookie("userid", userId);
        Cookie cookie2 = new Cookie("username", username);
        Cookie cookie3 = new Cookie("access_token", access_token);
        Cookie cookie4 = new Cookie("refresh_token", refresh_token);
        Cookie cookie5 = new Cookie("email", email);
        Cookie cookie6 = new Cookie("mobile", mobile);
        Cookie cookie7 = new Cookie("env", env);
        Cookie cookie8 = new Cookie("mobile", mobile);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //执行写入cookie
        driver.manage().addCookie(cookie1);
        driver.manage().addCookie(cookie2);
        driver.manage().addCookie(cookie3);
        driver.manage().addCookie(cookie4);
        driver.manage().addCookie(cookie5);
        driver.manage().addCookie(cookie6);
        driver.manage().addCookie(cookie7);
        driver.manage().addCookie(cookie8);
        log.info("登录成功");
        //刷新页面
        driver.navigate().refresh();

        return access_token;
    }


    /**
     * H5登录方法
     *
     * @param environment
     * @param language
     * @param driver
     * @param mobile
     * @param pwd
     * @return
     */
    public String LoginH5(String environment, String language, WebDriver driver, String mobile, String pwd) {

        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();
        Map<String, String> map3 = new HashMap<>();
        //获取url与URL
        map3 = returnEnvUrl(environment, language);
        //获取url与URL
        String url1 = map3.get("url1");
        String url2 = map3.get("url2");
        String url = map3.get("url");

        //进入官网首页
        driver.get(url);
        //最大化窗口
        driver.manage().window().maximize();
        //调用登录接口
        map1 = interfaceLoginNormal(mobile, pwd, url1);
        //获取接口返回信息
        String access_token = map1.get("access_token");
        String env = map1.get("env");

        //调用usr接口
        map2 = interfaceUsr(access_token, url2);

        //获取接口返回信息
        String username = map2.get("username");
        String userId = map2.get("userId");
        String email = map2.get("email");
        String name= map2.get("name");
        String phoneNumber= map2.get("phoneNumber");
        String encrypMobile=map2.get("encrypMobile");

        //js写入localStorage
        String js1 = "window.localStorage.setItem('Uniqlo_token','" + access_token + "');";
        String js2 = "window.localStorage.setItem('Uniqlo_userEnv','" + env + "');";
        String s="{"+"\"username\":"+"\""+username+"\""+","+"\"userId\":"+"\""+userId+"\""+","+"\"name\":"+"\""+name+"\""+","+"\"phoneNumber\":"+"\""+phoneNumber+"\""+","+"\"encrypMobile\":"+"\""+encrypMobile+"\""+"}";
        log.info("userinfo:"+s);
        String js3 = "window.localStorage.setItem('userInfo','" + s + "');";

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //执行js代码
        ((JavascriptExecutor) driver).executeScript(js1);
        ((JavascriptExecutor) driver).executeScript(js2);
        ((JavascriptExecutor) driver).executeScript(js3);


        log.info("登录成功");
        //刷新页面
        driver.navigate().refresh();

        return access_token;

    }


    /**
     * 获取登录接口返回数据
     *
     * @param mobile
     * @param pwd
     * @param url
     * @return
     */
    public Map<String, String> interfaceLoginNormal(String mobile, String pwd, String url) {
        Map<String, String> map = new HashMap<>();
        String strResult = "";
        String access_token = "";
        String refresh_token = "";
        String env = "";
        //获取可关闭的 httpCilent
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(7000).setConnectionRequestTimeout(7000)
                .setSocketTimeout(7000).setRedirectsEnabled(true).build();
        //创建post请求
        HttpPost httpPost = new HttpPost(url);
        //设置超时时间
        httpPost.setConfig(requestConfig);
        //设置请求头
        httpPost.setHeader("Content-Type", "application/json");
        //创建json对象，优衣库参数格式为json形式
        JSONObject object = new JSONObject();
        //添加参数
        object.put("customerId", mobile);
        object.put("pwd", pwd);
        object.put("t", String.valueOf(System.currentTimeMillis()));
        object.put("mock", true);
        //post请求参数设置编码
        StringEntity stringEntity = new StringEntity(object.toString(), "utf-8");
        log.info("loginNormal接口请求参数:" + object.toString());
        //设置post请求参数
        httpPost.setEntity(stringEntity);
        try {
            //发送post请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {
                System.out.println("StatusCode:" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    //返回的数据
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                    //字符串转换为json
                    JSONObject response = JSONObject.fromObject(strResult);
                    //json数据转换为json数组格式，再将resp的数据转换成json对象
                    JSONObject resp = JSONObject.fromObject(response.getJSONArray("resp").get(0));
                    //获取access_token
                    access_token = resp.get("access_token").toString();
                    //获取refresh_token
                    refresh_token = resp.get("refresh_token").toString();
                    //获取env
                    env = resp.get("env").toString();
                    //打印信息
                    log.info("access_token：" + access_token);
                    log.info("refresh_token：" + refresh_token);
                    log.info("env：" + env);
                    log.info("超时时间设置为：" + "5000ms");
                } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } else {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                }
            } else {
                log.info("接口存在问题");
            }
            //打印接口返回数据
            log.info("loginNormal接口请求返回数据：" + strResult);
            //System.out.println("loginNormal接口请求返回数据：" + strResult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close(); //释放资源
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将参数加入map
        map.put("access_token", access_token);
        map.put("refresh_token", refresh_token);
        map.put("env", env);

        return map;
    }

    /**
     * 获取usr接口返回数据
     *
     * @param access_token
     * @param url
     * @return
     */
    public Map<String, String> interfaceUsr(String access_token, String url) {
        Map<String, String> map = new HashMap<>();
        String strResult = "";
        String userId = "";
        String username = "";
        String email = "";
        String name = "";
        String phoneNumber = "";
        String encrypMobile = "";

        //获取可关闭的 httpCilent
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(1000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(1000).setRedirectsEnabled(true).build();
        //创建get请求
        HttpGet httpGet = new HttpGet(url);
        //配置超时时间
        httpGet.setConfig(requestConfig);
        //设置请求头
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "bearer " + access_token);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);

            strResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果

            log.info("usr接口请求返回数据：" + strResult);

            //System.out.println("usr接口请求返回数据：" + strResult);
            //字符串转换为json
            JSONObject response = JSONObject.fromObject(strResult);
            //获取userId
            userId = response.getString("userId");
            //获取username
            username = response.getString("username");
            //获取phoneNumber
            phoneNumber = response.getString("mobileNumber");
            //获取encrypMobile
            encrypMobile = response.getString("encrypMobile");

            try {
                //获取name
                name = response.getString("name");
                //获取email
                email = response.getString("email");
            } catch (Exception e) {

            }
            log.info("userid：" + userId);
            log.info("username：" + username);
            log.info("email：" + email);
            log.info("name:" + name);
            log.info("phoneNumber:" + phoneNumber);
            log.info("encrypMobile:" + encrypMobile);


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                httpClient.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("userId", userId);
        map.put("username", username);
        map.put("email", email);
        map.put("name", name);
        map.put("phoneNumber", phoneNumber);
        map.put("encrypMobile", encrypMobile);

        return map;
    }
}
