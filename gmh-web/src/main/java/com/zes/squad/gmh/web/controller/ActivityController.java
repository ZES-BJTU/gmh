package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.ActivityContentTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.ActivityQueryCondition;
import com.zes.squad.gmh.entity.po.ActivityContentPo;
import com.zes.squad.gmh.entity.po.ActivityPo;
import com.zes.squad.gmh.entity.union.ActivityContentUnion;
import com.zes.squad.gmh.entity.union.ActivityUnion;
import com.zes.squad.gmh.service.ActivityService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ActivityContentParams;
import com.zes.squad.gmh.web.entity.param.ActivityParams;
import com.zes.squad.gmh.web.entity.param.ActivityQueryParams;
import com.zes.squad.gmh.web.entity.vo.ActivityContentVo;
import com.zes.squad.gmh.web.entity.vo.ActivityVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/activities")
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ActivityVo> doCreateActivity(@RequestBody ActivityParams params) {
        checkActivityCreateParams(params);
        ActivityUnion union = buildActivityUnionByParams(params);
        ActivityUnion newUnion = activityService.createActivity(union);
        ActivityVo vo = buildActivityVoByUnion(newUnion);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveActivity(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除活动");
        activityService.removeActivity(id);
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveActivities(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除活动");
        activityService.removeActivities(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<ActivityVo> doModifyActivity(@PathVariable("id") Long id, @RequestBody ActivityParams params) {
        ensureParameterExist(id, "请选择待修改活动");
        params.setId(id);
        checkActivityModifyParams(params);
        ActivityUnion union = buildActivityUnionByParams(params);
        ActivityUnion newUnion = activityService.modifyActivity(union);
        ActivityVo vo = buildActivityVoByUnion(newUnion);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<ActivityVo> doQueryActivityDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "活动不存在");
        ActivityUnion union = activityService.queryActivityDetail(id);
        ActivityVo vo = buildActivityVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<ActivityVo>> doListPagedActivities(ActivityQueryParams queryParams) {
        CheckHelper.checkPageParams(queryParams);
        ActivityQueryCondition condition = CommonConverter.map(queryParams, ActivityQueryCondition.class);
        PagedList<ActivityUnion> pagedUnions = activityService.listPagedActivities(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ActivityUnion> unions = pagedUnions.getData();
        List<ActivityVo> vos = Lists.newArrayListWithCapacity(unions.size());
        for (ActivityUnion union : unions) {
            ActivityVo vo = buildActivityVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/all", method = { RequestMethod.GET })
    public JsonResult<List<ActivityVo>> doListAllActivity() {
        List<ActivityUnion> unions = activityService.listAllActivities();
        if (CollectionUtils.isEmpty(unions)) {
            return JsonResults.success(Lists.newArrayList());
        }
        List<ActivityVo> vos = Lists.newArrayListWithCapacity(unions.size());
        for (ActivityUnion union : unions) {
            ActivityVo vo = buildActivityVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(vos);
    }

    private void checkActivityCreateParams(ActivityParams params) {
        ensureParameterExist(params, "请输入活动信息");
        ensureParameterNotExist(params.getId(), "活动已存在");
        ensureParameterExist(params.getCode(), "请输入活动编码");
        ensureParameterExist(params.getName(), "请输入活动名称");
        ensureParameterExist(params.getPrice(), "请输入活动价格");
        ensureParameterValid(params.getPrice().compareTo(BigDecimal.ZERO) == 1, "活动价格应大于0");
        ensureParameterExist(params.getDeadline(), "请输入活动有效期");
        ensureParameterValid(params.getDeadline().after(new Date()), "活动有效期不合法");
        ensureCollectionNotEmpty(params.getActivityContentParams(), "请输入活动内容");
        List<Long> projectIds = Lists.newArrayList();
        List<Long> cardIds = Lists.newArrayList();
        List<Long> productIds = Lists.newArrayList();
        List<BigDecimal> contents = Lists.newArrayList();
        for (ActivityContentParams contentParams : params.getActivityContentParams()) {
            ensureParameterExist(contentParams, "请输入活动内容");
            ensureParameterExist(contentParams.getType(), "活动内容分类为空");
            ensureParameterValid(EnumUtils.containsKey(contentParams.getType(), ActivityContentTypeEnum.class),
                    "活动内容分类错误");
            if (contentParams.getType() == ActivityContentTypeEnum.PROJECT.getKey()) {
                ensureParameterExist(contentParams.getRelatedId(), "请选择活动项目");
                projectIds.add(contentParams.getRelatedId());
                ensureParameterExist(contentParams.getNumber(), "活动项目次数为空");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动项目次数应大于0");
            } else if (contentParams.getType() == ActivityContentTypeEnum.MEMBER_CARD.getKey()) {
                ensureParameterExist(contentParams.getRelatedId(), "请选择活动会员卡");
                cardIds.add(contentParams.getRelatedId());
                ensureParameterExist(contentParams.getNumber(), "活动会员卡张数为空");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动会员卡张数应大于0");
            } else if (contentParams.getType() == ActivityContentTypeEnum.PRODUCT.getKey()) {
                ensureParameterExist(contentParams.getRelatedId(), "请选择活动产品");
                productIds.add(contentParams.getRelatedId());
                ensureParameterExist(contentParams.getNumber(), "请输入活动产品个数");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动产品个数应大于0");
            } else {
                ensureParameterExist(contentParams.getContent(), "请输入代金券额度");
                ensureParameterValid(contentParams.getContent().compareTo(BigDecimal.ZERO) == 1, "代金券金额应大于0");
                contents.add(contentParams.getContent());
                ensureParameterExist(contentParams.getNumber(), "请输入活动代金券张数");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动代金券张数应大于0");
            }
        }
        if (projectIds.size() != Sets.newHashSet(projectIds).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动项目存在重复");
        }
        if (cardIds.size() != Sets.newHashSet(cardIds).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动会员卡存在重复");
        }
        if (productIds.size() != Sets.newHashSet(productIds).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动产品存在重复");
        }
        if (contents.size() != Sets.newHashSet(contents).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动代金券存在重复");
        }
    }

    private ActivityUnion buildActivityUnionByParams(ActivityParams params) {
        ActivityPo po = CommonConverter.map(params, ActivityPo.class);
        ActivityUnion union = new ActivityUnion();
        union.setActivityPo(po);
        List<ActivityContentPo> contentPos = Lists.newArrayListWithCapacity(params.getActivityContentParams().size());
        for (ActivityContentParams contentParams : params.getActivityContentParams()) {
            ActivityContentPo contentPo = CommonConverter.map(contentParams, ActivityContentPo.class);
            contentPos.add(contentPo);
        }
        List<ActivityContentUnion> contentUnions = Lists.newArrayListWithCapacity(contentPos.size());
        for (ActivityContentPo contentPo : contentPos) {
            ActivityContentUnion contentUnion = new ActivityContentUnion();
            contentUnion.setActivityContentPo(contentPo);
            contentUnions.add(contentUnion);
        }
        union.setActivityContentUnions(contentUnions);
        return union;
    }

    private void checkActivityModifyParams(ActivityParams params) {
        ensureParameterExist(params, "请输入活动信息");
        if (params.getPrice() != null) {
            ensureParameterValid(params.getPrice().compareTo(BigDecimal.ZERO) == 1, "活动价格应大于0");
        }
        if (params.getDeadline() != null) {
            ensureParameterValid(params.getDeadline().after(new Date()), "活动有效期不合法");
        }
        ensureCollectionNotEmpty(params.getActivityContentParams(), "请输入活动内容");
        List<Long> projectIds = Lists.newArrayList();
        List<Long> cardIds = Lists.newArrayList();
        List<Long> productIds = Lists.newArrayList();
        List<BigDecimal> contents = Lists.newArrayList();
        for (ActivityContentParams contentParams : params.getActivityContentParams()) {
            ensureParameterExist(contentParams, "请输入活动内容");
            ensureParameterExist(contentParams.getType(), "活动内容分类为空");
            ensureParameterValid(EnumUtils.containsKey(contentParams.getType(), ActivityContentTypeEnum.class),
                    "活动内容分类错误");
            if (contentParams.getType() == ActivityContentTypeEnum.PROJECT.getKey()) {
                ensureParameterExist(contentParams.getRelatedId(), "请选择活动项目");
                projectIds.add(contentParams.getRelatedId());
                ensureParameterExist(contentParams.getNumber(), "活动项目次数为空");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动项目次数应大于0");
            } else if (contentParams.getType() == ActivityContentTypeEnum.MEMBER_CARD.getKey()) {
                ensureParameterExist(contentParams.getRelatedId(), "请选择活动会员卡");
                cardIds.add(contentParams.getRelatedId());
                ensureParameterExist(contentParams.getNumber(), "活动会员卡张数为空");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动会员卡张数应大于0");
            } else if (contentParams.getType() == ActivityContentTypeEnum.PRODUCT.getKey()) {
                ensureParameterExist(contentParams.getRelatedId(), "请选择活动产品");
                productIds.add(contentParams.getRelatedId());
                ensureParameterExist(contentParams.getNumber(), "请输入活动产品个数");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动产品个数应大于0");
            } else {
                ensureParameterExist(contentParams.getContent(), "请输入代金券额度");
                ensureParameterValid(contentParams.getContent().compareTo(BigDecimal.ZERO) == 1, "代金券金额应大于0");
                contents.add(contentParams.getContent());
                ensureParameterExist(contentParams.getNumber(), "请输入活动代金券张数");
                ensureParameterValid(contentParams.getNumber().compareTo(BigDecimal.ZERO) == 1, "活动代金券张数应大于0");
            }
        }
        if (projectIds.size() != Sets.newHashSet(projectIds).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动项目存在重复");
        }
        if (cardIds.size() != Sets.newHashSet(cardIds).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动会员卡存在重复");
        }
        if (productIds.size() != Sets.newHashSet(productIds).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动产品存在重复");
        }
        if (contents.size() != Sets.newHashSet(contents).size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "活动代金券存在重复");
        }
    }

    private ActivityVo buildActivityVoByUnion(ActivityUnion union) {
        ActivityVo vo = CommonConverter.map(union.getActivityPo(), ActivityVo.class);
        List<ActivityContentVo> contentVos = Lists.newArrayListWithCapacity(union.getActivityContentUnions().size());
        for (ActivityContentUnion contentUnion : union.getActivityContentUnions()) {
            ActivityContentVo contentVo = CommonConverter.map(contentUnion.getActivityContentPo(),
                    ActivityContentVo.class);
            contentVo.setType(EnumUtils.getDescByKey(contentUnion.getActivityContentPo().getType(),
                    ActivityContentTypeEnum.class));
            if (contentUnion.getActivityContentPo().getType() == ActivityContentTypeEnum.PROJECT.getKey()) {
                if (contentUnion.getProjectPo() != null) {
                    contentVo.setRelatedName(String.valueOf(contentUnion.getProjectPo().getName()));
                }
            } else if (contentUnion.getActivityContentPo().getType() == ActivityContentTypeEnum.MEMBER_CARD.getKey()) {
                if (contentUnion.getMemberCardPo() != null) {
                    contentVo.setRelatedName(String.valueOf(contentUnion.getMemberCardPo().getName()));
                }
            } else if (contentUnion.getActivityContentPo().getType() == ActivityContentTypeEnum.PRODUCT.getKey()) {
                if (contentUnion.getProductPo() != null) {
                    contentVo.setRelatedName(String.valueOf(contentUnion.getProductPo().getName()));
                }
            } else {
                contentVo.setContent(contentUnion.getActivityContentPo().getContent());
            }
            contentVos.add(contentVo);
        }
        vo.setActivityContentVos(contentVos);
        return vo;
    }

}
