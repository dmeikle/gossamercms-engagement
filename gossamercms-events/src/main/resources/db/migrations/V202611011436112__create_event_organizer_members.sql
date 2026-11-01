CREATE TABLE event_organizer_members (
    "id" UUID PRIMARY KEY,
    "organizerId" UUID,
    "userId" UUID,
    "role" VARCHAR,
    "status" VARCHAR,
    "invitedAt" TIMESTAMP WITH TIME ZONE,
    "joinedAt" TIMESTAMP WITH TIME ZONE,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_organizer_members_organizerId" FOREIGN KEY ("organizerId") REFERENCES "event_organizers" ("id"),
    CONSTRAINT "fk_event_organizer_members_userId" FOREIGN KEY ("userId") REFERENCES "users" ("id")
);
CREATE INDEX "idx_event_organizer_members_organizerId" ON "event_organizer_members" ("organizerId");
CREATE INDEX "idx_event_organizer_members_userId" ON "event_organizer_members" ("userId");
