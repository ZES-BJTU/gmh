package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.UserUnion;

public interface UserUnionMapper {

    /**
     * 根据用户id集合查询用户详细信息
     * 
     * @param ids
     * @return
     */
    List<UserUnion> selectUserUnionsByIds(List<Long> ids);

}
