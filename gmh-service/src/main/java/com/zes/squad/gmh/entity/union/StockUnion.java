package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.StockAmountPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockUnion extends Union {

    private static final long serialVersionUID = 1L;

    private StockTypePo       stockTypePo;
    private StockPo           stockPo;
    private StockAmountPo     stockAmountPo;
    private StorePo           storePo;

}
