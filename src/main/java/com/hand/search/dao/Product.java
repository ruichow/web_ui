package com.hand.search.dao;


import com.hand.constant.Distribution;
import lombok.Data;

/**
 * @author：关河九州
 * @date：2019/11/14 17:17
 * @version：1.0
 */


@Data
public class Product {
    private String productCode;
    private String productId;
    private String sizeText;
    private String colorNo;
    private int stock;
    private String styleText;
    private Distribution distribution;
    // 店铺名
    private String storeDisplayName;
    private String storeCode;
    // 商品名
    private String fullName;
}
