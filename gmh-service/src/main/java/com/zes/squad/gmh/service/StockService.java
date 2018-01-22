package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockTypeUnion;

public interface StockService {

    /**
     * 新建库存分类
     * 
     * @param po
     */
    void CreateStockType(StockTypePo po);

    /**
     * 删除单个库存分类
     * 
     * @param id
     */
    void deleteStockType(Long id);

    /**
     * 删除多个库存分类
     * 
     * @param ids
     */
    void deleteStockTypes(List<Long> ids);

    /**
     * 更新库存分类
     * 
     * @param po
     */
    void updateStockType(StockTypePo po);

    /**
     * 查询单个库存分类
     * 
     * @param id
     * @return
     */
    StockTypeUnion queryStockTypeDetail(Long id);

    /**
     * 条件查询库存分类(分页)
     * 
     * @param condition
     * @return
     */
    PagedList<StockTypeUnion> queryStockTypesByCondition(StockTypeQueryCondition condition);

}
