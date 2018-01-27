package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockTypeUnion;
import com.zes.squad.gmh.entity.union.StockUnion;

public interface StockService {

    /**
     * 新建库存分类
     * 
     * @param po
     */
    void createStockType(StockTypePo po);

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
    void modifyStockType(StockTypePo po);

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
    PagedList<StockTypeUnion> listPagedStockTypes(StockTypeQueryCondition condition);

    /**
     * 新建库存
     * 
     * @param po
     */
    void createStock(StockPo po);

    /**
     * 根据id删除库存
     * 
     * @param id
     */
    void deleteStock(Long id);

    /**
     * 批量删除库存
     * 
     * @param ids
     */
    void deleteStocks(List<Long> ids);

    /**
     * 修改库存
     * 
     * @param po
     */
    void modifyStock(StockPo po);

    /**
     * 根绝id查询库存
     * 
     * @param id
     * @return
     */
    StockUnion queryStock(Long id);

    /**
     * 根据条件模糊搜索库存
     * 
     * @param condition
     * @return
     */
    PagedList<StockUnion> listPagedStocks(StockQueryCondition condition);

}
