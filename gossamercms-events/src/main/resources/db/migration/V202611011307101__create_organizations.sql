CREATE TABLE organizations (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(255),
    "slug" VARCHAR(255),
    "legalName" VARCHAR(255),
    "description" VARCHAR,
    "email" VARCHAR(255),
    "phone" VARCHAR(50),
    "website" VARCHAR(500),
    "logoUrl" VARCHAR(1000),
    "status" VARCHAR,
    "createdBy" UUID,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_organizations_createdBy" FOREIGN KEY ("createdBy") REFERENCES "users" ("id")
);
CREATE INDEX "idx_organizations_createdBy" ON "organizations" ("createdBy");
