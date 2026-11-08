package com.gossamercms.chat.adapters.inbound;


import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.chat.dtos.requests.ChatConversationRequest;
import com.gossamercms.chat.dtos.responses.ChatConversationListResponse;
import com.gossamercms.chat.dtos.responses.ChatConversationResponse;
import com.gossamercms.chat.enums.ChatConversationStatus;
import com.gossamercms.chat.handlers.ChatConversationsHandler;
import com.gossamercms.chat.models.ChatConversation;
import com.gossamercms.chat.services.ChatService;
import com.gossamercms.chat.config.ChatAuthenticationInterceptor;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryFilter;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat-conversations")
public class ChatConversationsController extends BaseController<ChatConversation, ChatConversationDto, ChatConversationRequest,  ChatConversationResponse, ChatConversationListResponse> {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatConversationsController(ChatConversationsHandler handler,
                                       ChatService chatService,
                                       SimpMessagingTemplate messagingTemplate) {
        super(handler, ChatConversationListResponse.class);
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }
    @Override
        protected ChatConversationDto toDto(ChatConversationRequest request) {
            return ChatConversationDto.builder()
                        .id(request.getChatConversationId())
                        .senderContextId(request.getSenderContextId())
                        .assignedStaffContextId(request.getAssignedStaffContextId())
                        .topic(request.getTopic())
                        .status(request.getStatus())
                        .build();

        }

        @Override
        protected ChatConversationResponse toResponse(ChatConversationDto dto) {
            return ChatConversationResponse.builder()
                           .chatConversationId(dto.getId())
                           .senderContextId(dto.getSenderContextId())
                           .assignedStaffContextId(dto.getAssignedStaffContextId())
                           .topic(dto.getTopic())
                           .status(dto.getStatus())
                           .createdAt(dto.getCreatedAt())
                           .build();

        }

    @MessageMapping("/start-conversation")
    public void processMessage(Principal principal,
                               @Payload ChatConversationRequest request) {
        JwtUser jwtUser = ChatAuthenticationInterceptor.requireUser(principal);

        ChatConversationResponse conversation =
                chatService.startConversation(jwtUser, request);

        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/messages",
                conversation
        );
        messagingTemplate.convertAndSend("/topic/staff-queue", conversation);
    }


    @GetMapping("/list-conversations")
    public ApiResponse<ListResultset<ChatConversationDto>> listConversations(
            @CurrentUser JwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        ListResultset<ChatConversationDto> conversations = handler.getAll(QueryOptions.builder()
                .filters(Map.of("senderContextId", jwtUser.getUserContextId()))
                        .queryFilters(List.of(
                                new QueryFilter("status", QueryFilter.Operator.NOT_EQUALS, ChatConversationStatus.CLOSED)

                                ))
                        .orderBy(Map.of("createdAt", "desc"))
                        .page(page)
                        .size(size)
                .build());

        return ApiResponse.ok(conversations);
    }
}
