package com.zes.squad.gmh.mapper;

import com.zes.squad.gmh.entity.po.ProductConvertStockFlowPo;

public interface ProductConvertStockFlowMapper {

    /**
     * 插入
     * 
     * @param po
     * @return
     */
    int insert(ProductConvertStockFlowPo po);

    /**
     * 更新状态
     * 
     * @param id
     * @return
     */
    int updateStatus(Long id);

}
