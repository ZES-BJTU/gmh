package com.zes.squad.gmh.web.task;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.AppointmentService;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.websocket.WebSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppointmentTask {

    @Autowired
    private UserService        userService;
    @Autowired
    private AppointmentService appointmentService;

    public void remindCustomers() {
        log.info("定时任务开始运行");
        List<UserUnion> onlineUsers = userService.listOnLineUsers();
        if (CollectionUtils.isEmpty(onlineUsers)) {
            log.info("当前无登录用户");
            return;
        }
        log.info("当前登录用户数:{}", onlineUsers.size());
        List<AppointmentUnion> unions = appointmentService.getRemindAppointment();
        WebSocketServer webSocketServer = new WebSocketServer();
        for (UserUnion user : onlineUsers) {
            String token = user.getUserTokenPo().getToken();
            List<AppointmentUnion> appointmentUnions = Lists.newArrayList();
            for (AppointmentUnion appointmentUnion : unions) {
                if (appointmentUnion == null || appointmentUnion.getStoreUnion() == null
                        || appointmentUnion.getStoreUnion().getStorePo() == null) {
                    continue;
                }
                Long appointmentStoreId = appointmentUnion.getStoreUnion().getStorePo().getId();
                Long userStoreId = user.getUserPo().getStoreId();
                log.info("appointmentStoreId is {}, userStoreId is {}", appointmentStoreId, userStoreId);
                if (Objects.equals(appointmentStoreId, userStoreId)) {
                    appointmentUnions.add(appointmentUnion);
                }
            }
            String message = JsonUtils.toJson(JsonResults.success(appointmentUnions));
            webSocketServer.sendMessage(token, message);
        }
    }

}
