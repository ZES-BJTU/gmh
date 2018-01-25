package com.zes.squad.gmh.web.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockVo {

    private Long       id;
    private Long       stockTypeId;
    private String     stockTypeName;
    private String     name;
    private String     unitName;
    private BigDecimal totalAmount;
    private String     storeName;

}
