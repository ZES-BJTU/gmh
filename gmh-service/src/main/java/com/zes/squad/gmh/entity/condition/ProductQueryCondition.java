package com.zes.squad.gmh.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductQueryCondition extends QueryCondition {

    private Long   productTypeId;
    private String search;
    private Long   storeId;

}
