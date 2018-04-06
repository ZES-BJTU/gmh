package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.ProductFlowPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductFlowUnion extends Union {

    private static final long serialVersionUID = 1L;

    private ProductTypePo     productTypePo;
    private ProductPo         productPo;
    private ProductFlowPo     productFlowPo;
    private StorePo           storePo;

}
