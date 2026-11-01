CREATE TABLE event_ticket_types (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "sessionId" UUID,
    "name" VARCHAR(255),
    "description" VARCHAR,
    "ticketType" VARCHAR,
    "price" DECIMAL,
    "currency" VARCHAR(3),
    "quantityAvailable" INTEGER,
    "quantityReserved" INTEGER,
    "quantitySold" INTEGER,
    "minimumPerOrder" INTEGER,
    "maximumPerOrder" INTEGER,
    "salesStartAt" TIMESTAMP WITH TIME ZONE,
    "salesEndAt" TIMESTAMP WITH TIME ZONE,
    "status" VARCHAR,
    "sortOrder" INTEGER,
    "isVisible" BOOLEAN,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_ticket_types_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_ticket_types_sessionId" FOREIGN KEY ("sessionId") REFERENCES "event_sessions" ("id")
);
CREATE INDEX "idx_event_ticket_types_eventId" ON "event_ticket_types" ("eventId");
CREATE INDEX "idx_event_ticket_types_sessionId" ON "event_ticket_types" ("sessionId");
