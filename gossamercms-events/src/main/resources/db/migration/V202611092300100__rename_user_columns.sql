alter table event_orders rename column "purchaserUserId" to "purchaserUserContextId";
alter table event_orders
    drop constraint "fk_event_orders_purchaserUserId";
alter table event_orders
    add constraint fk_event_orders_purchaser_user_context_id foreign key ("purchaserUserContextId") references user_contexts ("id");

alter table event_registrations rename column "userId" to "userContextId";
alter table event_registrations
    drop constraint "fk_event_registrations_userId";
alter table event_registrations
    add constraint fk_event_registrations_user_context_id foreign key ("userContextId") references user_contexts ("id");