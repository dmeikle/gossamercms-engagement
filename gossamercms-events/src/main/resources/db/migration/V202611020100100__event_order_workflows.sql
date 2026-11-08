CREATE TABLE event_order_access (
    "orderId" UUID PRIMARY KEY REFERENCES event_orders ("id"),
    "tokenHash" VARCHAR(64) NOT NULL UNIQUE,
    "expiresAt" TIMESTAMP NOT NULL
);

ALTER TABLE event_orders ADD COLUMN "cancelReason" VARCHAR(500);

-- One checkout/payment is supported per order. Legacy duplicates must be reconciled
-- before applying this migration rather than silently selecting an arbitrary payment.
CREATE UNIQUE INDEX "uq_event_payments_orderId" ON event_payments ("orderId");
CREATE UNIQUE INDEX "uq_event_refunds_inflight" ON event_refunds ("paymentId")
    WHERE "status" = 'processing';
