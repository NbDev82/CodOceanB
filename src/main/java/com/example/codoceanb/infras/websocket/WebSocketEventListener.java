package com.example.codoceanb.infras.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger log = LogManager.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebsocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Received a new web socket connection, sessionId: `{}`", headerAccessor.getSessionId());
    }

    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Disconnected a new web socket connection, sessionId: `{}`", headerAccessor.getSessionId());
    }
}
