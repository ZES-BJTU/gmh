package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.ActivityPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeSaleEmployeePo;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.entity.union.PrintUnion;
import com.zes.squad.gmh.mapper.ActivityUnionMapper;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.mapper.UserMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.service.MessageService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ConsumeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordQueryParams;
import com.zes.squad.gmh.web.entity.param.PrintParams;
import com.zes.squad.gmh.web.entity.vo.ConsumeRecordVo;
import com.zes.squad.gmh.web.entity.vo.PrintVo;
import com.zes.squad.gmh.web.entity.vo.StoreVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/consume")
@RestController
public class ConsumeController {
    @Autowired
    private ConsumeRecordService consumeRecordService;
    @Autowired
    private CustomerMapper       customerMapper;
    @Autowired
    private UserMapper       userMapper;
    @Autowired
    private ActivityUnionMapper  activityUnionMapper;
    @Autowired
    private MessageService       messageService;

    @RequestMapping(path = "/createProductConsume", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateProductConsume(@RequestBody ConsumeCreateOrModifyParams params) {
        Map<String, Object> map = consumeRecordService.getTradeSerialNumber("B");
        consumeRecordService.createProductConsumeRecord(map, params.getConsumeRecordPo(),
                params.getConsumeRecordDetails(), params.getConsumeSaleEmployees());
        return JsonResults.success();
    }

    @RequestMapping(path = "/createCardConsume", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateCardConsume(@RequestBody ConsumeCreateOrModifyParams params) {
        Map<String, Object> map = consumeRecordService.getTradeSerialNumber("C");
        consumeRecordService.createCardConsumeRecord(map, params.getConsumeRecordPo(), params.getConsumeRecordDetails(),
                params.getGifts(), params.getMemberCardPo(), params.getConsumeSaleEmployees());
        return JsonResults.success();
    }

    @RequestMapping(path = "/createProjectConsume", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateProjectConsume(@RequestBody ConsumeCreateOrModifyParams params) {
        Map<String, Object> map = consumeRecordService.getTradeSerialNumber("D");
        consumeRecordService.createProjectConsumeRecord(map, params.getConsumeRecordPo(),
                params.getConsumeRecordDetails(), params.getConsumeSaleEmployees());
        return JsonResults.success();
    }

    @RequestMapping(path = "/createActivityConsume", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateActivityConsume(@RequestBody ConsumeCreateOrModifyParams params) {
        Map<String, Object> map = consumeRecordService.getTradeSerialNumber("A");
        consumeRecordService.createActivityConsumeRecord(map, params.getConsumeRecordPo(),
                params.getConsumeRecordDetails(), params.getConsumeSaleEmployees());
        return JsonResults.success();
    }

    @RequestMapping(path = "/createConsume", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateConsume(@RequestBody ConsumeCreateOrModifyParams params) {
        checkConsumeCreateParams(params);
        UserQueryCondition uerQueryCondition = new UserQueryCondition();
    	uerQueryCondition.setRole(2);
    	uerQueryCondition.setStoreId(ThreadContext.getUserStoreId());
    	List<Long> managerList = userMapper.selectIdsByCondition(uerQueryCondition);
    	UserPo managerUser = userMapper.selectById(managerList.get(0));
    	if(managerList.size()==0 || managerList.size()>1){
    		throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "店铺信息异常");
    	}
    	if(params.getConsumeRecordPo().getCouponAmount() != null){
    		if(params.getConsumeRecordPo().getCouponAmount()>1 && (params.getValidStr()=="" || params.getValidStr()==null)){
            	
            	messageService.sendAuthCode(managerUser.getMobile());
            }
    	}
        
        if (params.getConsumeRecordPo().getCouponAmount() != null) {
            if (params.getConsumeRecordPo().getCouponAmount() > 1) {
                if (params.getValidStr() == null) {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "请输入验证码");
                } else {
                    messageService.validateAuthCode(managerUser.getMobile(),
                            params.getValidStr());
                }
            }
        }
        List<BigDecimal> percentList = getPercentListByStr(params.getPercents());
        List<ConsumeSaleEmployeePo> consumeSaleEmployees = getconsumeSaleEmployeesFromParams(params.getEmployeeIds(),
        		percentList);
        params.setConsumeSaleEmployees(consumeSaleEmployees);
        if (params.getConsumeSaleEmployees().size() != 0) {
            BigDecimal total = new BigDecimal(0);
            for (int i = 0; i < params.getConsumeSaleEmployees().size(); i++) {
                total = total.add(params.getConsumeSaleEmployees().get(i).getPercent());
            }
            if (total.compareTo(new BigDecimal(100)) != 0) {
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "销售员及顾问分担比例总和不为100");
            }
        }
        
        consumeRecordService.createConsumeRecord(params.getConsumeRecordPo(), params.getConsumeRecordDetails(),
                params.getGifts(), params.getMemberCardPo(), params.getConsumeSaleEmployees());
        return JsonResults.success();
    }

    private List<BigDecimal> getPercentListByStr(String percents) {
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		if(percents==null || percents=="")
			return list;
		List<String> tmpList = Arrays.asList(percents.split(","));
		if(tmpList.size()==0){
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "输入格式错误 请以英文逗号分隔");
		}
		for(String tmp : tmpList){
			list.add(new BigDecimal(tmp));
		}
		return list;
	}

	private List<ConsumeSaleEmployeePo> getconsumeSaleEmployeesFromParams(List<Long> employeeIds,
                                                                          List<BigDecimal> percents) {
        List<ConsumeSaleEmployeePo> consumeSaleEmployees = new ArrayList<ConsumeSaleEmployeePo>();
        if (employeeIds == null || percents == null)
            return consumeSaleEmployees;
        if (employeeIds.size() != percents.size()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "员工绩效积分输入有误");
        }

        ConsumeSaleEmployeePo po = new ConsumeSaleEmployeePo();
        for (int i = 0; i < employeeIds.size(); i++) {
            po.setEmployeeId(employeeIds.get(i));
            po.setPercent(percents.get(i));
            consumeSaleEmployees.add(CommonConverter.map(po, ConsumeSaleEmployeePo.class));
        }

        return consumeSaleEmployees;
    }

