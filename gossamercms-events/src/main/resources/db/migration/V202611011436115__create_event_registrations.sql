CREATE TABLE event_registrations (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "orderId" UUID,
    "orderItemId" UUID,
    "userContextId" UUID,
    "attendeeFirstName" VARCHAR(100),
    "attendeeLastName" VARCHAR(100),
    "attendeeEmail" VARCHAR(255),
    "status" VARCHAR,
    "registeredAt" TIMESTAMP,
    "cancelledAt" TIMESTAMP,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_registrations_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_registrations_orderId" FOREIGN KEY ("orderId") REFERENCES "event_orders" ("id"),
    CONSTRAINT "fk_event_registrations_orderItemId" FOREIGN KEY ("orderItemId") REFERENCES "event_order_items" ("id"),
    CONSTRAINT "fk_event_registrations_userContextId" FOREIGN KEY ("userContextId") REFERENCES "user_contexts" ("id")
);
CREATE INDEX "idx_event_registrations_eventId" ON "event_registrations" ("eventId");
CREATE INDEX "idx_event_registrations_orderId" ON "event_registrations" ("orderId");
CREATE INDEX "idx_event_registrations_orderItemId" ON "event_registrations" ("orderItemId");
CREATE INDEX "idx_event_registrations_userContextId" ON "event_registrations" ("userContextId");
