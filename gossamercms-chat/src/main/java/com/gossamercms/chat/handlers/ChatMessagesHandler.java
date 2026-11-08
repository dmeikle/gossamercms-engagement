package com.gossamercms.chat.handlers;

import com.gossamercms.chat.data.ChatMessagesDbService;
import com.gossamercms.chat.dtos.ChatMessageDto;
import com.gossamercms.chat.models.ChatMessage;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ChatMessagesHandler extends BaseHandler<ChatMessage, ChatMessageDto> {

    public ChatMessagesHandler(ChatMessagesDbService db) {
        super(db);
    }
}
