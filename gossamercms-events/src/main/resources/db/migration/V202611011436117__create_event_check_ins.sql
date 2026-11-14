CREATE TABLE event_check_ins (
    "id" UUID PRIMARY KEY,
    "ticketId" UUID,
    "sessionId" UUID,
    "checkedInBy" UUID,
    "checkedInAt" TIMESTAMP,
    "method" VARCHAR,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_check_ins_ticketId" FOREIGN KEY ("ticketId") REFERENCES "event_tickets" ("id"),
    CONSTRAINT "fk_event_check_ins_sessionId" FOREIGN KEY ("sessionId") REFERENCES "event_sessions" ("id"),
    CONSTRAINT "fk_event_check_ins_checkedInBy" FOREIGN KEY ("checkedInBy") REFERENCES "user_contexts" ("id")
);
CREATE INDEX "idx_event_check_ins_ticketId" ON "event_check_ins" ("ticketId");
CREATE INDEX "idx_event_check_ins_sessionId" ON "event_check_ins" ("sessionId");
CREATE INDEX "idx_event_check_ins_checkedInBy" ON "event_check_ins" ("checkedInBy");
