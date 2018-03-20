package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class StockCreateOrModifyParams {

    private Long       id;
    private Long       stockTypeId;
    private String     code;
    private String     name;
    private String     unitName;

}
