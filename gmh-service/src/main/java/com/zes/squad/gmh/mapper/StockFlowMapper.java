package com.zes.squad.gmh.mapper;

import com.zes.squad.gmh.entity.po.StockFlowPo;

public interface StockFlowMapper {

    /**
     * 插入流水
     * 
     * @param po
     * @return
     */
    int insert(StockFlowPo po);

    /**
     * 更改流水记录为无效
     * 
     * @param id
     * @return
     */
    int updateStatus(Long id);

}
