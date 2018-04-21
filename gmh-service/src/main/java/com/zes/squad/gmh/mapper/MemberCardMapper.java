package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.MemberCardPo;

public interface MemberCardMapper {

    /**
     * 新增会员卡
     * 
     * @param po
     * @return
     */
    int insert(MemberCardPo po);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int updateStatus(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 修改会员卡
     * 
     * @param po
     * @return
     */
    int updateSelective(MemberCardPo po);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    MemberCardPo selectById(Long id);

    /**
     * 根据会员卡编码查询id
     * 
     * @param code
     * @return
     */
    MemberCardPo selectByCode(String code);

}
