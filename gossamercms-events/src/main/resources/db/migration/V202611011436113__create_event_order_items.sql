CREATE TABLE event_order_items (
    "id" UUID PRIMARY KEY,
    "orderId" UUID,
    "ticketTypeId" UUID,
    "quantity" INTEGER,
    "unitPrice" DECIMAL,
    "lineTotal" DECIMAL,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_order_items_orderId" FOREIGN KEY ("orderId") REFERENCES "event_orders" ("id"),
    CONSTRAINT "fk_event_order_items_ticketTypeId" FOREIGN KEY ("ticketTypeId") REFERENCES "event_ticket_types" ("id")
);
CREATE INDEX "idx_event_order_items_orderId" ON "event_order_items" ("orderId");
CREATE INDEX "idx_event_order_items_ticketTypeId" ON "event_order_items" ("ticketTypeId");
