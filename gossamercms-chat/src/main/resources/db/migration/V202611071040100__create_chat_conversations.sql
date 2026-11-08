CREATE TABLE chat_conversations (
    "id" UUID PRIMARY KEY,
    "senderContextId" UUID,
    "assignedStaffContextId" UUID,
    "topic" VARCHAR(20),
    "status" VARCHAR(20),
    "createdBy" UUID,
    "updatedBy" UUID,
    "deletedBy" UUID,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_chat_conversations_senderContextId" FOREIGN KEY ("senderContextId") REFERENCES "user_contexts" ("id"),
    CONSTRAINT "fk_chat_conversations_assignedStaffContextId" FOREIGN KEY ("assignedStaffContextId") REFERENCES "user_contexts" ("id")
);
CREATE INDEX "idx_chat_conversations_senderContextId" ON "chat_conversations" ("senderContextId");
CREATE INDEX "idx_chat_conversations_assignedStaffContextId" ON "chat_conversations" ("assignedStaffContextId");
