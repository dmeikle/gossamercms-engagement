# gossamercms-engagement

## Newsletter admin API

The `/admin/newsletters`, `/admin/newsletter-subscribers`, and
`/admin/newsletter-subscriptions` controllers inherit CRUD routes from
`BaseController` and use explicit request/response DTOs. Requests omit record IDs
and audit timestamps; responses include IDs but omit audit timestamps.
Newsletter requests expose `name`, `description`, and `isActive`; subscriber
requests expose `email`, `firstName`, and `lastName`. Subscription requests expose
subscriber/newsletter IDs, status, consent information, subscription timestamps,
confirmation-token fields, IP address, and user agent.
The public subscription controller uses the same request/response mappings with
`TypedBaseController`; its subscribe, confirm, unsubscribe, and my-subscriptions
routes retain their existing payloads and behavior. Public newsletter routes are
unchanged.

## Contact API

Contact admin controllers retain `BaseController`; public contact controllers use
`TypedBaseController` with explicit mappings for every previously inherited route.
Both expose list, get-by-ID, `/get`, create, update-by-ID, `/update`, delete-by-ID,
filtered delete, `/{id}/restore`, and `/bulk` at their existing controller paths.
No contact endpoints are added or removed.

Contact forms, form fields, submissions, and attachments use dedicated
request/response contracts. Requests omit record IDs and audit fields; responses
include IDs but omit audit fields. Form `notificationRecipients` remains a JSON
array of strings; settings, field options/validation, and submission values
remain JSON objects. Public create and bulk requests continue to use the base
controller's existing authentication behavior.

Public submission creation at `POST /contact-submissions/{contactForm}` accepts a
flat JSON object, for example:

```json
{
  "fullName": "Test Sender",
  "email": "sender@example.com",
  "inquiryType": "general",
  "message": "Additional details"
}
```

The entire object is stored in the submission's JSON `values` column, including
additional keys, nested objects, and arrays. `fullName`, `email`, and `inquiryType`
must be present as strings (otherwise the endpoint returns `400`); they are also
copied to `submitterName`, `submitterEmail`, and `subject`, respectively. The form ID
comes from the path. Other body keys remain form values and do not set submission
metadata such as status or assignment. Admin submission contracts are unchanged.

## Event order API

Order mutations are service workflows, not generic CRUD. No payment SDK is included.

| Method | Path | Access |
| --- | --- | --- |
| POST | `/events/{eventId}/orders` | Authenticated purchaser |
| GET | `/events/{eventId}/orders?page=1&size=50` | Event manager; size 1–100 |
| GET | `/event-orders/{orderId}` | Purchaser or event manager |
| GET | `/event-orders/public/{accessToken}` | Bearer token; limited, non-PII response |
| POST | `/event-orders/{orderId}/cancel` | Purchaser or event manager |
| POST | `/event-orders/{orderId}/refund` | Purchaser or event manager |

Checkout body:
```json
{
  "name": "Buyer",
  "email": "buyer@example.com",
  "currency": "USD",
  "items": [{"ticketTypeId": "00000000-0000-0000-0000-000000000001", "quantity": 2}],
  "successUrl": "https://example.com/success",
  "cancelUrl": "https://example.com/cancel"
}
```

Cancel requires `{"reason":"Changed plans"}`. Refund requires
`{"amount":10.00,"reason":"Partial refund"}` in currency major units.
Checkout returns `order`, `checkoutUrl`, and a one-time returned `accessToken`
inside the normal API response envelope. Order reads include server-priced items;
public reads return only event ID, currency, total, and status. Public tokens have
256 bits of randomness, are stored only as SHA-256 hashes, and expire after 30 days.
Do not log tokens/checkout URLs or place tokens in analytics/referrer data. The host
application must allow anonymous GET access to the distinct public route.

