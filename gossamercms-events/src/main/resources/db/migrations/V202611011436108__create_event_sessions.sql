CREATE TABLE event_sessions (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "locationId" UUID,
    "title" VARCHAR(255),
    "description" VARCHAR,
    "startAt" TIMESTAMP WITH TIME ZONE,
    "endAt" TIMESTAMP WITH TIME ZONE,
    "timezone" VARCHAR(100),
    "sortOrder" INTEGER,
    "status" VARCHAR,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_sessions_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_sessions_locationId" FOREIGN KEY ("locationId") REFERENCES "event_locations" ("id")
);
CREATE INDEX "idx_event_sessions_eventId" ON "event_sessions" ("eventId");
CREATE INDEX "idx_event_sessions_locationId" ON "event_sessions" ("locationId");
