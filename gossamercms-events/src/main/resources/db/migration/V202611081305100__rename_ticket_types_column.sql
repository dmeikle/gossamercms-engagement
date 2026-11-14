ALTER TABLE "event_ticket_types" DROP COLUMN "ticketType";

ALTER TABLE "event_ticket_types" ADD COLUMN "ticketTypeId" UUID;

ALTER TABLE "event_ticket_types" ADD CONSTRAINT "fk_event_ticket_types_ticketTypeId" FOREIGN KEY ("ticketTypeId") REFERENCES "ticket_types" ("id");