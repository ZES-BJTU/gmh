package com.zes.squad.gmh.task.service.impl;

import javax.websocket.server.ServerEndpoint;

import com.zes.squad.gmh.task.service.AppointmentTaskService;

@ServerEndpoint(value = "/appointment")
public class AppointmentTaskServiceImpl implements AppointmentTaskService {

    @Override
    public void remindCustomer() {
        
    }

    @Override
    public void remindReception() {
        
    }

}
