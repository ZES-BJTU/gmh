package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.ProductConvertStockFlowPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductConvertStockFlowUnion extends Union {

    private static final long         serialVersionUID = 1L;

    private ProductConvertStockFlowPo productConvertStockFlowPo;
    private ProductTypePo             productTypePo;
    private ProductPo                 productPo;
    private StockTypePo               stockTypePo;
    private StockPo                   stockPo;
    private StorePo                   storePo;

}
