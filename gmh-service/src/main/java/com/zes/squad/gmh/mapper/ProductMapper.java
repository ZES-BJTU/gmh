package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.ProductPo;

public interface ProductMapper {

    /**
     * 增加产品
     * 
     * @param po
     * @return
     */
    int insert(ProductPo po);

    /**
     * 根据id删除产品
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据id批量删除产品
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新产品
     * 
     * @param po
     * @return
     */
    int updateSelective(ProductPo po);

    /**
     * 根据id查询产品
     * 
     * @param id
     * @return
     */
    ProductPo selectById(Long id);

    /**
     * 根据id查询产品
     * 
     * @param id
     * @return
     */
    List<ProductPo> selectByTypeId(Long productTypeId);

    /**
     * 根据编码查询产品
     * 
     * @param name
     * @return
     */
    ProductPo selectByCode(String code);

    /**
     * 根据名称查询产品
     * 
     * @param name
     * @return
     */
    ProductPo selectByName(String name);

    /**
     * 查询所有产品
     * 
     * @return
     */
    List<ProductPo> selectAll();

}
