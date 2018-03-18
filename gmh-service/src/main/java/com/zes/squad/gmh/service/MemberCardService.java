package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.MemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.MemberCardUnion;

public interface MemberCardService {

    /**
     * 创建会员卡
     * 
     * @param po
     * @return
     */
    MemberCardPo createMemberCard(MemberCardPo po);

    /**
     * 删除会员卡(单个)
     * 
     * @param id
     */
    void removeMemberCard(Long id);

    /**
     * 删除会员卡(多个)
     * 
     * @param ids
     */
    void removeMemberCards(List<Long> ids);

    /**
     * 修改会员卡
     * 
     * @param po
     * @return
     */
    MemberCardPo modifyMemberCard(MemberCardPo po);

    /**
     * 查询会员卡详情
     * 
     * @param id
     * @return
     */
    MemberCardUnion queryMemberCardDetail(Long id);

    /**
     * 根据项目编码查询会员卡id
     * 
     * @param code
     * @return
     */
    Long queryMemberCardIdByCode(String code);

    /**
     * 根据查询条件查询会员卡
     * 
     * @param condition
     * @return
     */
    PagedList<MemberCardUnion> listPagedMemberCards(MemberCardQueryCondition condition);

}
