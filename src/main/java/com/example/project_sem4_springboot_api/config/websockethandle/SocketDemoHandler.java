package com.example.project_sem4_springboot_api.config.websockethandle;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class SocketDemoHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established on session: {}", session.getId());

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String tutorial =message.getPayload().toString();
        log.info("Message: {}", tutorial);
        session.sendMessage(new TextMessage("Started processing tutorial: " + session + " - " + tutorial));
        Thread.sleep(1000);
        session.sendMessage(new TextMessage("Completed processing tutorial: " + tutorial));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
