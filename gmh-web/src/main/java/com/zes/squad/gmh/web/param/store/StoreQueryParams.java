package com.zes.squad.gmh.web.param.store;

import com.zes.squad.gmh.web.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreQueryParams extends QueryParams{
    
    private String storeKeyWord;

}
