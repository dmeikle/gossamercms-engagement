CREATE TABLE contact_forms (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(255),
    "slug" VARCHAR(255),
    "description" VARCHAR,
    "status" VARCHAR(50),
    "successMessage" VARCHAR,
    "notificationRecipients" JSONB,
    "sendConfirmationEmail" BOOLEAN,
    "confirmationEmailSubject" VARCHAR(255),
    "confirmationEmailTemplate" VARCHAR,
    "allowAttachments" BOOLEAN,
    "maximumAttachments" INTEGER,
    "maximumAttachmentSize" INTEGER,
    "settings" JSONB,
    "createdBy" UUID,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW()

);
CREATE INDEX "idx_contact_forms_slug" ON "contact_forms" ("slug");
