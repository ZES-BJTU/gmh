package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.po.StockAmountPo;

public interface StockAmountMapper {

    /**
     * 插入
     * 
     * @param po
     * @return
     */
    int insert(StockAmountPo po);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 修改库存余量
     * 
     * @param po
     * @return
     */
    int updateAmount(StockAmountPo po);

    /**
     * 添加库存
     * 
     * @param po
     * @return
     */
    int addAmount(StockAmountPo po);

    /**
     * 根据库存和门店修改数量
     * 
     * @param po
     * @return
     */
    int addAmountByStockAndStore(StockAmountPo po);
    
    /**
     * 根据库存和门店批量修改数量
     * 
     * @param pos
     * @return
     */
    int batchAddAmountByStockAndStore(List<StockAmountPo> pos);

    /**
     * 减少库存
     * 
     * @param po
     * @return
     */
    int reduceAmount(StockAmountPo po);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    StockAmountPo selectById(Long id);

    /**
     * 根据库存id查询
     * 
     * @param stockId
     * @return
     */
    List<StockAmountPo> selectByStockId(Long stockId);

    /**
     * 根据库存id和门店id查询
     * 
     * @param stockId
     * @param storeId
     * @return
     */
    StockAmountPo selectByStockAndStore(@Param("stockId") Long stockId, @Param("storeId") Long storeId);

}
