package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.MemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.MemberCardUnion;
import com.zes.squad.gmh.mapper.MemberCardMapper;
import com.zes.squad.gmh.mapper.MemberCardUnionMapper;
import com.zes.squad.gmh.service.MemberCardService;

@Service("memberCardService")
public class MemberCardServiceImpl implements MemberCardService {

    @Autowired
    private MemberCardMapper      memberCardMapper;
    @Autowired
    private MemberCardUnionMapper memberCardUnionMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public MemberCardPo createMemberCard(MemberCardPo po) {
        MemberCardPo existingPo = memberCardMapper.selectByCode(po.getCode());
        ensureEntityNotExist(existingPo, "会员卡已存在");
        int result = memberCardMapper.insert(po);
        ensureConditionSatisfied(result == 1, "添加会员卡失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeMemberCard(Long id) {
        int result = memberCardMapper.updateStatus(id);
        ensureConditionSatisfied(result == 1, "会员卡置为无效失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeMemberCards(List<Long> ids) {
        int result = memberCardMapper.batchDelete(ids);
        ensureConditionSatisfied(result == ids.size(), "删除会员卡失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public MemberCardPo modifyMemberCard(MemberCardPo po) {
        if (!Strings.isNullOrEmpty(po.getCode())) {
            MemberCardPo cardPo = memberCardMapper.selectByCode(po.getCode());
            if (cardPo != null) {
                ensureConditionSatisfied(cardPo.getId().equals(po.getId()), "会员卡重复");
            }
        }
        memberCardMapper.updateSelective(po);
        MemberCardPo newPo = memberCardMapper.selectById(po.getId());
        ensureEntityExist(newPo, "会员卡不存在");
        return newPo;
    }

    @Override
    public MemberCardUnion queryMemberCardDetail(Long id) {
        MemberCardUnion union = memberCardUnionMapper.selectById(id);
        ensureEntityExist(union, "会员卡不存在");
        ensureEntityExist(union.getMemberCardPo(), "会员卡不存在");
        return union;
    }

    @Override
    public Long queryMemberCardIdByCode(String code) {
        ensureParameterExist(code, "会员卡编码为空");
        MemberCardPo po = memberCardMapper.selectByCode(code);
        ensureEntityExist(po, "会员卡不存在");
        return po.getId();
    }

    @Override
    public PagedList<MemberCardUnion> listPagedMemberCards(MemberCardQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<MemberCardUnion> unions = memberCardUnionMapper.selectByCondition();
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<MemberCardUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
