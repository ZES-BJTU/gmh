package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class QueryParams {

    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Integer limit;

}