Event managers are the event creator or active organizer members with `owner`,
`admin`, or `manager` roles (`EVENT_MANAGER` is also recognized for legacy records).
Authentication alone never grants access to purchaser
data. Orders are accepted only for published public events within registration
windows, and visible `active`/`on_sale` ticket types within sales windows.
`quantityAvailable` is total capacity (null means unlimited); reserved and sold
quantities consume capacity. Prices/currency/order limits come from ticket types;
client prices, totals, or purchaser IDs are ignored. Discounts, tax, and fees are
currently zero; refunding does not automatically restock sold tickets or revoke
registrations/tickets.

### Payment integration and recovery

Register an `EventPaymentAdapter` Spring bean (a Stripe adapter can implement this
later). Missing/ambiguous checkout adapters or a missing adapter for an existing
payment's provider return **503**, never simulated checkout/refund success.
Use one adapter or mark the default checkout adapter `@Primary`; persisted payments
route back to their provider name. Provider names must be stable and unique.
Adapters must validate redirect destinations against their own allowed origins,
set checkout expiry consistently with the order's 30-minute reservation, and use
the supplied operation UUID as a provider idempotency key. They must return an
actual checkout ID/URL and only confirm cancellation/refund after provider confirmation.

Local reservations and operation claims are transactional and row-locked. External
calls run outside database transactions; operation records are committed first.
`EventOrderService` orchestrates these phases through the existing order, item,
ticket-type, payment, refund, and event DbServices. `EventOrderTransactions` selects
the order model's datasource explicitly and preserves `REQUIRES_NEW` phase boundaries;
individual DbService operations join the phase's connection rather than starting
independent transactions. Order/payment locks and UUID-string ticket lock ordering
remain shared across each atomic reservation or cancellation phase.
An uncertain checkout returns **502** with its order ID and remains
`checkout_unknown` (or `checkout_creating` if the process/database fails), retaining
inventory until reconciliation. Do not blindly repeat checkout after a timeout:
that creates a different order. Cancellation permits only `pending` orders and
idempotent `cancelling` retries; inventory is released only after provider session
cancellation is confirmed. Uncertain refunds remain `refund_processing`; retry
the same amount and reason to reuse the persisted refund operation ID. Different
refunds are blocked until that operation is reconciled. Successful cumulative
refunds cannot exceed the confirmed payment or order total.

The future adapter integration must add verified, idempotent webhooks/reconciliation
to record confirmed `providerPaymentId`, payment/order `paid` states, and move
reserved inventory to sold. It must lock the order, then payment, then ticket types
in ascending UUID string order, and respect `cancelling`/`refund_processing` claims. Expiry and
abandoned checkout cleanup likewise require provider confirmation before releasing
inventory; no automatic expiry worker is included. Crash recovery must reuse the
persisted order/refund UUID, not issue a new provider operation. Until that integration
exists, checkout is explicitly unavailable without a configured adapter and refunds
require an independently confirmed persisted payment.

Apply `V202611020100100__event_order_workflows.sql` after existing event migrations.
It adds private public-token storage, cancellation reasons, and uniqueness guards.
Reconcile any legacy duplicate payments per order before migration.

Validation:
```sh
mvn -pl gossamercms-events -am test '-Dtest=EventOrder*Test,EventOrdersControllerTest' -Dsurefire.failIfNoSpecifiedTests=false
```
Workflow tests use the existing migrations in H2 PostgreSQL mode; H2 does not support
the PostgreSQL partial refund index, so its concurrency protection is tested through
actual row locks. Run migrations against PostgreSQL before deployment.

## Event admin API

All event admin controllers retain `BaseController` CRUD routes and use explicit
request/response mappings. Requests omit record IDs and audit fields; responses
include IDs and business fields but omit audit fields. Existing category,
location, and event request contracts are reused where compatible. Separate
admin contracts preserve administrative fields such as organizer Stripe settings,
event visibility/publication state, order totals, and payment/refund provider IDs
without changing public workflow contracts.

Event publish/cancel actions and organizer event/member actions retain their
existing payloads and behavior. Location reads use the mapped response; location
deletes retain their `204 No Content` response.