    @RequestMapping(path = "/calMoney", method = { RequestMethod.PUT })
    public JsonResult<BigDecimal> doCalMoney(@RequestBody ConsumeCreateOrModifyParams params) {

        BigDecimal money = consumeRecordService.doCalMoney(params.getConsumeRecordPo(),
                params.getConsumeRecordDetails(), params.getGifts(), params.getMemberCardPo());
        return JsonResults.success(money);
    }

    @RequestMapping(path = "/modifyConsume", method = { RequestMethod.PUT })
    public JsonResult<Void> doModify(@RequestBody ConsumeCreateOrModifyParams params) {
        checkConsumeCreateParams(params);
        UserQueryCondition uerQueryCondition = new UserQueryCondition();
    	uerQueryCondition.setRole(2);
    	uerQueryCondition.setStoreId(ThreadContext.getUserStoreId());
    	List<Long> managerList = userMapper.selectIdsByCondition(uerQueryCondition);
    	UserPo managerUser = userMapper.selectById(managerList.get(0));
    	if(managerList.size()==0 || managerList.size()>1){
    		throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "店铺信息异常");
    	}
    	
        if(params.getConsumeRecordPo().getCouponAmount()>1 && (params.getValidStr()=="" || params.getValidStr()==null)){
        	
        	messageService.sendAuthCode(managerUser.getMobile());
        }
        if (params.getConsumeRecordPo().getCouponAmount() != null) {
            if (params.getConsumeRecordPo().getCouponAmount() > 1) {
                if (params.getValidStr() == null) {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "请输入验证码");
                } else {
                    messageService.validateAuthCode(managerUser.getMobile(),
                            params.getValidStr());
                }
            }
        }

        consumeRecordService.modify(params.getConsumeRecordPo(), params.getConsumeRecordDetails(), params.getGifts(),
                params.getConsumeRecordPo().getId(), params.getMemberCardPo(), params.getConsumeSaleEmployees());
        return JsonResults.success();
    }

    @RequestMapping(path = "/print", method = { RequestMethod.PUT })
    public JsonResult<PrintVo> doPrint(@RequestBody PrintParams params) {

        PrintUnion printUnion = consumeRecordService.getPrint(params.getConsumeRecordId());
        PrintVo printVo = buildPrintVoByUnion(printUnion);
        return JsonResults.success(printVo);
    }

    @RequestMapping(path = "/list", method = { RequestMethod.PUT })
    public JsonResult<PagedList<ConsumeRecordVo>> doListPagedConsumeRecord(@RequestBody ConsumeRecordQueryParams params) {
        CheckHelper.checkPageParams(params);
        ConsumeRecordQueryCondition consumeRecordQueryCondition = CommonConverter.map(params,
                ConsumeRecordQueryCondition.class);
        consumeRecordQueryCondition.setStoreId(ThreadContext.getUserStoreId());
        PagedList<ConsumeRecordUnion> pagedUnions = consumeRecordService
                .listPagedConsumeRecords(consumeRecordQueryCondition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ConsumeRecordVo> consumeRecordVos = new ArrayList<ConsumeRecordVo>();
        for (ConsumeRecordUnion consumeRecordUnion : pagedUnions.getData()) {
            ConsumeRecordVo consumeRecordVo = buildAppointmentVoByUnion(consumeRecordUnion);
            consumeRecordVos.add(consumeRecordVo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), consumeRecordVos));
    }

    @RequestMapping(path = "/changedList", method = { RequestMethod.PUT })
    public JsonResult<PagedList<ConsumeRecordVo>> doChangedListPagedConsumeRecord(@RequestBody ConsumeRecordQueryParams params) {
        CheckHelper.checkPageParams(params);
        ConsumeRecordQueryCondition consumeRecordQueryCondition = CommonConverter.map(params,
                ConsumeRecordQueryCondition.class);
        consumeRecordQueryCondition.setStoreId(ThreadContext.getUserStoreId());
        PagedList<ConsumeRecordUnion> pagedUnions = consumeRecordService
                .changedListPagedConsumeRecords(consumeRecordQueryCondition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ConsumeRecordVo> consumeRecordVos = new ArrayList<ConsumeRecordVo>();
        for (ConsumeRecordUnion consumeRecordUnion : pagedUnions.getData()) {
            ConsumeRecordVo consumeRecordVo = buildAppointmentVoByUnion(consumeRecordUnion);
            consumeRecordVos.add(consumeRecordVo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), consumeRecordVos));
    }

    private ConsumeRecordVo buildAppointmentVoByUnion(ConsumeRecordUnion consumeRecordUnion) {
        ConsumeRecordVo vo = CommonConverter.map(consumeRecordUnion.getConsumeRecordPo(), ConsumeRecordVo.class);
        ConsumeRecordPo po = consumeRecordUnion.getConsumeRecordPo();

        CustomerPo customerPo = customerMapper.getByMobile(consumeRecordUnion.getConsumeRecordPo().getCustomerMobile());
        vo.setConsumeRecordDetailUnion(consumeRecordUnion.getConsumeRecordDetailUnion());
        vo.setConsumeRecordGiftUnion(consumeRecordUnion.getConsumeRecordGiftUnion());
        vo.setConsumeSaleEmployees(consumeRecordUnion.getConsumeSaleEmployees());
        vo.setCustomerName(customerPo.getName());
        if (po.getActivityId() != null) {
            ActivityPo activity = activityUnionMapper.selectById(po.getActivityId()).getActivityPo();
            vo.setActivityName(activity.getName());
        }
        if (po.getConsumeType() == 1) {
            vo.setConsumeType("办卡");
        }
        if (po.getConsumeType() == 2) {
            vo.setConsumeType("买产品");
        }
        if (po.getConsumeType() == 3) {
            vo.setConsumeType("做项目");
        }
        if (po.getConsumeType() == 4) {
            vo.setConsumeType("参加活动");
        }
        if (po.getConsumeType() == 5) {
            vo.setConsumeType("充值");
        }

        if (po.getPaymentWay() == 1) {
            vo.setPaymentWayName("会员卡");
        }
        if (po.getPaymentWay() == 2) {
            vo.setPaymentWayName("活动");
        }
        if (po.getPaymentWay() == 3) {
            vo.setPaymentWayName("现金");
        }
        if (po.getPaymentWay() == 31) {
            vo.setPaymentWayName("现金及代金券");
        }
        if (po.getPaymentWay() == 32) {
            vo.setPaymentWayName("现金及代金券");
        }
        if (po.getPaymentWay() == 4) {
            vo.setPaymentWayName("赠送");
        }

        return vo;
    }

    private void checkConsumeCreateParams(ConsumeCreateOrModifyParams params) {
        ConsumeRecordPo consumeRecordPo = params.getConsumeRecordPo();
        List<ConsumeRecordDetailPo> detailList = params.getConsumeRecordDetails();
        MemberCardPo memberCardPo = params.getMemberCardPo();
        ensureParameterExist(consumeRecordPo.getConsumeType(), "未输入消费类型");
        if (consumeRecordPo.getConsumeType() == 1) {
            ensureParameterExist(memberCardPo, "请选择会员卡");
        }
        if (consumeRecordPo.getConsumeType() == 2) {
            ensureParameterExist(detailList, "请选择产品");
        }
        if (consumeRecordPo.getConsumeType() == 3) {
            ensureParameterExist(detailList, "请选择项目");
        }
        if (consumeRecordPo.getConsumeType() == 4) {
            ensureParameterExist(consumeRecordPo.getActivityId(), "请选择活动");
        }
        if (consumeRecordPo.getPaymentWay() == 1) {
            ensureParameterExist(consumeRecordPo.getPayWayId(), "请选择会员卡");
            //			ensureParameterExist(consumeRecordPo.getPayWayContentId(), "请选择会员卡");
        }
        if (consumeRecordPo.getPaymentWay() == 2) {
            ensureParameterExist(consumeRecordPo.getPayWayId(), "请选择活动");
            ensureParameterExist(consumeRecordPo.getPayWayContentId(), "请选择活动");
        }

        if (consumeRecordPo.getPaymentWay() == 31) {
            ensureParameterExist(consumeRecordPo.getPayWayId(), "请选择会员卡");
            ensureParameterExist(consumeRecordPo.getPayWayContentId(), "请选择会员卡");
            ensureParameterExist(consumeRecordPo.getCouponAmount(), "请输入代金券数量");
        }
        if (consumeRecordPo.getPaymentWay() == 32) {
            ensureParameterExist(consumeRecordPo.getPayWayId(), "请选择活动");
            ensureParameterExist(consumeRecordPo.getPayWayContentId(), "请选择活动");
            ensureParameterExist(consumeRecordPo.getCouponAmount(), "请输入代金券数量");
        }

    }

    private PrintVo buildPrintVoByUnion(PrintUnion printUnion) {

        PrintVo printVo = new PrintVo();
        ConsumeRecordPo consumeRecordPo = printUnion.getConsumeRecordPo();
        ConsumeRecordVo consumeRecordVo = CommonConverter.map(consumeRecordPo, ConsumeRecordVo.class);
        StoreVo storeVo = CommonConverter.map(printUnion.getStorePo(), StoreVo.class);
        consumeRecordVo.setConsumeRecordDetailUnion(printUnion.getConsumeRecordDetailUnion());
        consumeRecordVo.setConsumeRecordGiftUnion(printUnion.getConsumeRecordGiftUnion());
        CustomerPo customerPo = customerMapper.getByMobile(consumeRecordPo.getCustomerMobile());
        consumeRecordVo.setCustomerName(customerPo.getName());
        if (consumeRecordPo.getActivityId() != null) {
            ActivityPo activity = activityUnionMapper.selectById(consumeRecordPo.getActivityId()).getActivityPo();
            consumeRecordVo.setActivityName(activity.getName());
        }
        if (consumeRecordPo.getConsumeType() == 1) {
            consumeRecordVo.setConsumeType("办卡");
        }
        if (consumeRecordPo.getConsumeType() == 2) {
            consumeRecordVo.setConsumeType("买产品");
        }
        if (consumeRecordPo.getConsumeType() == 3) {
            consumeRecordVo.setConsumeType("做项目");
        }
        if (consumeRecordPo.getConsumeType() == 4) {
            consumeRecordVo.setConsumeType("参加活动");
        }

        if (consumeRecordPo.getPaymentWay() == 1) {
            consumeRecordVo.setPaymentWayName("会员卡");
        }
        if (consumeRecordPo.getPaymentWay() == 2) {
            consumeRecordVo.setPaymentWayName("活动");
        }
        if (consumeRecordPo.getPaymentWay() == 3) {
            consumeRecordVo.setPaymentWayName("现金");
        }
        if (consumeRecordPo.getPaymentWay() == 31) {
            consumeRecordVo.setPaymentWayName("现金及代金券");
        }
        if (consumeRecordPo.getPaymentWay() == 32) {
            consumeRecordVo.setPaymentWayName("现金及代金券");
        }
        if (consumeRecordPo.getPaymentWay() == 4) {
            consumeRecordVo.setPaymentWayName("赠送");
        }
        printVo.setConsumeRecordVo(consumeRecordVo);
        printVo.setStoreVo(storeVo);
        printVo.setCustomerMemberCardUnions(printUnion.getCustomerMemberCardUnions());
        return printVo;
    }

}
