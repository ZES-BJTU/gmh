package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;
import com.zes.squad.gmh.service.MessageService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.message.MessageParams;

@RequestMapping(path = "/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/sendAuthCode", method = { RequestMethod.GET })
    public JsonResult<Void> doSendAuthCode(@RequestBody MessageParams params) {
        ensureParameterExist(params, "短信参数为空");
        ensureParameterExist(params.getMobile(), "手机号为空");
        messageService.sendAuthCode(params.getMobile());
        return JsonResults.success();
    }

    @RequestMapping(path = "/validateAuthCode", method = { RequestMethod.POST })
    public JsonResult<Void> doValidateAuthCode(@RequestBody MessageParams params) {
        ensureParameterExist(params, "短信参数为空");
        ensureParameterExist(params.getMobile(), "手机号为空");
        ensureParameterExist(params.getAuthCode(), "验证码为空");
        messageService.validateAuthCode(params.getMobile(), params.getAuthCode());
        return JsonResults.success();
    }

}
