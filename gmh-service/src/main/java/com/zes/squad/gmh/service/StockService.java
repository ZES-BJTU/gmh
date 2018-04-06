package com.zes.squad.gmh.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockAmountPo;
import com.zes.squad.gmh.entity.po.StockFlowPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockUnion;

public interface StockService {

    /**
     * 新建库存分类
     * 
     * @param po
     */
    StockTypePo createStockType(StockTypePo po);

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
    StockTypePo modifyStockType(StockTypePo po);

    /**
     * 查询单个库存分类
     * 
     * @param id
     * @return
     */
    StockTypePo queryStockTypeDetail(Long id);

    /**
     * 条件查询库存分类(分页)
     * 
     * @param condition
     * @return
     */
    PagedList<StockTypePo> listPagedStockTypes(StockTypeQueryCondition condition);

    /**
     * 查询所有分类
     * 
     * @param condition
     * @return
     */
    List<StockTypePo> listAllStockTypes();

    /**
     * 新建库存
     * 
     * @param po
     */
    StockPo createStock(StockPo po);

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
    StockPo modifyStock(StockPo po);

    /**
     * 根绝id查询库存
     * 
     * @param id
     * @return
     */
    StockUnion queryStockDetail(Long id);

    /**
     * 根据条件模糊搜索库存
     * 
     * @param condition
     * @return
     */
    PagedList<StockUnion> listPagedStocks(StockQueryCondition condition);

    /**
     * 查询所有库存
     * 
     * @return
     */
    List<StockPo> listAllStocks();

    /**
     * 新建库存容量
     * 
     * @param po
     * @return
     */
    StockAmountPo createStockAmount(StockAmountPo po);

    /**
     * 删除单个库存容量
     * 
     * @param id
     */
    void removeStockAmount(Long id);

    /**
     * 删除多个库存容量
     * 
     * @param ids
     */
    void removeStockAmounts(List<Long> ids);

    /**
     * 修改库存
     * 
     * @param po
     * @return
     */
    StockAmountPo modifyStockAmount(StockAmountPo po);

    /**
     * 添加库存
     * 
     * @param po
     */
    StockAmountPo addStockAmount(StockAmountPo po);

    /**
     * 减少库存
     * 
     * @param flowPo
     */
    void reduceStockAmount(StockFlowPo flowPo);

    /**
     * 根据消费记录id将库存流水置为无效
     * 
     * @param recordId
     */
    void modifyFlowInvalid(Long recordId);

    /**
     * 根据id查询库存详情
     * 
     * @param id
     * @return
     */
    StockUnion queryStockDetailWithAmount(Long id);

    /**
     * 根据查询条件分页查询库存详情
     * 
     * @param condition
     * @return
     */
    PagedList<StockUnion> listPagedStocksWithAmount(StockQueryCondition condition);

    /**
     * 导出库存
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    Workbook exportStocks(Date beginTime, Date endTime);

}
