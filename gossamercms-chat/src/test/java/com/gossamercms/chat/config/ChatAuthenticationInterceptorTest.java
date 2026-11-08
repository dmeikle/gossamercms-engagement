package com.gossamercms.chat.config;

import com.gossamercms.security.jwt.JwtUser;
import com.gossamercms.security.services.JwtClaimsMapper;
import com.gossamercms.security.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatAuthenticationInterceptorTest {

    private final String signingKey = UUID.randomUUID().toString() + UUID.randomUUID();
    private final JwtService jwtService = new JwtService(signingKey, 60);
    private final JwtClaimsMapper mapper = new JwtClaimsMapper();
    private final ChatAuthenticationInterceptor interceptor =
            new ChatAuthenticationInterceptor(jwtService, mapper);
    private final UUID userId = UUID.randomUUID();
    private final UUID contextId = UUID.randomUUID();

    @ParameterizedTest
    @EnumSource(value = StompCommand.class, names = {"CONNECT", "STOMP"})
    void connectValidatesTokenAndExposesAuthenticatedJwtUserToSession(StompCommand command) {
        var accessor = StompHeaderAccessor.create(command);
        accessor.setNativeHeader("Authorization", "Bearer " + token(jwtService));
        var sessionUser = new AtomicReference<Principal>();
        accessor.setUserChangeCallback(sessionUser::set);
        Message<?> message = message(accessor);

        assertSame(message, interceptor.preSend(message, null));

        var authentication = assertInstanceOf(
                UsernamePasswordAuthenticationToken.class, sessionUser.get());
        assertTrue(authentication.isAuthenticated());
        assertNull(authentication.getCredentials());
        JwtUser user = ChatAuthenticationInterceptor.requireUser(authentication);
        assertEquals(userId, user.getUserId());
        assertEquals(contextId, user.getUserContextId());
        assertEquals("chat-user", authentication.getName());
        assertTrue(user.hasRole("USER"));
    }

    @ParameterizedTest
    @EnumSource(value = StompCommand.class, names = {"CONNECT", "STOMP"})
    void rejectsConnectWithoutCredentialsOrAuthenticatedHandshake(StompCommand command) {
        var accessor = StompHeaderAccessor.create(command);
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> interceptor.preSend(message(accessor), null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Basic credentials", "Bearer ", "Bearer not-a-jwt"})
    void rejectsMalformedCredentials(String header) {
        var accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization", header);
        assertThrows(BadCredentialsException.class,
                () -> interceptor.preSend(message(accessor), null));
        assertNull(accessor.getUser());
    }

    @Test
    void rejectsDuplicateAuthorizationHeaders() {
        var accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.addNativeHeader("Authorization", "Bearer " + token(jwtService));
        accessor.addNativeHeader("Authorization", "Bearer " + token(jwtService));
        assertThrows(BadCredentialsException.class,
                () -> interceptor.preSend(message(accessor), null));
    }

    @Test
    void rejectsExpiredToken() {
        var accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization",
                "Bearer " + token(new JwtService(signingKey, -600)));
        assertThrows(BadCredentialsException.class,
                () -> interceptor.preSend(message(accessor), null));
        assertNull(accessor.getUser());
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() {
        var accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization", "Bearer " + token(
                new JwtService(UUID.randomUUID().toString() + UUID.randomUUID(), 60)));
        assertThrows(BadCredentialsException.class,
                () -> interceptor.preSend(message(accessor), null));
        assertNull(accessor.getUser());
    }

    @Test
    void acceptsAuthenticatedHttpHandshakeWithoutConnectToken() {
        var accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        var authentication = authentication();
        accessor.setUser(authentication);
        Message<?> message = message(accessor);
        assertSame(message, interceptor.preSend(message, null));
        assertSame(authentication, accessor.getUser());
    }

    @Test
    void invalidConnectTokenDoesNotFallBackToHttpAuthentication() {
        var accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setUser(authentication());
        accessor.setNativeHeader("Authorization", "Bearer invalid");
        assertThrows(BadCredentialsException.class,
                () -> interceptor.preSend(message(accessor), null));
    }

    @Test
    void guardRejectsUnauthenticatedAndNonJwtPrincipals() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ChatAuthenticationInterceptor.requireUser(null));
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ChatAuthenticationInterceptor.requireUser(() -> "chat-user"));
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ChatAuthenticationInterceptor.requireUser(
                        UsernamePasswordAuthenticationToken.unauthenticated(
                                authentication().getPrincipal(), null)));
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ChatAuthenticationInterceptor.requireUser(
                        UsernamePasswordAuthenticationToken.authenticated(
                                "chat-user", null, List.of())));
    }

    @Test
    void permitsAuthenticatedApplicationMessagesAndPrivateSubscriptions() {
        for (StompCommand command : List.of(StompCommand.SEND, StompCommand.SUBSCRIBE)) {
            var accessor = StompHeaderAccessor.create(command);
            accessor.setUser(authentication());
            accessor.setDestination(command == StompCommand.SEND
                    ? "/app/start-conversation" : "/user/queue/messages");
            Message<?> message = message(accessor);
            assertSame(message, interceptor.preSend(message, null));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"support-agent", "support-supervisor"})
    void permitsStaffWithViewAllPermissionToSubscribe(String role) {
        var accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination("/topic/staff-queue");
        accessor.setUser(staffAuthentication(role, List.of("support.chat.view_all")));
        Message<?> message = message(accessor);
        assertSame(message, interceptor.preSend(message, null));
    }

    @Test
    void rejectsStaffQueueSubscriptionsWithoutRoleOrPermission() {
        for (var authentication : List.of(authentication(),
                staffAuthentication("support-agent", List.of()),
                staffAuthentication("USER", List.of("support.chat.view_all")))) {
            var accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
            accessor.setDestination("/topic/staff-queue");
            accessor.setUser(authentication);
            assertThrows(AccessDeniedException.class,
                    () -> interceptor.preSend(message(accessor), null));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"/topic/*", "/topic/**", "/topic/{queue}", "/topic/staff-*",
            "/queue/messages", "/user/another-user/queue/messages"})
    void rejectsWildcardAndDirectBrokerSubscriptions(String destination) {
        var accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination(destination);
        accessor.setUser(staffAuthentication(
                "support-agent", List.of("support.chat.view_all")));
        assertThrows(AccessDeniedException.class,
                () -> interceptor.preSend(message(accessor), null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/topic/staff-queue", "/queue/messages", "/user/queue/messages"})
    void rejectsClientSendsToBrokerDestinations(String destination) {
        var accessor = StompHeaderAccessor.create(StompCommand.SEND);
        accessor.setDestination(destination);
        accessor.setUser(authentication());
        assertThrows(AccessDeniedException.class,
                () -> interceptor.preSend(message(accessor), null));
    }

    @ParameterizedTest
    @EnumSource(value = StompCommand.class, names = {"SEND", "SUBSCRIBE"})
    void rejectsUnauthenticatedSendAndSubscribe(StompCommand command) {
        var accessor = StompHeaderAccessor.create(command);
        accessor.setDestination("/topic/staff-queue");
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> interceptor.preSend(message(accessor), null));
    }

    @Test
    void permitsDisconnectWithoutAuthentication() {
        Message<?> message = message(StompHeaderAccessor.create(StompCommand.DISCONNECT));
        assertSame(message, interceptor.preSend(message, null));
    }

    @Test
    void webSocketConfigurationRegistersAuthenticationInterceptor() {
        var registration = mock(ChannelRegistration.class);
        new WebSocketConfig(jwtService, mapper).configureClientInboundChannel(registration);
        verify(registration).interceptors(any(ChatAuthenticationInterceptor.class));
    }

    private String token(JwtService service) {
        return service.generateToken(userId,
                Map.of("identifier", "chat-user", "userContextId", contextId.toString()),
                List.of("USER"));
    }

    private UsernamePasswordAuthenticationToken authentication() {
        JwtUser user = mapper.fromToken(token(jwtService), jwtService);
        return UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
    }

    private UsernamePasswordAuthenticationToken staffAuthentication(
            String role, List<String> permissions) {
        String token = jwtService.generateToken(userId,
                Map.of("identifier", "chat-staff", "userContextId", contextId.toString(),
                        "permissions", permissions), List.of(role));
        JwtUser user = mapper.fromToken(token, jwtService);
        return UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
    }

    private Message<?> message(StompHeaderAccessor accessor) {
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