The public payment, promo-code, registration, ticket, ticket-type, and waitlist
controllers use `TypedBaseController` with explicit `GET /` list and `GET /{id}`
routes only. Lists accept `page`, `size`, and filter parameters. Their former
inherited create, update, delete, restore, and bulk routes are no longer exposed.
Responses omit audit fields, and public ticket responses also omit `qrTokenHash`.
Other public event controllers and order workflow routes are unchanged.

## Event organizer API

`EventOrganizersController` is a `TypedBaseController` delegating to the inherited
`BaseHandler` operations. Authentication and authorization are handled by the
auth/security modules before requests reach the controller. `{organizer}` path
variables are preloaded by `EventOrganizerConverter`. Responses use
`EventOrganizerResponse` (no Stripe or audit fields).

| Method | Path | Delegates to |
| --- | --- | --- |
| GET | `/event-organizers?page=1&size=50` | `getAll` |
| GET | `/event-organizers/{organizer}` | converter-loaded DTO |
| POST | `/event-organizers` | `create` |
| PUT | `/event-organizers/{organizer}` | supplied fields merged into existing DTO, then `updateById` |
| GET | `/event-organizers/{organizer}/events?page=1&size=50` | `EventOrganizersDbService.getEvents` (`findAllBySql`) |
| GET | `/event-organizers/{organizer}/members?page=1&size=50` | members `getAll` filtered by `organizerId` |
| POST | `/event-organizers/{organizer}/members` | `EventOrganizerMembersHandler.invite` |
| DELETE | `/event-organizers/{organizer}/members/{userId}` | members `delete` by `organizerId` + `userId` |

Admin access uses the inherited `BaseController` mappings under
`/admin/event-organizers` and `/admin/event-organizer-members` (filter members with
`?organizerId=`); organizer events are available via
`/admin/event-organizer-assignments`.

PUT accepts the create body fields (`name`, `slug`, `description`, `email`, `phone`,
`website`); omitted fields and internal fields (status, Stripe, audit) are preserved.

Membership request:
```json
{
  "userId": "00000000-0000-0000-0000-000000000002",
  "role": "manager"
}
```

An invitation persists a member with status `invited`; no email is sent. An existing
membership for the same organizer/user returns 409.

Pagination follows the framework's 1-based convention; the PostgreSQL adapter
treats `page=0` as the first page.

Validation:
```sh
mvn -pl gossamercms-events -am package '-Dtest=EventOrganizersControllerTest,EventOrder*Test,EventOrdersControllerTest' -Dsurefire.failIfNoSpecifiedTests=false
```

## Recommended engagement modules



1. Newsletters & Subscribers
   Already started
- Newsletter definitions
- Subscriber management
- Subscription preferences
- Consent management
- Email campaigns and distribution



2. Events & Calendar
   Planned
- Event creation and publishing
- Calendar views
- Event categories
- Event registration
- Attendance management
- Event reminders



3. Contact & Inquiries
- Contact forms
- General inquiries
- Sales and service requests
- Inquiry categorization
- Assignment and status tracking
- Automated acknowledgement emails



4. Comments & Discussions
- Article comments
- Discussion threads
- Replies
- Moderation
- Spam prevention
- Reporting inappropriate content



5. Surveys & Feedback
- Customer satisfaction surveys
- General feedback forms
- Post-event surveys
- Questionnaires
- Ratings
- Response reporting



6. Polls & Voting
- Single and multiple-choice polls
- Public voting
- Voting deadlines
- Results visualization
- Vote restrictions


7. Membership & Loyalty
- Membership registration
- Membership levels
- Points and rewards
- Member benefits
- Renewal reminders
- Member-only content



8. Live Chat & Messaging
- Website chat widget
- Visitor inquiries
- Agent conversations
- Conversation history
- Automated responses
- Offline messaging



9. Testimonials & Reviews
- Customer testimonials
- Product and service reviews
- Star ratings
- Review moderation
- Featured testimonials
- Public display widgets


10. Announcements & Notifications
- Website announcements
- Promotional banners
- Important notices
- Scheduled announcements
- Browser push notifications
- Targeted notifications



