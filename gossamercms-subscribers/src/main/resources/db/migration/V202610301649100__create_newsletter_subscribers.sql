CREATE TABLE newsletter_subscribers (
    "id" UUID PRIMARY KEY,
    "email" VARCHAR(255),
    "firstName" VARCHAR(100),
    "lastName" VARCHAR(100),
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW()
);
