package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.UserUnion;

public interface UserService {

    /**
     * 登录
     * 
     * @param account
     * @param password
     * @return
     */
    UserUnion loginWithAccount(String account, String password);

    /**
     * 修改密码
     * 
     * @param id
     * @param originalPassword
     * @param newPassword
     */
    void changePassword(Long id, String originalPassword, String newPassword);

    /**
     * 充值密码
     * 
     * @param mobile
     */
    void resetPassword(String mobile);

    /**
     * 根据token获取用户信息
     * 
     * @param token
     * @return
     */
    UserUnion queryUserByToken(String token);

    /**
     * 登出
     * 
     * @param userId
     */
    void logout(Long userId);

    /**
     * 新建用户
     * 
     * @param po
     */
    UserPo createUser(UserPo po);

    /**
     * 删除单个用户
     * 
     * @param id
     */
    void removeUser(Long id);

    /**
     * 批量删除用户
     * 
     * @param ids
     */
    void removeUsers(List<Long> ids);

    /**
     * 修改用户
     * 
     * @param po
     */
    UserPo modifyUser(UserPo po);

    /**
     * 用户模糊搜索
     * 
     * @param condition
     * @return
     */
    PagedList<UserUnion> listPagedUsers(UserQueryCondition condition);

}
