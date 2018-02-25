package com.zes.squad.gmh.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.po.StockPo;

public interface StockMapper {

    /**
     * 插入单个库存
     * 
     * @param po
     * @return
     */
    int insert(StockPo po);

    /**
     * 根据id删除库存
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除库存
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新库存
     * 
     * @param po
     * @return
     */
    int updateSelective(StockPo po);

    StockPo getById(Long id);
    
    int updateTotalAmount(Map<String, Number> map);
}
