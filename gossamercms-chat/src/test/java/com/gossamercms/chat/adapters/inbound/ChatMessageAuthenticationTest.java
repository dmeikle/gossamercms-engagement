package com.gossamercms.chat.adapters.inbound;

import com.gossamercms.chat.config.ChatAuthenticationInterceptor;
import com.gossamercms.chat.data.ChatConversationsDbService;
import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.chat.dtos.requests.ChatConversationRequest;
import com.gossamercms.chat.dtos.requests.ChatMessageRequest;
import com.gossamercms.chat.dtos.responses.ChatConversationResponse;
import com.gossamercms.chat.handlers.ChatConversationsHandler;
import com.gossamercms.chat.handlers.ChatMessagesHandler;
import com.gossamercms.chat.services.ChatService;
import com.gossamercms.security.jwt.JwtUser;
import com.gossamercms.security.services.JwtClaimsMapper;
import com.gossamercms.security.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.MimeTypeUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ChatMessageAuthenticationTest {

    private final ChatService service = mock(ChatService.class);
    private final SimpMessagingTemplate messaging = mock(SimpMessagingTemplate.class);
    private final ChatConversationsDbService conversations = mock(ChatConversationsDbService.class);
    private final ChatConversationsController startController = new ChatConversationsController(
            mock(ChatConversationsHandler.class), service, messaging);
    private final ChatMessagesController messagesController = new ChatMessagesController(
            mock(ChatMessagesHandler.class), service, messaging, conversations);

    @Test
    void bothHandlersRejectMissingAuthenticationBeforeDatabaseAccess() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> startController.processMessage(null, new ChatConversationRequest()));
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> messagesController.processMessage(
                        null, UUID.randomUUID(), new ChatMessageRequest()));
        verifyNoInteractions(service, conversations, messaging);
    }

    @Test
    void stompDispatchResolvesPrincipalAndOnlyDeserializesRequestPayloads() {
        JwtService jwtService = new JwtService(
                UUID.randomUUID().toString() + UUID.randomUUID(), 60);
        UUID contextId = UUID.randomUUID();
        String token = jwtService.generateToken(UUID.randomUUID(),
                Map.of("identifier", "chat-user", "userContextId", contextId.toString()),
                List.of("USER"));
        var connect = StompHeaderAccessor.create(StompCommand.CONNECT);
        connect.setNativeHeader("Authorization", "Bearer " + token);
        connect.setLeaveMutable(true);
        new ChatAuthenticationInterceptor(jwtService, new JwtClaimsMapper()).preSend(
                MessageBuilder.createMessage(new byte[0], connect.getMessageHeaders()), null);
        var authentication = (UsernamePasswordAuthenticationToken) connect.getUser();
        JwtUser user = ChatAuthenticationInterceptor.requireUser(authentication);
        UUID conversationId = UUID.randomUUID();
        UUID staffId = UUID.randomUUID();
        ChatConversationResponse createdConversation = ChatConversationResponse.builder()
                .chatConversationId(conversationId)
                .build();
        when(service.startConversation(eq(user), any(ChatConversationRequest.class)))
                .thenReturn(createdConversation);
        when(conversations.getById(conversationId))
                .thenReturn(ChatConversationDto.builder()
                        .id(conversationId).assignedStaffContextId(staffId).build());

        try (var context = new StaticApplicationContext()) {
            context.getBeanFactory().registerSingleton("startController", startController);
            context.getBeanFactory().registerSingleton("messagesController", messagesController);
            var inbound = new ExecutorSubscribableChannel();
            var outbound = new ExecutorSubscribableChannel();
            var handler = new SimpAnnotationMethodMessageHandler(
                    inbound, outbound, new SimpMessagingTemplate(outbound));
            handler.setApplicationContext(context);
            handler.setDestinationPrefixes(List.of("/app"));
            handler.setMessageConverter(new JacksonJsonMessageConverter());
            handler.afterPropertiesSet();

            for (String destination : List.of(
                    "/app/start-conversation", "/app/messages/" + conversationId)) {
                var send = StompHeaderAccessor.create(StompCommand.SEND);
                send.setDestination(destination);
                send.setSessionId("chat-test");
                send.setSessionAttributes(Map.of());
                send.setUser(authentication);
                send.setContentType(MimeTypeUtils.APPLICATION_JSON);
                handler.handleMessage(MessageBuilder.createMessage(
                        "{\"messageContent\":\"Hello\"}".getBytes(StandardCharsets.UTF_8),
                        send.getMessageHeaders()));
            }
        }

        verify(service).startConversation(eq(user),
                argThat(request -> "Hello".equals(request.getMessageContent())));
        verify(messaging).convertAndSendToUser(
                authentication.getName(), "/queue/messages", createdConversation);
        verify(messaging).convertAndSend("/topic/staff-queue", createdConversation);
        verify(conversations).getById(conversationId);
        verify(service).sendMessage(eq(user),
                argThat(conversation -> conversationId.equals(conversation.getId())),
                argThat(request -> "Hello".equals(request.getMessageContent())));
    }
}
