CREATE TABLE event_locations (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "name" VARCHAR(255),
    "locationType" VARCHAR,
    "address1" VARCHAR(255),
    "address2" VARCHAR(255),
    "city" VARCHAR(100),
    "stateProvince" VARCHAR(100),
    "postalCode" VARCHAR(20),
    "countryCode" VARCHAR(2),
    "latitude" DECIMAL,
    "longitude" DECIMAL,
    "onlineUrl" VARCHAR(1000),
    "instructions" VARCHAR,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_locations_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id")
);
CREATE INDEX "idx_event_locations_eventId" ON "event_locations" ("eventId");
