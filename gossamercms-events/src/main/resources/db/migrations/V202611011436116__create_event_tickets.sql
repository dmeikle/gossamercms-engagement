CREATE TABLE event_tickets (
    "id" UUID PRIMARY KEY,
    "registrationId" UUID,
    "ticketTypeId" UUID,
    "ticketNumber" VARCHAR(100),
    "qrTokenHash" VARCHAR(255),
    "status" VARCHAR,
    "issuedAt" TIMESTAMP WITH TIME ZONE,
    "transferredAt" TIMESTAMP WITH TIME ZONE,
    "cancelledAt" TIMESTAMP WITH TIME ZONE,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_tickets_registrationId" FOREIGN KEY ("registrationId") REFERENCES "event_registrations" ("id"),
    CONSTRAINT "fk_event_tickets_ticketTypeId" FOREIGN KEY ("ticketTypeId") REFERENCES "event_ticket_types" ("id")
);
CREATE INDEX "idx_event_tickets_registrationId" ON "event_tickets" ("registrationId");
CREATE INDEX "idx_event_tickets_ticketTypeId" ON "event_tickets" ("ticketTypeId");
