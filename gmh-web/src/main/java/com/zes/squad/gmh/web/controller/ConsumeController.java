package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ConsumeCreateOrModifyParams;

@RequestMapping(path = "/consume")
@RestController
public class ConsumeController {
	@Autowired
    private ConsumeRecordService consumeRecordService;

	 @RequestMapping(path = "/createCardConsume", method = { RequestMethod.PUT })
	    public JsonResult<Void> doCreateCardConsume(@RequestBody ConsumeCreateOrModifyParams params) {
		 	consumeRecordService.createProductConsumeRecord(params.getConsumeRecordPo(), params.getConsumeRecordProducts());
	        return JsonResults.success();
	    }
}
