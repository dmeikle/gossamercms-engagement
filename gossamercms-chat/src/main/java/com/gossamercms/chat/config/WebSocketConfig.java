package com.gossamercms.chat.config;


import com.gossamercms.security.services.JwtClaimsMapper;
import com.gossamercms.security.services.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatAuthenticationInterceptor authenticationInterceptor;

    public WebSocketConfig(JwtService jwtService, JwtClaimsMapper jwtClaimsMapper) {
        this.authenticationInterceptor = new ChatAuthenticationInterceptor(jwtService, jwtClaimsMapper);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authenticationInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Destination prefix for messages sent from client -> server
        registry.setApplicationDestinationPrefixes("/app");

        // Enable simple broker for private queues and general topics
        registry.enableSimpleBroker("/queue", "/topic");

        // Prefix used by SimpMessagingTemplate.convertAndSendToUser()
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint for clients to initiate the WebSocket connection
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Fallback option for browsers without native WebSocket
    }
}
