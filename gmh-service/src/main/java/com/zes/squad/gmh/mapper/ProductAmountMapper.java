package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ProductAmountQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;

public interface ProductAmountMapper {

    /**
     * 插入单个产品数量记录
     * 
     * @param po
     * @return
     */
    int insert(ProductAmountPo po);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除产品数量
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);
    
    /**
     * 
     * @param po
     * @return
     */
    int updateAmount(ProductAmountPo po);

    /**
     * 根据条件查询产品数量
     * 
     * @param condition
     * @return
     */
    ProductAmountPo selectByCondition(ProductAmountQueryCondition condition);

}
