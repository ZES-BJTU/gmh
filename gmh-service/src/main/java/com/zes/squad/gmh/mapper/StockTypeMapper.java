package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockTypePo;

public interface StockTypeMapper {

    /**
     * 新增库存分类
     * 
     * @param po
     * @return
     */
    int insert(StockTypePo po);

    /**
     * 根据id删除单个库存分类
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除库存分类
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新库存分类
     * 
     * @param po
     * @return
     */
    int updateSelective(StockTypePo po);

    /**
     * 根据id查询库存分类
     * 
     * @param id
     * @return
     */
    StockTypePo selectById(Long id);

    /**
     * 根据名称查询
     * 
     * @param name
     * @return
     */
    StockTypePo selectByName(String name);

    /**
     * 根据搜索条件查询
     * 
     * @param condition
     * @return
     */
    List<StockTypePo> selectByCondition(StockTypeQueryCondition condition);

}
