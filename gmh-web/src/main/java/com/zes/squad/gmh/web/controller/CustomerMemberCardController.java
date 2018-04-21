package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.YesOrNoEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.service.CustomerMemberCardService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ChangeCardStoreParams;
import com.zes.squad.gmh.web.entity.param.CustomerMemberCardQueryParams;
import com.zes.squad.gmh.web.entity.param.ModifyValidDateParams;
import com.zes.squad.gmh.web.entity.param.PaymentParams;
import com.zes.squad.gmh.web.entity.param.RechargeOrBuyProjectParams;
import com.zes.squad.gmh.web.entity.param.ReturnCardParams;
import com.zes.squad.gmh.web.entity.param.TurnCardParams;
import com.zes.squad.gmh.web.entity.vo.CustomerMemberCardVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/customerMemberCard")
@RestController
public class CustomerMemberCardController {

    @Autowired
    private CustomerMemberCardService customerMemberCardService;

    @RequestMapping(path = "/list", method = { RequestMethod.PUT })
    public JsonResult<PagedList<CustomerMemberCardVo>> doListPagedConsumeRecord(@RequestBody CustomerMemberCardQueryParams params) {
        CheckHelper.checkPageParams(params);
        CustomerMemberCardQueryCondition customerMemberCardQueryCondition = CommonConverter.map(params,
                CustomerMemberCardQueryCondition.class);
        customerMemberCardQueryCondition.setStoreId(ThreadContext.getUserStoreId());
        PagedList<CustomerMemberCardUnion> pagedUnions = customerMemberCardService
                .listPagedCustomerMemberCard(customerMemberCardQueryCondition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<CustomerMemberCardVo> customerMemberCardVos = new ArrayList<CustomerMemberCardVo>();
        for (CustomerMemberCardUnion customerMemberCardUnion : pagedUnions.getData()) {
            CustomerMemberCardVo customerMemberCardVo = buildAppointmentVoByUnion(customerMemberCardUnion);
            customerMemberCardVos.add(customerMemberCardVo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), customerMemberCardVos));
    }

    @RequestMapping(path = "/changedList", method = { RequestMethod.PUT })
    public JsonResult<PagedList<CustomerMemberCardVo>> doChangedListPagedConsumeRecord(@RequestBody CustomerMemberCardQueryParams params) {
        CheckHelper.checkPageParams(params);
        CustomerMemberCardQueryCondition customerMemberCardQueryCondition = CommonConverter.map(params,
                CustomerMemberCardQueryCondition.class);
        customerMemberCardQueryCondition.setStoreId(ThreadContext.getUserStoreId());
        PagedList<CustomerMemberCardUnion> pagedUnions = customerMemberCardService
                .changedListPagedCustomerMemberCard(customerMemberCardQueryCondition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<CustomerMemberCardVo> customerMemberCardVos = new ArrayList<CustomerMemberCardVo>();
        for (CustomerMemberCardUnion customerMemberCardUnion : pagedUnions.getData()) {
            CustomerMemberCardVo customerMemberCardVo = buildAppointmentVoByUnion(customerMemberCardUnion);
            customerMemberCardVos.add(customerMemberCardVo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), customerMemberCardVos));
    }

    @RequestMapping(path = "/return", method = { RequestMethod.PUT })
    public JsonResult<Void> doReturnCard(@RequestBody ReturnCardParams returnCardParams) {
        if (returnCardParams.getReturnedMoney().compareTo(BigDecimal.ZERO) == -1) {
            return JsonResults.fail(10001, "退款金额应大于0");
        }
        CustomerMemberCardPo po = CommonConverter.map(returnCardParams, CustomerMemberCardPo.class);
        customerMemberCardService.returnCard(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/turn", method = { RequestMethod.PUT })
    public JsonResult<Void> doTurnCard(@RequestBody TurnCardParams turnCardParams) {

        customerMemberCardService.turnCard(turnCardParams.getId(), turnCardParams.getNewCardId(),
                turnCardParams.getTurnedMoney(), turnCardParams.getTurnedReason());
        return JsonResults.success();
    }

    @RequestMapping(path = "/changeStore", method = { RequestMethod.PUT })
    public JsonResult<Void> doChangeStore(@RequestBody ChangeCardStoreParams params) {
        CustomerMemberCardPo po = CommonConverter.map(params, CustomerMemberCardPo.class);
        customerMemberCardService.changeStore(po, params.getNewStoreId(), params.getTurnedMoney(),
                params.getTurnedReason());
        return JsonResults.success();
    }

    @RequestMapping(path = "/changeValidDate", method = { RequestMethod.PUT })
    public JsonResult<Void> doChangeValidDate(@RequestBody ModifyValidDateParams params) {
        customerMemberCardService.changeValidDate(params.getId(), params.getValidDate());
        return JsonResults.success();
    }

    @RequestMapping(path = "/getCardPay", method = { RequestMethod.PUT })
    public JsonResult<List<CustomerMemberCardVo>> doGetCardPay(@RequestBody PaymentParams params) {
        checkCardPayParams(params);
        List<CustomerMemberCardUnion> unionList = customerMemberCardService.getCardListByMobile(params.getPaymentWay(),
                params.getCustomerMobile());
        List<CustomerMemberCardVo> customerMemberCardVos = new ArrayList<CustomerMemberCardVo>();
        for (CustomerMemberCardUnion customerMemberCardUnion : unionList) {
            CustomerMemberCardVo customerMemberCardVo = buildAppointmentVoByUnion(customerMemberCardUnion);
            customerMemberCardVos.add(customerMemberCardVo);
        }

        return JsonResults.success(customerMemberCardVos);
    }

    @RequestMapping(path = "/recharge", method = { RequestMethod.PUT })
    public JsonResult<Void> doRecharge(@RequestBody RechargeOrBuyProjectParams params) {

        customerMemberCardService.recharge(params.getCardId(), params.getRechargeMoney(), params.getConsultantId(),
                params.getSalesManId());

        return JsonResults.success();
    }

    @RequestMapping(path = "/buyProject", method = { RequestMethod.PUT })
    public JsonResult<Void> doBuyProject(@RequestBody RechargeOrBuyProjectParams params) {

        customerMemberCardService.buyProject(params.getCardId(), params.getProjectId(), params.getProjectTimes(),
                params.getUseRemainMoney());

        return JsonResults.success();
    }

    private CustomerMemberCardVo buildAppointmentVoByUnion(CustomerMemberCardUnion customerMemberCardUnion) {

        CustomerMemberCardVo vo = CommonConverter.map(customerMemberCardUnion, CustomerMemberCardVo.class);
        vo.setIsReturned(EnumUtils.getDescByKey(customerMemberCardUnion.getIsReturned(), YesOrNoEnum.class));
        vo.setIsValid(EnumUtils.getDescByKey(customerMemberCardUnion.getIsValid(), YesOrNoEnum.class));
        vo.setIsTurned(EnumUtils.getDescByKey(customerMemberCardUnion.getIsTurned(), YesOrNoEnum.class));

        return vo;
    }

    private void checkCardPayParams(PaymentParams params) {

        ensureParameterExist(params.getPaymentWay(), "未输入支付方式");
        ensureParameterExist(params.getCustomerMobile(), "未输入会员手机号");
        if (!(params.getPaymentWay() == 1 || params.getPaymentWay() == 31)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "支付方式输入有误");
        }

    }
}
