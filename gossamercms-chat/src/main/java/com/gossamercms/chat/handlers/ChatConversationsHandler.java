package com.gossamercms.chat.handlers;

import com.gossamercms.chat.data.ChatConversationsDbService;
import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.chat.models.ChatConversation;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ChatConversationsHandler extends BaseHandler<ChatConversation, ChatConversationDto> {

    public ChatConversationsHandler(ChatConversationsDbService db) {
        super(db);
    }
}
