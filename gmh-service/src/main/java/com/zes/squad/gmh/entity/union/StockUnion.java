package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockUnion extends Union {

    private static final long serialVersionUID = 1L;

    private StockPo           stockPo;
    private StockTypePo       stockTypePo;

}
