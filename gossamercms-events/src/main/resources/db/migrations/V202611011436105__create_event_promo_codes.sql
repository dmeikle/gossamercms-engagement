CREATE TABLE event_promo_codes (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "code" VARCHAR(50),
    "discountType" VARCHAR,
    "discountValue" DECIMAL,
    "currency" VARCHAR(3),
    "maximumRedemptions" INTEGER,
    "redemptionCount" INTEGER,
    "minimumOrderAmount" DECIMAL,
    "validFrom" TIMESTAMP WITH TIME ZONE,
    "validUntil" TIMESTAMP WITH TIME ZONE,
    "isActive" BOOLEAN,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_promo_codes_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id")
);
CREATE INDEX "idx_event_promo_codes_eventId" ON "event_promo_codes" ("eventId");
