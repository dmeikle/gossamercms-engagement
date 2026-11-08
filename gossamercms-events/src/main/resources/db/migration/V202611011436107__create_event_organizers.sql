CREATE TABLE event_organizers (
    "id" UUID PRIMARY KEY,
    "organizationId" UUID,
    "name" VARCHAR(255),
    "slug" VARCHAR(255),
    "description" VARCHAR,
    "email" VARCHAR(255),
    "phone" VARCHAR(50),
    "website" VARCHAR(500),
    "logoUrl" VARCHAR(1000),
    "status" VARCHAR,
    "stripeAccountId" VARCHAR(255),
    "stripeChargesEnabled" BOOLEAN,
    "stripePayoutsEnabled" BOOLEAN,
    "stripeDetailsSubmitted" BOOLEAN,
    "createdBy" UUID,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_organizers_organizationId" FOREIGN KEY ("organizationId") REFERENCES "organizations" ("id"),
    CONSTRAINT "fk_event_organizers_createdBy" FOREIGN KEY ("createdBy") REFERENCES "users" ("id")
);
CREATE INDEX "idx_event_organizers_organizationId" ON "event_organizers" ("organizationId");
CREATE INDEX "idx_event_organizers_createdBy" ON "event_organizers" ("createdBy");
