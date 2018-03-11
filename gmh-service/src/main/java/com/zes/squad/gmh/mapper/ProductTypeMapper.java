package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductTypePo;

public interface ProductTypeMapper {

    /**
     * 插入单个产品分类
     * 
     * @param po
     * @return
     */
    int insert(ProductTypePo po);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新
     * 
     * @param po
     * @return
     */
    int updateSelective(ProductTypePo po);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    ProductTypePo selectById(Long id);

    /**
     * 根据名称查询
     * 
     * @param name
     * @return
     */
    ProductTypePo selectByName(String name);

    /**
     * 根据查询条件查询
     * 
     * @param condition
     * @return
     */
    List<ProductTypePo> selectByCondition(ProductTypeQueryCondition condition);

}
