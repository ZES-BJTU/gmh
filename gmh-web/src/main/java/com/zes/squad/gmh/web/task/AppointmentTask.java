package com.zes.squad.gmh.web.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.websocket.WebSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppointmentTask {

    @Autowired
    private UserService userService;

    public void remindCustomers() {
        log.info("定时任务开始运行");
        List<UserUnion> onlineUsers = userService.listOnLineUsers();
        if (CollectionUtils.isEmpty(onlineUsers)) {
            log.info("当前无登录用户");
            return;
        }
        log.info("当前登录用户数:{}", onlineUsers.size());
        WebSocketServer webSocketServer = new WebSocketServer();
        for (UserUnion user : onlineUsers) {
            String token = user.getUserTokenPo().getToken();
            String storeName = user.getStorePo().getName();
            webSocketServer.sendMessage(token, storeName);
        }
    }

}
