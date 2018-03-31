package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
     * 根据产品和门店修改数量
     * 
     * @param po
     * @return
     */
    int addAmountByProductAndStore(ProductAmountPo po);

    /**
     * 根据产品和门店批量修改数量
     * 
     * @param pos
     * @return
     */
    int batchAddAmountByProductAndStore(List<ProductAmountPo> pos);

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
     * @param productId
     * @return
     */
    List<ProductAmountPo> selectByProductId(Long productId);

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

    /**
     * 根据产品id和门店id查询
     * 
     * @param productId
     * @param storeId
     * @return
     */
    ProductAmountPo selectByProductAndStore(@Param("productId") Long productId, @Param("storeId") Long storeId);

}
