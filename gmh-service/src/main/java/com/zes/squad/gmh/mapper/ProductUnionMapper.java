package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.union.ProductUnion;

public interface ProductUnionMapper {

    /**
     * 查询产品详情
     * 
     * @param id
     * @return
     */
    ProductUnion selectById(Long id);

    /**
     * 根据条件查询产品详情
     * 
     * @param condition
     * @return
     */
    List<ProductUnion> selectByCondition(ProductQueryCondition condition);

}
