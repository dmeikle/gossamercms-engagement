CREATE TABLE newsletters (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(150),
    "description" VARCHAR(500),
    "isActive" BOOLEAN,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW()
);
