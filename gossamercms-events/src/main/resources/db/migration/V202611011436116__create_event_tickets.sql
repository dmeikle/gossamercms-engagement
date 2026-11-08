CREATE TABLE event_tickets (
    "id" UUID PRIMARY KEY,
    "registrationId" UUID,
    "ticketTypeId" UUID,
    "ticketNumber" VARCHAR(100),
    "qrTokenHash" VARCHAR(255),
    "status" VARCHAR,
    "issuedAt" TIMESTAMP,
    "transferredAt" TIMESTAMP,
    "cancelledAt" TIMESTAMP,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_tickets_registrationId" FOREIGN KEY ("registrationId") REFERENCES "event_registrations" ("id"),
    CONSTRAINT "fk_event_tickets_ticketTypeId" FOREIGN KEY ("ticketTypeId") REFERENCES "event_ticket_types" ("id")
);
CREATE INDEX "idx_event_tickets_registrationId" ON "event_tickets" ("registrationId");
CREATE INDEX "idx_event_tickets_ticketTypeId" ON "event_tickets" ("ticketTypeId");
