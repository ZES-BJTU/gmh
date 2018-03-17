package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.MemberCardUnion;

public interface MemberCardUnionMapper {

    /**
     * 根据id查询会员卡详情
     * 
     * @param id
     * @return
     */
    MemberCardUnion selectById(Long id);

    /**
     * 根据条件查询
     * 
     * @return
     */
    List<MemberCardUnion> selectByCondition();

}
