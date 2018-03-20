package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class StockVo {

    private Long       id;
    private Long       stockTypeId;
    private String     stockTypeName;
    private String     code;
    private String     name;
    private String     unitName;

}
