CREATE TABLE event_refunds (
    "id" UUID PRIMARY KEY,
    "paymentId" UUID,
    "providerRefundId" VARCHAR(255),
    "amount" DECIMAL,
    "currency" VARCHAR(3),
    "reason" VARCHAR(500),
    "status" VARCHAR,
    "refundedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_refunds_paymentId" FOREIGN KEY ("paymentId") REFERENCES "event_payments" ("id")
);
CREATE INDEX "idx_event_refunds_paymentId" ON "event_refunds" ("paymentId");
