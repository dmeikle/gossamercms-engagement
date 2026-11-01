CREATE TABLE event_waitlists (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "ticketTypeId" UUID,
    "firstName" VARCHAR(100),
    "lastName" VARCHAR(100),
    "email" VARCHAR(255),
    "quantityRequested" INTEGER,
    "status" VARCHAR,
    "position" INTEGER,
    "offeredAt" TIMESTAMP WITH TIME ZONE,
    "expiresAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_waitlists_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_waitlists_ticketTypeId" FOREIGN KEY ("ticketTypeId") REFERENCES "event_ticket_types" ("id")
);
CREATE INDEX "idx_event_waitlists_eventId" ON "event_waitlists" ("eventId");
CREATE INDEX "idx_event_waitlists_ticketTypeId" ON "event_waitlists" ("ticketTypeId");
