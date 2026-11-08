CREATE TABLE contact_form_fields (
    "id" UUID PRIMARY KEY,
    "contactFormId" UUID,
    "name" VARCHAR(100),
    "label" VARCHAR(255),
    "type" VARCHAR(50),
    "placeholder" VARCHAR(255),
    "helpText" VARCHAR,
    "defaultValue" VARCHAR,
    "isRequired" BOOLEAN,
    "isVisible" BOOLEAN,
    "isActive" BOOLEAN,
    "sortOrder" INTEGER,
    "options" JSONB,
    "validation" JSONB,
    "deletedAt" TIMESTAMP,
    "createdAt" TIMESTAMP DEFAULT NOW(),
    "updatedAt" TIMESTAMP DEFAULT NOW(),
    CONSTRAINT "fk_contact_form_fields_contactFormId" FOREIGN KEY ("contactFormId") REFERENCES "contact_forms" ("id")
);
CREATE INDEX "idx_contact_form_fields_contactFormId" ON "contact_form_fields" ("contactFormId");
CREATE UNIQUE INDEX "idx_contact_form_fields_contactFormId_name" ON "contact_form_fields" ("contactFormId", "name");