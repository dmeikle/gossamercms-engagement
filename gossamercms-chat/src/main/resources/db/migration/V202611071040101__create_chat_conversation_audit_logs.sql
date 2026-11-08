CREATE TABLE chat_conversation_audit_logs (
    "id" UUID PRIMARY KEY,
    "chatConversationId" UUID,
    "action" VARCHAR(20),
    "performedBy" UUID,
    "createdBy" UUID,
    "updatedBy" UUID,
    "deletedBy" UUID,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_chat_conversation_audit_logs_chatConversationId" FOREIGN KEY ("chatConversationId") REFERENCES "chat_conversations" ("id"),
    CONSTRAINT "fk_chat_conversation_audit_logs_performedBy" FOREIGN KEY ("performedBy") REFERENCES "user_contexts" ("id")
);
CREATE INDEX "idx_chat_conversation_audit_logs_chatConversationId" ON "chat_conversation_audit_logs" ("chatConversationId");
CREATE INDEX "idx_chat_conversation_audit_logs_performedBy" ON "chat_conversation_audit_logs" ("performedBy");
