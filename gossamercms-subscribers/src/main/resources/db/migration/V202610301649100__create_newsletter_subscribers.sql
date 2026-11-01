CREATE TABLE newsletter_subscribers (
    "id" UUID PRIMARY KEY,
    "email" VARCHAR(255),
    "firstName" VARCHAR(100),
    "lastName" VARCHAR(100),
    "deletedAt" TIMESTAMP WITH TIME ZONE,
    "createdAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    "updatedAt" TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
