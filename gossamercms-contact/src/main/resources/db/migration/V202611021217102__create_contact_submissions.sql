CREATE TABLE contact_submissions (
    "id" UUID PRIMARY KEY,
    "contactFormId" UUID,
    "submitterName" VARCHAR(255),
    "submitterEmail" VARCHAR(255),
    "subject" VARCHAR(255),
    "values" JSONB,
    "status" VARCHAR(50),
    "priority" VARCHAR(50),
    "assignedTo" UUID,
    "sourceUrl" VARCHAR(2000),
    "referrerUrl" VARCHAR(2000),
    "ipAddress" VARCHAR(45),
    "userAgent" VARCHAR(1000),
    "submittedAt" TIMESTAMP,
    "resolvedAt" TIMESTAMP,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_contact_submissions_contactFormId" FOREIGN KEY ("contactFormId") REFERENCES "contact_forms" ("id"),
    CONSTRAINT "fk_contact_submissions_assignedTo" FOREIGN KEY ("assignedTo") REFERENCES "users" ("id")
);
CREATE INDEX "idx_contact_submissions_contactFormId" ON "contact_submissions" ("contactFormId");
CREATE INDEX "idx_contact_submissions_assignedTo" ON "contact_submissions" ("assignedTo");
CREATE INDEX "idx_contact_submissions_status" ON "contact_submissions" ("status");
CREATE INDEX "idx_contact_submissions_submittedAt" ON "contact_submissions" ("submittedAt");
