CREATE TABLE event_orders (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "orderNumber" VARCHAR(50),
    "purchaserUserId" UUID,
    "purchaserName" VARCHAR(255),
    "purchaserEmail" VARCHAR(255),
    "currency" VARCHAR(3),
    "subtotal" DECIMAL,
    "discountTotal" DECIMAL,
    "taxTotal" DECIMAL,
    "serviceFee" DECIMAL,
    "total" DECIMAL,
    "status" VARCHAR,
    "expiresAt" TIMESTAMP,
    "paidAt" TIMESTAMP,
    "cancelledAt" TIMESTAMP,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_orders_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_orders_purchaserUserId" FOREIGN KEY ("purchaserUserId") REFERENCES "users" ("id")
);
CREATE INDEX "idx_event_orders_eventId" ON "event_orders" ("eventId");
CREATE INDEX "idx_event_orders_purchaserUserId" ON "event_orders" ("purchaserUserId");
