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

    /**
     * 根据编码查询库存
     * 
     * @param id
     * @return
     */
    StockPo selectById(Long id);

    /**
     * 根据库存类型id查询
     * 
     * @param id
     * @return
     */
    List<StockPo> selectByTypeId(Long id);

    /**
     * 根据编码查询库存
     * 
     * @param code
     * @return
     */
    StockPo selectByCode(String code);

    /**
     * 查询所有库存
     * 
     * @return
     */
    List<StockPo> selectAll();

    /**
     * 查询门店所有库存
     * 
     * @param storeId
     * @return
     */
    List<StockPo> selectStoreAll(Long storeId);

    StockPo getById(Long id);

    int updateTotalAmount(Map<String, Number> map);
}
