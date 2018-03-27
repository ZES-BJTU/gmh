package com.zes.squad.gmh.mapper;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.po.ProductConvertStockPo;

public interface ProductConvertStockMapper {

    /**
     * 插入
     * 
     * @param po
     * @return
     */
    int insert(ProductConvertStockPo po);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 更新
     * 
     * @param po
     * @return
     */
    int updateSelective(ProductConvertStockPo po);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    ProductConvertStockPo selectById(Long id);

    /**
     * 根据产品id和库存id查重
     * 
     * @param productId
     * @param stockId
     * @return
     */
    ProductConvertStockPo selectByProductAndStock(@Param("productId") Long productId, @Param("stockId") Long stockId);

}
