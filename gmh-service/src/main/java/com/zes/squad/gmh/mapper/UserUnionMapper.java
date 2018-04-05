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

    /**
     * 根据用户角色查询用户详细信息
     * 
     * @param role
     * @return
     */
    List<UserUnion> selectUserUnionsByRole(Integer role);

    /**
     * 查询所有登录用户
     * 
     * @return
     */
    List<UserUnion> selectOnline();

}
