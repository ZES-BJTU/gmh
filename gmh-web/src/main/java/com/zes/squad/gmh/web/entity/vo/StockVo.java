package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockVo {

    private Long       id;
    private Long       stockTypeId;
    private String     stockTypeName;
    private String     code;
    private String     name;
    private String     unitName;
    private Long       stockAmountId;
    private BigDecimal amount;
    private Long       storeId;
    private String     storeName;

}
