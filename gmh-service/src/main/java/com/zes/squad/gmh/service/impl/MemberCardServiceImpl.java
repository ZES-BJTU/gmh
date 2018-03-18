package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionValid;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        List<MemberCardPo> existingPos = memberCardMapper.selectByCode(po.getCode());
        ensureCollectionEmpty(existingPos, "会员卡已存在");
        int result = memberCardMapper.insert(po);
        ensureConditionValid(result == 1, "添加会员卡失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeMemberCard(Long id) {
        int result = memberCardMapper.deleteById(id);
        ensureConditionValid(result == 1, "删除会员卡失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeMemberCards(List<Long> ids) {
        int result = memberCardMapper.batchDelete(ids);
        ensureConditionValid(result == ids.size(), "删除会员卡失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public MemberCardPo modifyMemberCard(MemberCardPo po) {
        memberCardMapper.updateSelective(po);
        MemberCardPo newPo = memberCardMapper.selectById(po.getId());
        ensureEntityExist(newPo, "会员卡不存在");
        List<MemberCardPo> pos = memberCardMapper.selectByCode(newPo.getCode());
        ensureCollectionNotEmpty(pos, "会员卡不存在");
        ensureConditionValid(pos.size() == 1, "会员卡重复");
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
        List<MemberCardPo> pos = memberCardMapper.selectByCode(code);
        ensureCollectionNotEmpty(pos, "会员卡不存在");
        MemberCardPo po = pos.get(0);
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
