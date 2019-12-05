package com.test.pc.page;

import com.test.pc.common.PageCommon;
import com.test.pc.data.LoginData;
import com.test.pc.locator.LoginLocator;
import javafx.animation.Animation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import javax.swing.text.html.parser.Entity;
import java.nio.file.attribute.BasicFileAttributes;
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
     * 登录（UI方式）
     * @param user 用户名
     * @param pwd 密码
     */
    public void loginHZERO_UI(String user,String pwd) throws Exception{
        log.info("通过界面登录");
        //进入HZero平台首页
        driver.get(LoginData.URL);
        //等到页面登录input可见
        for (int i = 0; i < 1800; i++){
            Thread.sleep(100);
            if(isElementContained("username")) {
                break;
            }
        }
            //等到用户名的input可见之后再去输入
            inputData(LoginLocator.USER_INPUT,user);
            //等到密码的input可见之后再去输入
            inputData(LoginLocator.PWD_INPUT,pwd);
            //等到登录按钮可见之后再点击
            clickButton(LoginLocator.LOGIN_BUTTON);
            for(int i = 0; i < 1800; i++){
                Thread.sleep(100);
                if(isElementContained("//h1[text()='HZERO开发环境']")){
                    break;
                }
            }
            log.info("登录成功！");
    }

    /**
     * 登录（api方式）
     *
     * @param user 用户名
     * @param pwd 密码
     */
    public void loginHZERO_API(String user,String pwd) throws Exception{
        log.info("使用API进行登录操作");

        //进入平台首页
        driver.get(LoginData.URL);
        //返回数据access_token
        String access_token;
        //refresh_token
        String refresh_token;
        //状态码
        int statusCode;

        /* post请求设置超时时间 */
        //创建post请求
        HttpPost httpPost = new HttpPost(LoginData.TOKEN_URL);
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                //连接超时时间
                .setConnectTimeout(5000)
                //请求超时时间
                .setConnectionRequestTimeout(5000)
                //socket 超时时间
                .setSocketTimeout(5000)
                //默认允许自动重定向
                .setRedirectsEnabled(true).build();
        //给post请求设置超时时间
        httpPost.setConfig(requestConfig);

        /* post请求设置请求头 */
        httpPost.setHeader("Authorization",LoginData.AUTHORIZATION_HEADER);
        httpPost.setHeader("Accept",LoginData.ACCEPT_HEADER);

        /* post请求设置传参 */
        //定义form-data型参数
        List<BasicNameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("username",user));
        pair.add(new BasicNameValuePair("password",pwd));
        pair.add(new BasicNameValuePair("scope",LoginData.SCOPE_PARAM));
        pair.add(new BasicNameValuePair("grant_type",LoginData.GRANT_TYPE_PARAM));
        //给post请求设置参数
        httpPost.setEntity(new UrlEncodedFormEntity(pair));

        /* post请求执行 */
        //定义可关闭的CloseableHttpClient
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //发送post请求
        HttpResponse httpResponse = closeableHttpClient.execute(httpPost);

        /* 获取返回信息 */
        if (httpResponse == null){
            throw new Exception("返回信息为null，登录接口有问题");
        }
        //获取状态码
        statusCode = httpResponse.getStatusLine().getStatusCode();
        log.info(driver + "的登录接口状态码为：" + statusCode);
        if (statusCode != LoginData.SUCCESS_CODE){
            throw new Exception("返回状态码不是200，登录接口有问题");
        }
        //拿到String类型返回数据
        String resultStr = EntityUtils.toString(httpResponse.getEntity());
        //把返回数据转化为JSONObject
        JSONObject resultJson = JSONObject.fromObject(resultStr);
        //获取access_token
        access_token = resultJson.get("access_token").toString();
        log.info(driver + "的登录接口access_token：" + access_token);
        //获取refresh_token
        refresh_token = resultJson.get("refresh_token").toString();
        log.info(driver + "的登录接口refresh_token：" + refresh_token);

        /* 关闭http请求，释放资源 */
        if (closeableHttpClient != null){
            closeableHttpClient.close();
        }

        /* 把token保存进local storage */
        String userStr = "{\"username\":\"" + user + "\",\"password\":\"" + pwd + "\",\"remember\":true}";
        String authorityStr = "[\"guest\"]";
        ((JavascriptExecutor) driver).executeScript("window.localStorage.setItem('user','" + userStr + "')");
        ((JavascriptExecutor) driver).executeScript("window.localStorage.setItem('antd-pro-authority','" + authorityStr + "')");

        /* 把token保存进session storage */
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.setItem('token', '" + access_token + "')");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.setItem('refresh_token', '" + refresh_token + "')");

        //跳转页面
        driver.get(LoginData.BOARD_URL);
        //延时等待某一字段出现
        for (int i = 0; i < 1800; i++){
            Thread.sleep(100);
            if (isElementContained("h1[text()='HZERO开发环境']")){
                break;
            }
        }

        log.info("API登录成功！");
    }
}
