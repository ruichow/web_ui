package com.hand.constant;

public final class UrlConstants {
    // 登录url
    public static final String loginUrl = "/hmall-ur-service/login/normal";
    // 滑块确认
    public static final String check = "/hmall-verification-service/v/check";
    // 发验证码:/hmall-sms-service/mobile/send/zh_HK?t=1565573471336
    public static final String sendMsg = "/hmall-sms-service/mobile/send";
    // 收验证码:/hmall-sms-service/mobile/getMsg/18262605229/QUICK_LOGIN/zh_CN?t=1565251310719
    public static final String getMsg = "/hmall-sms-service/mobile/getMsg";
    // 快速登录
    public static final String quickLogin = "/hmall-ur-service/login/quick";
    // user
    public static final String user = "/auth/user";
    // 搜索商品
    public static final String searchProductUrl = "/hmall-sc-service/search/searchWithDescriptionAndConditions";
    // 查询库存
    public static final String stockUrl = "/stock/stock/query";
    // 查询该productCode下面所有的productId列表及详情（H5） /product/i/product/spu/h5/query/u0000000000010/zh_HK
    public static final String productSpuUrl = "/product/i/product/spu/%s/query";
    // 查询该productCode下面所有的productId列表及详情（PC）/data/products/spu/zh_HK/u0000000000108.json
    public static final String dataProducts = "/data/products/spu";
    // 创建临时订单
    public static final String createTempOrdersUrl = "/hmall-od-service/order/createTempOrders";
    // 通过门店显示名称模糊搜索门店列表
    public static final String searchStoreNameList = "/hmall-sc-service/i/storeSearch/mobile/searchStoreNameList";
    // 获取门店所有商品
    public static final String searchWithStoreAndConditions = "/hmall-sc-service/search/searchWithStoreAndConditions";
    // 增加收获地址
    public static final String insertAddressUrl = "/hmall-ur-service/customer/address/insert";
    // 获取收货地址
    public static final String getAddressListUrl = "/hmall-ur-service/customer/address/list";
    // 获取购物车列表
    public static final String queryCartUrl = "/cart/cart/query/mobile";
    // 删除购物车指定商品
    public static final String deleteFromCartUrl = "/cart/cart/delete";
    // 查询订单列表
    public static final String queryOrderUrl = "/hmall-od-service/order/queryForUserOrders/1/20";
    // 删除指定订单
    public static final String deleteOrderUrl = "/hmall-od-service/order/updateOrderRecovered";
    // 取消订单
    public static final String cancelOrderUrl = "/hmall-od-service/order/updateOrderCanceled";

    // 表示密码为：111111
    public static final String pwd = "hn494K5iDvfl7b/PVstOTg4aTNNwOTb5gHNuMlYupOs2lzQjFovTk32sDxfJa38YHIbYVFhbcannQQFp35lOGsIjnM4ZyjbJNj+J3b09rx+jzJnOQ72KvQdePlOYj968FfeYLxkjjALYZ/DAs4yj64r8WpmjECjbyKWf3YE6fRU=";

    private UrlConstants() {

    }
}
