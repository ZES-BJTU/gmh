package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.enums.ActivityContentTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ActivityQueryCondition;
import com.zes.squad.gmh.entity.po.ActivityContentPo;
import com.zes.squad.gmh.entity.po.ActivityPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProjectPo;
import com.zes.squad.gmh.entity.union.ActivityContentUnion;
import com.zes.squad.gmh.entity.union.ActivityUnion;
import com.zes.squad.gmh.mapper.ActivityContentMapper;
import com.zes.squad.gmh.mapper.ActivityMapper;
import com.zes.squad.gmh.mapper.ActivityUnionMapper;
import com.zes.squad.gmh.mapper.MemberCardMapper;
import com.zes.squad.gmh.mapper.ProductMapper;
import com.zes.squad.gmh.mapper.ProjectMapper;
import com.zes.squad.gmh.service.ActivityService;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper        activityMapper;
    @Autowired
    private ActivityContentMapper activityContentMapper;
    @Autowired
    private ActivityUnionMapper   activityUnionMapper;
    @Autowired
    private ProjectMapper         projectMapper;
    @Autowired
    private MemberCardMapper      memberCardMapper;
    @Autowired
    private ProductMapper         productMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ActivityUnion createActivity(ActivityUnion union) {
        ActivityPo po = union.getActivityPo();
        ActivityPo existingPo = activityMapper.selectByCode(po.getCode());
        ensureEntityNotExist(existingPo, "活动编码被占用");
        activityMapper.insert(po);
        List<ActivityContentUnion> contentUnions = union.getActivityContentUnions();
        List<ActivityContentPo> contentPos = Lists.newArrayListWithCapacity(contentUnions.size());
        for (ActivityContentUnion contentUnion : contentUnions) {
            ActivityContentPo contentPo = contentUnion.getActivityContentPo();
            contentPo.setActivityId(po.getId());
            contentPos.add(contentPo);
        }
        activityContentMapper.batchInsert(contentPos);
        return union;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeActivity(Long id) {
        int record = activityMapper.deleteById(id);
        ensureConditionSatisfied(record == 1, "活动删除失败");
        activityContentMapper.batchDeleteByActivityId(Lists.newArrayList(id));
        List<ActivityContentPo> contentPos = activityContentMapper.selectByActivityId(id);
        ensureCollectionEmpty(contentPos, "活动删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeActivities(List<Long> ids) {
        int records = activityMapper.batchDelete(ids);
        ensureConditionSatisfied(records == ids.size(), "活动删除失败");
        activityContentMapper.batchDeleteByActivityId(ids);
        for (Long id : ids) {
            List<ActivityContentPo> contentPos = activityContentMapper.selectByActivityId(id);
            ensureCollectionEmpty(contentPos, "活动删除失败");
        }
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ActivityUnion modifyActivity(ActivityUnion union) {
        ActivityPo po = union.getActivityPo();
        ActivityPo activityPo = activityMapper.selectByCode(po.getCode());
        if (activityPo != null) {
            ensureConditionSatisfied(activityPo.getId().equals(po.getId()), "活动已存在");
        }
        activityMapper.updateSelective(po);
        activityContentMapper.batchDeleteByActivityId(Lists.newArrayList(po.getId()));
        List<ActivityContentPo> contentPos = activityContentMapper.selectByActivityId(po.getId());
        ensureCollectionEmpty(contentPos, "活动修改失败");
        List<ActivityContentUnion> contentUnions = union.getActivityContentUnions();
        contentPos = Lists.newArrayListWithCapacity(contentUnions.size());
        for (ActivityContentUnion contentUnion : contentUnions) {
            ActivityContentPo contentPo = contentUnion.getActivityContentPo();
            contentPo.setActivityId(po.getId());
            contentPos.add(contentPo);
        }
        activityContentMapper.batchInsert(contentPos);
        return union;
    }

    @Override
    public ActivityUnion queryActivityDetail(Long id) {
        ActivityUnion union = activityUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到活动");
        ensureEntityExist(union.getActivityPo(), "未找到活动");
        ensureCollectionNotEmpty(union.getActivityContentUnions(), "未找到活动内容");
        buildActivityContentUnions(union.getActivityContentUnions());
        return union;
    }

    @Override
    public PagedList<ActivityUnion> listPagedActivities(ActivityQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Long> ids = activityMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<ActivityUnion> unions = activityUnionMapper.selectByIds(ids);
        for (ActivityUnion union : unions) {
            ensureEntityExist(union, "未找到活动");
            ensureEntityExist(union.getActivityPo(), "未找到活动");
            ensureCollectionNotEmpty(union.getActivityContentUnions(), "未找到活动内容");
            buildActivityContentUnions(union.getActivityContentUnions());
        }
        PageInfo<Long> info = new PageInfo<>(ids);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Override
    public List<ActivityUnion> listAllActivities() {
        List<ActivityUnion> unions = activityUnionMapper.selectAll();
        for (ActivityUnion union : unions) {
            ensureEntityExist(union, "未找到活动");
            ensureEntityExist(union.getActivityPo(), "未找到活动");
            ensureCollectionNotEmpty(union.getActivityContentUnions(), "未找到活动内容");
            buildActivityContentUnions(union.getActivityContentUnions());
        }
        return unions;
    }

    private void buildActivityContentUnions(List<ActivityContentUnion> contentUnions) {
        for (ActivityContentUnion contentUnion : contentUnions) {
            ensureEntityExist(contentUnion, "未找到活动内容");
            ensureEntityExist(contentUnion.getActivityContentPo(), "未找到活动内容");
            if (contentUnion.getActivityContentPo().getType() == ActivityContentTypeEnum.PROJECT.getKey()) {
                ProjectPo projectPo = projectMapper.selectById(contentUnion.getActivityContentPo().getRelatedId());
                contentUnion.setProjectPo(projectPo);
            }
            if (contentUnion.getActivityContentPo().getType() == ActivityContentTypeEnum.MEMBER_CARD.getKey()) {
                MemberCardPo memberCardPo = memberCardMapper
                        .selectById(contentUnion.getActivityContentPo().getRelatedId());
                contentUnion.setMemberCardPo(memberCardPo);
            }
            if (contentUnion.getActivityContentPo().getType() == ActivityContentTypeEnum.PRODUCT.getKey()) {
                ProductPo productPo = productMapper.selectById(contentUnion.getActivityContentPo().getRelatedId());
                contentUnion.setProductPo(productPo);
            }
        }
    }

}
