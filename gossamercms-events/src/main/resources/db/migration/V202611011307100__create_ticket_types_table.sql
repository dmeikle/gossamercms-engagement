CREATE TABLE ticket_types (
                               "id" UUID PRIMARY KEY,
                               "name" VARCHAR(255),
                               "description" VARCHAR(255),
                               "createdBy" UUID,
                               "deletedAt" TIMESTAMP,
                               "createdAt" TIMESTAMP DEFAULT NOW(),
                               "updatedAt" TIMESTAMP DEFAULT NOW(),
                               CONSTRAINT "fk_ticket_types_createdBy" FOREIGN KEY ("createdBy") REFERENCES "user_contexts" ("id")
);
