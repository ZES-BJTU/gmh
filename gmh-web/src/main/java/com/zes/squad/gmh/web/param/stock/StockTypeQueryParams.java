package com.zes.squad.gmh.web.param.stock;

import com.zes.squad.gmh.web.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockTypeQueryParams extends QueryParams {
    
    private String stockTypeKeyWord;

}
