CREATE TABLE event_organizer_assignments (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "organizerId" UUID,
    "role" VARCHAR,
    "isPrimary" BOOLEAN,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_organizer_assignments_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_organizer_assignments_organizerId" FOREIGN KEY ("organizerId") REFERENCES "event_organizers" ("id")
);
CREATE INDEX "idx_event_organizer_assignments_eventId" ON "event_organizer_assignments" ("eventId");
CREATE INDEX "idx_event_organizer_assignments_organizerId" ON "event_organizer_assignments" ("organizerId");
