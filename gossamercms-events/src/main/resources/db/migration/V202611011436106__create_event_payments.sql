CREATE TABLE event_payments (
    "id" UUID PRIMARY KEY,
    "orderId" UUID,
    "provider" VARCHAR,
    "providerPaymentId" VARCHAR(255),
    "providerCheckoutId" VARCHAR(255),
    "amount" DECIMAL,
    "currency" VARCHAR(3),
    "status" VARCHAR,
    "processedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_payments_orderId" FOREIGN KEY ("orderId") REFERENCES "event_orders" ("id")
);
CREATE INDEX "idx_event_payments_orderId" ON "event_payments" ("orderId");
