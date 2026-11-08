CREATE TABLE event_categories (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(100),
    "slug" VARCHAR(100),
    "description" VARCHAR(500),
    "parentId" UUID,
    "sortOrder" INTEGER,
    "isActive" BOOLEAN,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_event_categories_parentId" FOREIGN KEY ("parentId") REFERENCES "event_categories" ("id")
);
CREATE INDEX "idx_event_categories_parentId" ON "event_categories" ("parentId");
