package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ActivityQueryCondition;
import com.zes.squad.gmh.entity.po.ActivityPo;

public interface ActivityMapper {

    /**
     * 插入活动
     * 
     * @param po
     * @return
     */
    int insert(ActivityPo po);

    /**
     * 根据id删除活动
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除活动
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新活动
     * 
     * @param po
     * @return
     */
    int updateSelective(ActivityPo po);

    /**
     * 根据活动编码查询
     * 
     * @param code
     * @return
     */
    ActivityPo selectByCode(String code);

    /**
     * 根据条件查询活动id列表
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(ActivityQueryCondition condition);

}
