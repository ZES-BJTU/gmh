package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.ActivityContentTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerActivityQueryCondition;
import com.zes.squad.gmh.entity.union.CustomerActivityContentUnion;
import com.zes.squad.gmh.entity.union.CustomerActivityUnion;
import com.zes.squad.gmh.service.CustomerActivityService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.CustomerActivityQueryParams;
import com.zes.squad.gmh.web.entity.param.PaymentParams;
import com.zes.squad.gmh.web.entity.vo.CustomerActivityContentVo;
import com.zes.squad.gmh.web.entity.vo.CustomerActivityVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/customerActivity")
@RestController
public class CustomerActivityController {
	@Autowired
	private CustomerActivityService customerActivityService;
	@RequestMapping(path = "/list", method = { RequestMethod.PUT })
	public JsonResult<PagedList<CustomerActivityVo>> doListPagedConsumeRecord(
			@RequestBody CustomerActivityQueryParams params) {
		CheckHelper.checkPageParams(params);
		CustomerActivityQueryCondition customerActivityQueryCondition = CommonConverter.map(params,
				CustomerActivityQueryCondition.class);
		customerActivityQueryCondition.setStoreId(ThreadContext.getUserStoreId());
		PagedList<CustomerActivityUnion> pagedUnions = customerActivityService
				.listPagedCustomerActivity(customerActivityQueryCondition);
		if (CollectionUtils.isEmpty(pagedUnions.getData())) {
			return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
		}
		List<CustomerActivityVo> customerActivityVos = new ArrayList<CustomerActivityVo>();
		for (CustomerActivityUnion caUnion : pagedUnions.getData()) {
			CustomerActivityVo customerActivitydVo = buildAppointmentVoByUnion(caUnion);
			customerActivityVos.add(customerActivitydVo);
		}
		return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
				pagedUnions.getTotalCount(), customerActivityVos));
	}
	
	@RequestMapping(path ="/getActivityPay", method = {RequestMethod.PUT})
	public JsonResult<List<CustomerActivityVo>> doGetCardPay(@RequestBody PaymentParams params){
		checkActivityPayParams(params);
		List<CustomerActivityUnion> unionList = customerActivityService.getAcitvityListByMobile(params.getPaymentWay(),params.getCustomerMobile());
		List<CustomerActivityVo> customerActivityVos = new ArrayList<CustomerActivityVo>();
		for (CustomerActivityUnion caUnion : unionList) {
			CustomerActivityVo customerActivitydVo = buildAppointmentVoByUnion(caUnion);
			customerActivityVos.add(customerActivitydVo);
		}
		
		return JsonResults.success(customerActivityVos);
	}
	
	private CustomerActivityVo buildAppointmentVoByUnion(CustomerActivityUnion caUnion) {
		CustomerActivityVo vo = CommonConverter.map(caUnion, CustomerActivityVo.class);
		List<CustomerActivityContentVo> cacvList = new ArrayList<CustomerActivityContentVo>();
		for(CustomerActivityContentUnion cacu : caUnion.getCustomerActivityContents()){
			CustomerActivityContentVo cacv = CommonConverter.map(cacu, CustomerActivityContentVo.class);			
			cacv.setType(EnumUtils.getDescByKey(cacu.getType(), ActivityContentTypeEnum.class));
			cacvList.add(cacv);
		}
		vo.setCustomerActivityContents(cacvList);
		return vo;
	}
	
	private void checkActivityPayParams(PaymentParams params) {
	
		ensureParameterExist(params.getPaymentWay(), "未输入支付方式");
		ensureParameterExist(params.getCustomerMobile(), "未输入会员手机号");
		if(!(params.getPaymentWay()==2||params.getPaymentWay()==32)){
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "支付方式输入有误");
		}

	}
}
