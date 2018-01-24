package com.zes.squad.gmh.web.param.stock;

import com.zes.squad.gmh.web.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockQueryParams extends QueryParams {

    private Long   stockTypeId;
    private String stockKeyWord;

}
