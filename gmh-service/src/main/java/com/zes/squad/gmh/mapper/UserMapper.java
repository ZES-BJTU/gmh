package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.UserPo;

public interface UserMapper {

    /**
     * 插入单个用户记录
     * 
     * @param po
     * @return
     */
    int insert(UserPo po);

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

    /**
     * 更新密码
     * 
     * @param id
     * @param password
     * @return
     */
    int updatePassword(@Param("id") long id, @Param("password") String password);

    /**
     * 修改用户信息
     * 
     * @param po
     * @return
     */
    int updateSelective(UserPo po);

    /**
     * 根据id查询用户
     * 
     * @param id
     * @return
     */
    UserPo selectById(Long id);

    /**
     * 根据account查询
     * 
     * @param emailOrAccount
     * @return
     */
    UserPo selectByAccount(@Param("account") String account);

    /**
     * 根据mobile查询
     * 
     * @param email
     * @return
     */
    UserPo selectByEmail(@Param("email") String email);

    /**
     * 根据mobile查询
     * 
     * @param mobile
     * @return
     */
    UserPo selectByMobile(@Param("mobile") String mobile);

    /**
     * 查询用户id集合
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(UserQueryCondition condition);

    /**
     * 根据门店id查询
     * 
     * @param storeId
     * @return
     */
    List<UserPo> selectByStoreId(Long storeId);

}
