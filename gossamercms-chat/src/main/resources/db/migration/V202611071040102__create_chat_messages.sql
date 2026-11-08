CREATE TABLE chat_messages (
    "id" UUID PRIMARY KEY,
    "chatConversationId" UUID,
    "senderContextId" UUID,
    "recipientContextId" UUID,
    "status" VARCHAR(20),
    "messageContent" VARCHAR(1000),
    "viewedAt" TIMESTAMP,
    "createdBy" UUID,
    "updatedBy" UUID,
    "deletedBy" UUID,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP  DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_chat_messages_chatConversationId" FOREIGN KEY ("chatConversationId") REFERENCES "chat_conversations" ("id"),
    CONSTRAINT "fk_chat_messages_senderContextId" FOREIGN KEY ("senderContextId") REFERENCES "user_contexts" ("id"),
    CONSTRAINT "fk_chat_messages_recipientContextId" FOREIGN KEY ("recipientContextId") REFERENCES "user_contexts" ("id")
);
CREATE INDEX "idx_chat_messages_chatConversationId" ON "chat_messages" ("chatConversationId");
CREATE INDEX "idx_chat_messages_senderContextId" ON "chat_messages" ("senderContextId");
CREATE INDEX "idx_chat_messages_recipientContextId" ON "chat_messages" ("recipientContextId");
