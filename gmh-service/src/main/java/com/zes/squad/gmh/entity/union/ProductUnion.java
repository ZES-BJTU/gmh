package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductUnion extends Union {

    private static final long serialVersionUID = 1L;

    private ProductTypePo     productTypePo;
    private ProductPo         productPo;
    private ProductAmountPo   productAmountPo;
    private StorePo           storePo;

}
