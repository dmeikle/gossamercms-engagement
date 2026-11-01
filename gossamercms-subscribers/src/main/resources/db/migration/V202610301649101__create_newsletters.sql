CREATE TABLE newsletters (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(150),
    "description" VARCHAR(500),
    "isActive" BOOLEAN,
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
