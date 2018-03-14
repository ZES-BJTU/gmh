package com.zes.squad.gmh.mapper;

import com.zes.squad.gmh.entity.po.ProductFlowPo;

public interface ProductFlowMapper {

    /**
     * 插入流水
     * 
     * @param po
     * @return
     */
    int insert(ProductFlowPo po);

    /**
     * 更改流水记录为无效
     * 
     * @param id
     * @return
     */
    int updateStatus(Long id);

}
