package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据消费记录id将流水设置无效
     * 
     * @param recordId
     * @return
     */
    int updateStatusByRecordId(Long recordId);

    /**
     * 根据消费记录查询有效库存流水
     * 
     * @param recordId
     * @return
     */
    List<StockFlowPo> selectByRecordId(@Param("recordId") Long recordId, @Param("storeId") Long storeId);

}
