package com.hand.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Slf4j
public enum Environment {
    CNPROD(1, "prod","zh_CN", "https://a.uniqlo.cn", "https://www.uniqlo.cn","https://www.uniqlo.cn", "https://www.uniqlo.cn/product-detail.html?productCode="),
    HKPRODZH(2,"prod", "zh_HK", "https://d.uniqlo.com.hk", "https://www.uniqlo.com.hk", "https://www.uniqlo.com.hk/pc/zh_HK/index.html", "https://www.uniqlo.com.hk/pc/zh_HK/product-detail.html?productCode="),
    HKPRODEN(3, "prod","en_GB", "https://d.uniqlo.com.hk", "https://www.uniqlo.com.hk", "https://www.uniqlo.com.hk/pc/en_GB/index.html", "https://www.uniqlo.com.hk/pc/en_GB/product-detail.html?productCode="),
    CNUAT(4,"uat","zh_CN","https://iuat.uniqlo.cn/iapi","https://uatpic.uniqlo.cn/uniqlo","https://suat.uniqlo.cn", "https://h5uat.uniqlo.cn/h5/#/product?pid="),
    HKUATZH(5,"uat", "zh_HK", "http://azeaecag01-t.eastasia.cloudapp.azure.com", "http://azeaecag02-t.eastasia.cloudapp.azure.com", "http://azeaecag02-t.eastasia.cloudapp.azure.com/pc/zh_HK/", "http://azeaecag02-t.eastasia.cloudapp.azure.com/pc/zh_HK/product-detail.html?productCode="),
    HKUATEN(6, "uat","en_GB", "http://azeaecag01-t.eastasia.cloudapp.azure.com", "http://azeaecag02-t.eastasia.cloudapp.azure.com", "http://azeaecag02-t.eastasia.cloudapp.azure.com/pc/en_GB/", "http://azeaecag02-t.eastasia.cloudapp.azure.com/pc/en_GB/product-detail.html?productCode=");

    private int id;
    private String env;
    private String language;
    private String apiUrl;
    private String dataUrl;
    private String comUrl;
    private String detailUrl;

    public static Environment getEnvironment(String env, String language) {
        for (Environment environment : Environment.values()) {
            if (environment.getEnv().equalsIgnoreCase(env) && environment.getLanguage().equalsIgnoreCase(language)) {
                return environment;
            }
        }
//        log.warn("{}，环境不存在，请确认为：{},{},{}其中一个", env, Environment.CNPROD, Environment.HKPRODEN, Environment.HKPRODEN);
        return CNPROD;
    }

    @Override
    public String toString() {
        return this.getLanguage();
    }
}