## Additional modules worth considering
   These are slightly more specialized, but can add considerable value depending on the type of website.
   Module	Functionality
   Bookings & Reservations	Appointment scheduling, availability, booking confirmations and cancellations
   Contests & Giveaways	Competitions, entries, prize management and winner selection
   Referral Programs	Referral links, tracking, rewards and referral campaigns
   Promotions & Offers	Coupons, special offers, discount codes and promotional campaigns
   Resource Downloads	Downloadable guides, whitepapers, brochures and gated content
   Social Engagement	Social media feeds, sharing links and social interactions
   FAQ & Knowledge Base	Frequently asked questions, searchable articles and visitor self-service
   Visitor Notifications	Browser push notifications and opt-in notification subscriptions
   Customer Communities	User groups, member discussions and community participation
   Webinars & Online Sessions	Online event scheduling, registrations, attendance and follow-up

A few distinctions are worth making:
- Bookings & Reservations should be separate from Events because booking an appointment is a different workflow from registering for an event.
- Promotions & Offers could eventually integrate with your ecommerce module.
- Resource Downloads could integrate with your document management functionality.
- Webinars could initially be implemented as an event type rather than a separate module.
## Engagement modules I'd prioritize
   If I were developing this as a reusable CMS capability, I'd prioritize the following:
   Phase 1 — Core engagement
   Highest priority
   These provide the fundamental ways visitors interact with a website.
1. Newsletter Subscribers
2. Newsletters & Email Campaigns
3. Events & Calendar
4. Event Registrations
5. Contact & Inquiries
6. Surveys & Feedback

Phase 2 — Community engagement
These encourage repeat visits and participation.
1. Comments & Discussions
2. Polls & Voting
3. Testimonials & Reviews
4. Announcements & Notifications
5. Membership & Loyalty

Phase 3 — Advanced engagement
These add more sophisticated marketing and interaction capabilities.
1. Live Chat & Messaging
2. Bookings & Reservations
3. Contests & Giveaways
4. Referral Programs
5. Promotions & Offers
6. Visitor Analytics & Engagement Reporting

============================================
Phase 1
Core engagement infrastructure
- Subscribers
- Newsletters
- Subscriptions
- Email delivery service
- Consent management


Phase 2
Engagement activities
- Events
- Event registrations
- Contact & inquiries
- Surveys & feedback
- Announcements


Phase 3
Campaign management
- Campaign definitions
- Campaign scheduling
- Campaign audiences
- Campaign activities
- Campaign execution
- Campaign reporting


Phase 4
Advanced engagement
- Polls
- Reviews
- Referral programs
- Promotions
- Live chat
- Advanced analytics


2. Design the Campaigns module now



The important architectural decision is that Campaigns should reference existing module records rather than duplicate their data.

For example, a campaign activity might reference:
- A newsletter or email distribution.
- An event.
- An announcement.
- A survey.
  This makes Campaigns an orchestration layer.
3. Initial Campaigns data model
   I'd eventually expect these tables:
   Table	Purpose
   campaigns	Campaign definition and metadata
   campaign_activities	Activities belonging to a campaign
   campaign_audiences	Target audience definitions
   campaign_executions	Individual campaign runs
   campaign_activity_executions	Execution status of individual activities

I'd avoid implementing these until the underlying modules have established their interfaces.
4. One architectural decision I'd make early
   I'd introduce a common concept of a scheduled activity.

For example:
- An email can be scheduled for October 15 at 9 AM.
- An event announcement can be scheduled for October 12.
- A reminder can be scheduled 24 hours before an event.
- A survey can be published after an event finishes.
  You don't necessarily need a universal scheduling database table. You could start with a shared scheduling service and let individual modules own their scheduling data.

This will make Campaigns much easier to implement.
5. What I'd build next
   Given that you've already started the Subscribers module and are planning Events, my recommended immediate sequence is:
1. Finish Subscribers and Subscriptions.
2. Build Newsletters and email distribution.
3. Build Events and Event Registrations.
4. Build Announcements.
5. Build Surveys and Feedback.
6. Build Campaigns.