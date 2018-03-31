package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
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
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.MemberCardTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.MemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.MemberCardUnion;
import com.zes.squad.gmh.service.MemberCardService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.MemberCardParams;
import com.zes.squad.gmh.web.entity.param.MemberCardQueryParams;
import com.zes.squad.gmh.web.entity.vo.MemberCardVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/member/cards")
@RestController
public class MemberCardController {

    @Autowired
    private MemberCardService memberCardService;

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<MemberCardVo> doCreateMemberCard(@RequestBody MemberCardParams params) {
        checkMemberCardCreateParams(params);
        MemberCardPo po = CommonConverter.map(params, MemberCardPo.class);
        MemberCardPo newPo = memberCardService.createMemberCard(po);
        MemberCardVo vo = CommonConverter.map(newPo, MemberCardVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveMemberCard(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除会员卡");
        memberCardService.removeMemberCard(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<MemberCardVo> doModifyMemberCard(@PathVariable("id") Long id,
                                                       @RequestBody MemberCardParams params) {
        ensureParameterExist(id, "请选择待修改会员卡");
        ensureParameterExist(params, "请选择待修改会员卡");
        params.setId(id);
        checkMemberCardModifyParams(params);
        MemberCardPo po = CommonConverter.map(params, MemberCardPo.class);
        MemberCardPo newPo = memberCardService.modifyMemberCard(po);
        MemberCardVo vo = CommonConverter.map(newPo, MemberCardVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<MemberCardVo> doQueryMemberCardDetail(@PathVariable("id") Long id) {
        MemberCardUnion union = memberCardService.queryMemberCardDetail(id);
        MemberCardVo vo = buildMemberCardVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<MemberCardVo>> doListPagedMemberCards(MemberCardQueryParams queryParams) {
        CheckHelper.checkPageParams(queryParams);
        if (queryParams.getType() != null) {
            ensureParameterValid(EnumUtils.containsKey(queryParams.getType(), MemberCardTypeEnum.class), "会员卡类型错误");
        }
        MemberCardQueryCondition condition = CommonConverter.map(queryParams, MemberCardQueryCondition.class);
        PagedList<MemberCardUnion> pagedUnions = memberCardService.listPagedMemberCards(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<MemberCardVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (MemberCardUnion union : pagedUnions.getData()) {
            MemberCardVo vo = buildMemberCardVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    private void checkMemberCardCreateParams(MemberCardParams params) {
        ensureParameterExist(params, "会员卡信息为空");
        ensureParameterNotExist(params.getId(), "会员卡已存在");
        ensureParameterExist(params.getType(), "请输入会员卡类别");
        ensureParameterValid(EnumUtils.containsKey(params.getType(), MemberCardTypeEnum.class), "会员卡类别错误");
        ensureParameterExist(params.getCode(), "请输入会员卡编码");
        ensureParameterExist(params.getName(), "请输入会员卡名称");
        ensureParameterExist(params.getPrice(), "请输入会员卡价格");
        ensureParameterValid(params.getPrice().compareTo(BigDecimal.ZERO) == 1, "会员卡价格应大于0");
        if (params.getProjectId() != null) {
            ensureParameterExist(params.getTimes(), "请输入会员卡对应项目次数");
            ensureParameterValid(params.getTimes().intValue() > 0, "会员卡对应项目次数应大于0");
            ensureParameterExist(params.getProjectDiscount(), "请输入会员卡对应项目折扣");
            ensureParameterValid((params.getProjectDiscount().compareTo(BigDecimal.ZERO) == 1)
                    && ((params.getProjectDiscount().compareTo(BigDecimal.ONE) == 0)
                            || (params.getProjectDiscount().compareTo(BigDecimal.ONE) == -1)),
                    "会员卡对应项目折扣错误");
        } else if (params.getAmount() != null) {
            ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "会员卡储值应大于0");
            ensureParameterExist(params.getProductDiscount(), "请输入会员卡对应项目折扣");
            ensureParameterValid((params.getProductDiscount().compareTo(BigDecimal.ZERO) == 1)
                    && ((params.getProductDiscount().compareTo(BigDecimal.ONE) == 0)
                            || (params.getProductDiscount().compareTo(BigDecimal.ONE) == -1)),
                    "会员卡对应产品折扣错误");
        } else {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "请输入会员卡具体信息");
        }
    }

    private void checkMemberCardModifyParams(MemberCardParams params) {
        if (params.getType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getType(), MemberCardTypeEnum.class), "会员卡类别错误");
        }
        if (params.getPrice() != null) {
            ensureParameterValid(params.getPrice().compareTo(BigDecimal.ZERO) == 1, "会员卡价格应大于0");
        }
        if (params.getProjectId() != null) {
            if (params.getTimes() != null) {
                ensureParameterValid(params.getTimes().intValue() > 0, "会员卡对应项目次数应大于0");
            }
            if (params.getProjectDiscount() != null) {
                ensureParameterValid(
                        (params.getProjectDiscount().compareTo(BigDecimal.ZERO) == 1)
                                && ((params.getProjectDiscount().compareTo(BigDecimal.ONE) == 0)
                                        || (params.getProjectDiscount().compareTo(BigDecimal.ONE) == -1)),
                        "会员卡对应项目折扣错误");
            }
        } else if (params.getAmount() != null) {
            ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "会员卡储值应大于0");
            if (params.getProductDiscount() != null) {
                ensureParameterValid(
                        (params.getProductDiscount().compareTo(BigDecimal.ZERO) == 1)
                                && ((params.getProductDiscount().compareTo(BigDecimal.ONE) == 0)
                                        || (params.getProductDiscount().compareTo(BigDecimal.ONE) == -1)),
                        "会员卡对应产品折扣错误");
            }
        } else {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "请输入会员卡改动具体信息");
        }
    }

    private MemberCardVo buildMemberCardVoByUnion(MemberCardUnion union) {
        MemberCardVo vo = CommonConverter.map(union.getMemberCardPo(), MemberCardVo.class);
        vo.setTypeDesc(EnumUtils.getDescByKey(vo.getType(), MemberCardTypeEnum.class));
        if (union.getProjectPo() != null) {
            vo.setProjectName(union.getProjectPo().getName());
        }
        return vo;
    }

}
