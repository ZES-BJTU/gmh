package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.StorePo;

public interface StoreMapper {

    /**
     * 新增单个门店
     * 
     * @param po
     * @return
     */
    int insert(StorePo po);

    /**
     * 删除单个门店
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除门店
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 修改门店信息
     * 
     * @param po
     * @return
     */
    int updateSelective(StorePo po);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    StorePo selectById(Long id);

    /**
     * 根据名称查询
     * 
     * @param name
     * @return
     */
    StorePo selectByName(String name);

    /**
     * 查询所有门店
     * 
     * @return
     */
    List<StorePo> selectAll();

}
