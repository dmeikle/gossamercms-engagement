package com.gossamercms.chat.adapters.inbound;


import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.chat.dtos.ChatMessageDto;
import com.gossamercms.chat.dtos.requests.ChatMessageRequest;
import com.gossamercms.chat.dtos.responses.ChatMessageListResponse;
import com.gossamercms.chat.dtos.responses.ChatMessageResponse;
import com.gossamercms.chat.handlers.ChatMessagesHandler;
import com.gossamercms.chat.models.ChatMessage;
import com.gossamercms.chat.services.ChatService;
import com.gossamercms.chat.config.ChatAuthenticationInterceptor;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.gossamercms.chat.data.ChatConversationsDbService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/chat-messages")
public class ChatMessagesController extends TypedBaseController<ChatMessage, ChatMessageDto, ChatMessageRequest,  ChatMessageResponse, ChatMessageResponse> {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatConversationsDbService chatConversationsDbService;

    public ChatMessagesController(ChatMessagesHandler handler,
                                  ChatService chatService,
                                  SimpMessagingTemplate messagingTemplate,
                                  ChatConversationsDbService chatConversationsDbService) {
        super(handler, ChatMessageResponse.class);
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.chatConversationsDbService = chatConversationsDbService;
    }
    @Override
        protected ChatMessageDto toDto(ChatMessageRequest request) {
            return ChatMessageDto.builder()
                        .chatConversationId(request.getChatConversationId())
                        .senderContextId(request.getSenderContextId())
                        .recipientContextId(request.getRecipientContextId())
                        .status(request.getStatus())
                        .viewedAt(request.getViewedAt())
                        .build();

        }

        @Override
        protected ChatMessageResponse toResponse(ChatMessageDto dto) {
            return ChatMessageResponse.builder()
                           .chatConversationId(dto.getChatConversationId())
                           .senderContextId(dto.getSenderContextId())
                           .recipientContextId(dto.getRecipientContextId())
                           .status(dto.getStatus())
                           .viewedAt(dto.getViewedAt())
                           .createdAt(dto.getCreatedAt())
                           .build();

        }

    @MessageMapping("/messages/{conversation}")
    public void processMessage(Principal principal,
                               @DestinationVariable("conversation") UUID conversationId,
                               @Payload ChatMessageRequest request) {
        JwtUser jwtUser = ChatAuthenticationInterceptor.requireUser(principal);
        ChatConversationDto conversation =
                chatConversationsDbService.getById(conversationId);
        ChatMessageResponse message = chatService.sendMessage(jwtUser, conversation, request);

        messagingTemplate.convertAndSendToUser(
                conversation.getAssignedStaffContextId().toString(),
                "/queue/messages",
                message
        );
    }

    @GetMapping("/conversation/{conversation}")
    public ApiResponse<ListResultset<ChatMessageListResponse>> getConversationMessages(
            @CurrentUser JwtUser jwtUser,
            @PathVariable("conversation") ChatConversationDto conversation,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

//        if (jwtUser == null || !jwtUser.isAuthenticated()) {
//            throw new  ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED, "Authentication required");
//        }
//        if (page < 1 || size < 1 || size > 200) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Page must be positive; size must be 1–200");
//        }


//        UUID contextId = jwtUser.getUserContextId();
//        boolean owner = contextId != null
//                && contextId.equals(conversation.getSenderContextId());
//        boolean staff = jwtUser.hasRole("support-agent")
//                || jwtUser.hasRole("support-supervisor");
//        boolean assignedStaff = staff
//                && jwtUser.hasPermission("support.chat.view")
//                && contextId != null
//                && contextId.equals(conversation.getAssignedStaffContextId());
//        boolean canViewAll = staff
//                && jwtUser.hasPermission("support.chat.view_all");
//
//        if (!owner && !assignedStaff && !canViewAll) {
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN, "Conversation access denied");
//        }

        return ApiResponse.ok(handler.getAll(
                QueryOptions.of(
                        page,
                        size,
                        Map.of("chatConversationId", conversation.getId()),
                        Map.of("createdAt", "asc", "id", "asc")),
                ChatMessageListResponse.class));
    }
}
