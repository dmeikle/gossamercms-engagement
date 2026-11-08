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
    "salesStartAt" TIMESTAMP,
    "salesEndAt" TIMESTAMP,
    "status" VARCHAR,
    "sortOrder" INTEGER,
    "isVisible" BOOLEAN,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_ticket_types_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_ticket_types_sessionId" FOREIGN KEY ("sessionId") REFERENCES "event_sessions" ("id")
);
CREATE INDEX "idx_event_ticket_types_eventId" ON "event_ticket_types" ("eventId");
CREATE INDEX "idx_event_ticket_types_sessionId" ON "event_ticket_types" ("sessionId");
