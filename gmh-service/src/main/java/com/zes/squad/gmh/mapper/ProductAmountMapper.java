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
     * 更改产品数量(区别于进货卖货,不计入流水)
     * 
     * @param po
     * @return
     */
    int updateAmount(ProductAmountPo po);

    /**
     * 添加产品(进货)
     * 
     * @param po
     * @return
     */
    int addAmount(ProductAmountPo po);

    /**
     * 减少产品(卖货)
     * 
     * @param po
     * @return
     */
    int reduceAmount(ProductAmountPo po);

    /**
     * 根据id查询产品数量
     * 
     * @param id
     * @return
     */
    ProductAmountPo selectById(Long id);

    /**
     * 根据产品编码查询
     * 
     * @param code
     * @return
     */
    ProductAmountPo selectByCode(String code);

    /**
     * 根据条件查询产品数量
     * 
     * @param condition
     * @return
     */
    ProductAmountPo selectByCondition(ProductAmountQueryCondition condition);

}
