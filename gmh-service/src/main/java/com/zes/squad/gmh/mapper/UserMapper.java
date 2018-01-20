package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.UserUnion;

public interface UserMapper {

    /**
     * 根据id查询用户
     * 
     * @param id
     * @return
     */
    UserPo selectById(Long id);

    /**
     * 根据emailOrAccount查询
     * 
     * @param emailOrAccount
     * @return
     */
    UserPo selectByAccount(@Param("account") String account);

    /**
     * 更新密码
     * 
     * @param password
     * @return
     */
    int updatePassword(String password);

    /**
     * 插入单个用户记录
     * 
     * @param po
     * @return
     */
    int insert(UserPo po);

    /**
     * 修改用户信息
     * 
     * @param po
     * @return
     */
    int updateSelective(UserPo po);

    /**
     * 查询用户id集合
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(UserQueryCondition condition);

    /**
     * 根据用户id集合查询用户详细信息
     * 
     * @param ids
     * @return
     */
    List<UserUnion> selectUserUnionsByIds(List<Long> ids);

    /**
     * 删除单个用户
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除用户
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

}
