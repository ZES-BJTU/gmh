package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ProductStockConvertFlowTypeEnum {

    PRODUCT_TO_STOCK(1, "产品转库存"),
    STOCK_TO_PRODUCT(2, "库存转产品");

    private int    key;
    private String desc;

}
