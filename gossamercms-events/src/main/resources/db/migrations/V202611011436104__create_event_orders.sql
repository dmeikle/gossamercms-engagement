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
    "expiresAt" TIMESTAMP WITH TIME ZONE,
    "paidAt" TIMESTAMP WITH TIME ZONE,
    "cancelledAt" TIMESTAMP WITH TIME ZONE,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_orders_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_orders_purchaserUserId" FOREIGN KEY ("purchaserUserId") REFERENCES "users" ("id")
);
CREATE INDEX "idx_event_orders_eventId" ON "event_orders" ("eventId");
CREATE INDEX "idx_event_orders_purchaserUserId" ON "event_orders" ("purchaserUserId");
