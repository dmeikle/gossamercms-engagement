package com.gossamercms.chat.config;

import com.gossamercms.security.jwt.JwtUser;
import com.gossamercms.security.services.JwtClaimsMapper;
import com.gossamercms.security.services.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;

import java.security.Principal;
import java.util.List;

public class ChatAuthenticationInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final JwtClaimsMapper jwtClaimsMapper;

    public ChatAuthenticationInterceptor(JwtService jwtService, JwtClaimsMapper jwtClaimsMapper) {
        this.jwtService = jwtService;
        this.jwtClaimsMapper = jwtClaimsMapper;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }
        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            JwtUser user = requireUser(accessor.getUser());
            String destination = accessor.getDestination();
            if ("/topic/staff-queue".equals(destination)) {
                if (!(user.hasRole("support-agent") || user.hasRole("support-supervisor"))
                        || !user.hasPermission("support.chat.view_all")) {
                    throw new AccessDeniedException("Support staff access is required");
                }
            } else if (!"/user/queue/messages".equals(destination)) {
                throw new AccessDeniedException("Chat subscription destination is not allowed");
            }
            return message;
        }
        if (accessor.getCommand() == StompCommand.SEND) {
            requireUser(accessor.getUser());
            String destination = accessor.getDestination();
            if (destination == null || !destination.startsWith("/app/")) {
                throw new AccessDeniedException("Messages must be sent to a chat application handler");
            }
            return message;
        }
        if (accessor.getCommand() != StompCommand.CONNECT
                && accessor.getCommand() != StompCommand.STOMP) {
            return message;
        }

        List<String> authorization = accessor.getNativeHeader("Authorization");
        if (authorization == null) {
            requireUser(accessor.getUser());
            return message;
        }
        if (authorization.size() != 1 || !authorization.getFirst().startsWith("Bearer ")
                || authorization.getFirst().substring(7).isBlank()) {
            throw new BadCredentialsException("A single Bearer token is required for chat");
        }

        JwtUser user;
        try {
            user = jwtClaimsMapper.toUser(
                    jwtService.parseToken(authorization.getFirst().substring(7)).getPayload());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new BadCredentialsException("Invalid or expired chat credentials", exception);
        }

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                user, null, user.getAuthorities());
        requireUser(authentication);
        accessor.setUser(authentication);
        return message;
    }

    public static JwtUser requireUser(Principal principal) {
        if (!(principal instanceof Authentication authentication)
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof JwtUser user)) {
            throw new AuthenticationCredentialsNotFoundException(
                    "An authenticated chat session is required");
        }
        return user;
    }
}
