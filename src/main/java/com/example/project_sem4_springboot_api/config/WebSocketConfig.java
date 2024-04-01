package com.example.project_sem4_springboot_api.config;

import com.example.project_sem4_springboot_api.config.websockethandle.HandleTeacher;
import com.example.project_sem4_springboot_api.config.websockethandle.SocketDemoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig  implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // fix client_max_window_bits
        registry.addHandler(webSocketDemoHandler(), "/phu-huynh")
                .addHandler(webSocketHandleTeacher(), "/giao-vien")
                .setAllowedOrigins("*")
        ;

    }

    @Bean
    WebSocketHandler webSocketDemoHandler() {
        return new SocketDemoHandler();
    }
    @Bean
    WebSocketHandler webSocketHandleTeacher() {
        return new HandleTeacher();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mng-school").withSockJS();
    }

}
