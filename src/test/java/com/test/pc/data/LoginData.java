package com.test.pc.data;

/**
 * 登录页常量
 *
 * @author：关河九州
 * @date：2019/12/3 19:49
 * @version：1.0
 */
public class LoginData {
    /**
     * 登录页的url
     */
    //声明时用public static final定义的变量就成了常量，常量必须在声明的时候赋值，且不允许修改
    public static final String URL = "http://hzeronb.saas.hand-china.com/oauth/login";

    /**
     * token url
     */
    public static final String TOKEN_URL = "";

    /**
     * board url
     */
    public static final String BOARD_URL = "";

    /**
     * Authorization 请求头
     */
    public static final String AUTHORIZATION_HEADER = "";

    /**
     * Accept 请求头
     */
    public static final String ACCEPT_HEADER = "application/json";

    /**
     * scope 请求参数
     */
    public static final String SCOPE_PARAM = "read write";

    /**
     * grant_type 请求参数
     */
    public static final String GRANT_TYPE_PARAM = "password";

    /**
     * status code 返回码
     */
    public static final int SUCCESS_CODE = 200;

}
