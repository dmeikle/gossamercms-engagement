CREATE TABLE event_category_assignments (
    "id" UUID PRIMARY KEY,
    "eventId" UUID,
    "categoryId" UUID,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_category_assignments_eventId" FOREIGN KEY ("eventId") REFERENCES "events" ("id"),
    CONSTRAINT "fk_event_category_assignments_categoryId" FOREIGN KEY ("categoryId") REFERENCES "event_categories" ("id")
);
CREATE INDEX "idx_event_category_assignments_eventId" ON "event_category_assignments" ("eventId");
CREATE INDEX "idx_event_category_assignments_categoryId" ON "event_category_assignments" ("categoryId");
