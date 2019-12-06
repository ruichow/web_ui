package com.test.pc.page;


import com.test.pc.common.PageCommon;
import com.test.pc.data.LoginData;
import com.test.pc.locator.LoginLocator;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

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
     * 登录（UI方式登录）
     *
     * @param user 用户名
     * @param pwd 密码
     */
    public void loginHZERO_UI(String user,String pwd) throws Exception{
        log.info("使用UI进行登录操作");
        //进入平台首页
        driver.get(LoginData.URL);
        //等到页面登录输入框可见
        for (int i = 0; i < 1800; i++){
            log.info("寻找登录输入框！"+i);
            Thread.sleep(100);
            if (isElementContained("//input[@id='username']")){
                break;
            }
        }
        //等到用户名的输入框可见之后再去输入
        inputData(LoginLocator.USER_INPUT,user);
        //等到密码输入框可见之后再去输入
        inputData(LoginLocator.PWD_INPUT,pwd);
        //等到登录按钮可见之后再去点击
        clickButton(LoginLocator.LOGIN_BUTTON);
        for (int i = 0; i < 1800; i++){
            log.info("寻找登录按钮" +i);
            Thread.sleep(100);
            if (isElementContained("//button[text()='登录']")){
                break;
            }
        }
        log.info("UI登录成功！");
    }

    /**
     * 登录（API 方式登录）
     *
     * @param user 用户名
     * @param pwd 密码
     */
    public void loginHZERO_API(String user, String pwd) throws Exception {
        log.info("使用 API 进行登陆操作");

        // 进入融智汇的首页
        driver.get(LoginData.URL);

        /* 返回数据 */
        // access_token
        String access_token;
        // refresh_token
        String refresh_token;
        // Status Code
        int statusCode;

        /* post 请求设置超时时间 */
        // 创建 post 请求
        HttpPost httpPost = new HttpPost(LoginData.TOKEN_URL);
        // 配置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                // 连接超时时间
                .setConnectTimeout(5000)
                // 请求超时时间
                .setConnectionRequestTimeout(5000)
                // socket 超时时间
                .setSocketTimeout(5000)
                // 默认允许自动重定向
                .setRedirectsEnabled(true)
                .build();
        // 给 post 请求设置超时时间
        httpPost.setConfig(requestConfig);

        /* post 请求设置请求头 */
        // 给 post 请求设置请求头
        httpPost.setHeader("Authorization", LoginData.AUTHORIZATION_HEADER);
        httpPost.setHeader("Accept", LoginData.ACCEPT_HEADER);

        /* post 请求设置传参 */
        // 定义 form-data 型参数
        List<BasicNameValuePair> pair =new ArrayList<>();
        pair.add(new BasicNameValuePair("username", user));
        pair.add(new BasicNameValuePair("password", pwd));
        pair.add(new BasicNameValuePair("scope", LoginData.SCOPE_PARAM));
        pair.add(new BasicNameValuePair("grant_type", LoginData.GRANT_TYPE_PARAM));
        // 给 post 请求设置参数
        httpPost.setEntity(new UrlEncodedFormEntity(pair));

        /* post 请求执行 */
        // 定义可关闭的 CloseableHttpClient
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        // 发送 post 请求
        HttpResponse httpResponse = closeableHttpClient.execute(httpPost);

        /* 获取返回信息 */
        if(httpResponse == null){
            throw new Exception("返回信息为 NULL，登陆接口有问题！");
        }
        // 获取状态码
        statusCode = httpResponse.getStatusLine().getStatusCode();
        log.info(driver + " 的登陆接口状态码为:" + statusCode);
        if(statusCode != LoginData.SUCCESS_CODE){
            throw new Exception("返回状态码不为 200，登陆接口有问题！");
        }
        // 拿到 String 类型返回数据
        String resultStr = EntityUtils.toString(httpResponse.getEntity());
        // 把返回数据转化成 JSONObject
        JSONObject resultJson = JSONObject.fromObject(resultStr);
        // 获取 access_token
        access_token = resultJson.get("access_token").toString();
        log.info(driver + " 的登陆接口 access_token:" + access_token);
        // 获取 refresh_token
        refresh_token = resultJson.get("refresh_token").toString();
        log.info(driver + " 的登陆接口 refresh_token:" + refresh_token);

        /* 关闭 http 请求释放资源 */
        if(closeableHttpClient != null){
            closeableHttpClient.close();
        }

        /* 把 token 保存进 local storage */
        String userStr = "{\"username\":\"" + user + "\",\"password\":\"" + pwd + "\",\"remember\":true}";
        String authorityStr = "[\"guest\"]";
        ((JavascriptExecutor) driver).executeScript("window.localStorage.setItem('user', '" + userStr + "')");
        ((JavascriptExecutor) driver).executeScript("window.localStorage.setItem('antd-pro-authority', '" + authorityStr + "')");

        /* 把 token 保存进 session storage */
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.setItem('token', '" + access_token + "')");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.setItem('refresh_token', '" + refresh_token + "')");

        // 跳转页面
        driver.get(LoginData.BOARD_URL);
        // 延时的等待某一字段出现
        for(int i = 0; i < 1800; i++){
            Thread.sleep(100);
            if(isElementContained("//div[@class='ant-tabs-tab-unclosable']")){
                break;
            }
        }

        // 延时等待页面上所有转圈消失
        for(int i = 0;i < 600; i++ ){
            Thread.sleep(100);
            if(!isElementContained("//span[@class='ant-spin-dot ant-spin-dot-spin']")){
                break;
            }
        }

        log.info("API 登陆成功！");
    }

}
