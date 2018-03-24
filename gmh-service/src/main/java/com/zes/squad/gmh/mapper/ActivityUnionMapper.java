package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.ActivityUnion;

public interface ActivityUnionMapper {

    /**
     * 根据id列表查询详情
     * 
     * @param ids
     * @return
     */
    ActivityUnion selectById(Long id);

    /**
     * 根据id列表查询详情
     * 
     * @param ids
     * @return
     */
    List<ActivityUnion> selectByIds(List<Long> ids);

    /**
     * 查询所有活动
     * 
     * @return
     */
    List<ActivityUnion> selectAll();

}
