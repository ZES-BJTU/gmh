package com.zes.squad.gmh.mapper;

import com.zes.squad.gmh.entity.po.UserTokenPo;

public interface UserTokenMapper {

    /**
     * 插入或更新单个token对象
     * 
     * @param po
     * @return
     */
    int insertOrUpdate(UserTokenPo po);

    /**
     * 根据token查询
     * 
     * @param token
     * @return
     */
    UserTokenPo selectByToken(String token);

}
