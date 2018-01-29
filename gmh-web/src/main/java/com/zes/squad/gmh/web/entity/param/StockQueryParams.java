package com.zes.squad.gmh.web.entity.param;

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
