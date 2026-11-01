CREATE TABLE event_categories (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(100),
    "slug" VARCHAR(100),
    "description" VARCHAR(500),
    "parentId" UUID,
    "sortOrder" INTEGER,
    "isActive" BOOLEAN,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT "fk_event_categories_parentId" FOREIGN KEY ("parentId") REFERENCES "event_categories" ("id")
);
CREATE INDEX "idx_event_categories_parentId" ON "event_categories" ("parentId");
