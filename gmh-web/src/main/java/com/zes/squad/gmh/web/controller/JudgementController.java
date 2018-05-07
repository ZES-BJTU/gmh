package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.service.JudgementService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.vo.JudgementVo;

@RequestMapping(path = "/judgement")
@RestController
public class JudgementController {
	@Autowired
    private JudgementService             judgementService;
	
	@RequestMapping(path = "/update", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateProductType(@RequestBody JudgementVo vo) {
        ensureParameterExist(vo.getUrl(), "链接地址不能为空");
        judgementService.updateUrl(vo.getUrl());
        return JsonResults.success();
    }
}
