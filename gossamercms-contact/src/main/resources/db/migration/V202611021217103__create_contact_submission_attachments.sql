CREATE TABLE contact_submission_attachments (
    "id" UUID PRIMARY KEY,
    "contactSubmissionId" UUID,
    "fileName" VARCHAR(255),
    "contentType" VARCHAR(255),
    "fileSize" BIGINT,
    "storageKey" VARCHAR(1000),
    "uploadedBy" UUID,
    "uploadedAt" TIMESTAMP,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_contact_submission_attachments_contactSubmissionId" FOREIGN KEY ("contactSubmissionId") REFERENCES "contact_submissions" ("id")
);
CREATE INDEX "idx_contact_submission_attachments_contactSubmissionId" ON "contact_submission_attachments" ("contactSubmissionId");

