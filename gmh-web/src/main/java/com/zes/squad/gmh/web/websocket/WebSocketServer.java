package com.zes.squad.gmh.web.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint(value = "/appointment/remind/{token}", configurator = SpringConfigurator.class)
public class WebSocketServer {

    private static ConcurrentMap<String, Session> clients = Maps.newConcurrentMap();

    private String                                token;

    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
        log.info("客户端服务端建立websocket连接, token is {}", token);
        this.token = token;
        clients.putIfAbsent(token, session);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("客户端和服务端正常断开websocket连接, token is {}, reason is {}", token, closeReason.getReasonPhrase());
        clients.remove(this.token);
    }

    @OnError
    public void onError(Throwable throwable, Session session) {
        log.info("客户端和服务端异常断开websocket连接, token is {}", this.token, throwable);
        clients.remove(this.token);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端的消息, token is {}, message is {}", this.token, message);
    }

    public void sendMessage(String token, String message) {
        if (clients.containsKey(token)) {
            Session session = clients.get(token);
            if (session != null) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("发送消息到客户端异常, token is {}, message is {}", token, message);
                }
            }
        }
    }

}
