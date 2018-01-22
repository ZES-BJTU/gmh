package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.StockTypePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockTypeUnion extends Union {

    private static final long serialVersionUID = 1L;
    private StockTypePo       stockTypePo;
    private String            storeName;

}
