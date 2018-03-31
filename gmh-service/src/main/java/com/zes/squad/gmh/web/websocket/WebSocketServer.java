package com.zes.squad.gmh.web.websocket;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.socket.server.standard.SpringConfigurator;

@ServerEndpoint(value = "/appointment/remind/{token}", configurator = SpringConfigurator.class)
public class WebSocketServer {

    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
        
    }

}
