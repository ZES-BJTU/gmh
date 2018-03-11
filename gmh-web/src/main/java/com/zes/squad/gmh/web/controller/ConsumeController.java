package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ConsumeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordQueryParams;
import com.zes.squad.gmh.web.entity.vo.ConsumeRecordVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/consume")
@RestController
public class ConsumeController {
	@Autowired
    private ConsumeRecordService consumeRecordService;
	@Autowired
    private CustomerMapper customerMapper;
	 @RequestMapping(path = "/createProductConsume", method = { RequestMethod.PUT })
	    public JsonResult<Void> doCreateProductConsume(@RequestBody ConsumeCreateOrModifyParams params) {
		 	consumeRecordService.createProductConsumeRecord(params.getConsumeRecordPo(), params.getConsumeRecordDetails());
	        return JsonResults.success();
	    }
	 @RequestMapping(path = "/createCardConsume", method = { RequestMethod.PUT })
	    public JsonResult<Void> doCreateCardConsume(@RequestBody ConsumeCreateOrModifyParams params) {
		 consumeRecordService.createCardConsumeRecord(params.getConsumeRecordPo(), params.getConsumeRecordDetails(), params.getGifts());
	        return JsonResults.success();
	    }
	 @RequestMapping(path = "/createProjectConsume", method = { RequestMethod.PUT })
	    public JsonResult<Void> doCreateProjectConsume(@RequestBody ConsumeCreateOrModifyParams params) {
		 consumeRecordService.createProjectConsumeRecord(params.getConsumeRecordPo(), params.getConsumeRecordDetails());
	        return JsonResults.success();
	    }
	 @RequestMapping(path = "/list", method = { RequestMethod.PUT })
	    public JsonResult<PagedList<ConsumeRecordVo>> doListPagedConsumeRecord(@RequestBody ConsumeRecordQueryParams params) {
	        CheckHelper.checkPageParams(params);
	        ConsumeRecordQueryCondition consumeRecordQueryCondition = CommonConverter.map(params,
	        		ConsumeRecordQueryCondition.class);
	        consumeRecordQueryCondition.setStoreId(ThreadContext.getUserStoreId());
	        PagedList<ConsumeRecordUnion> pagedUnions = consumeRecordService.listPagedConsumeRecords(consumeRecordQueryCondition);
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
		vo.setConsumeRecordDetailUnions(consumeRecordUnion.getConsumeRecordDetailUnion());
		vo.setConsumeRecordGiftUnions(consumeRecordUnion.getConsumeRecordGiftUnion());
		vo.setCustomerName(customerMapper.getById(consumeRecordUnion.getConsumeRecordPo().getCustomerId()).getName());
		return vo;
	}
}
