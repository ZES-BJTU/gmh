package com.zes.squad.gmh.web.controller;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.vo.AppointmentVo;

@ServerEndpoint("/")
@RestController
public class AppointmentController {
    
    
    @OnMessage
    public JsonResult<AppointmentVo> doRemindReception() {
        return JsonResults.success();
    }

}
