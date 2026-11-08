Architecture Considerations for Production
Routing & Queue Assignment:

When a customer initiates a chat, push a request to a /topic/staff-queue topic that available staff members subscribe to.

`/app/start-conversation` creates a waiting conversation without assigning staff.
Its acknowledgement is delivered to the initiating principal's
`/user/queue/messages`; the same conversation is published to `/topic/staff-queue`.
That topic requires the JWT role `support-agent` or `support-supervisor` and the
permission `support.chat.view_all` from `V202611072039100__seed_chat_roles.sql`.
Refresh the JWT after changing database roles or permissions.

Subscriptions are limited to the exact `/topic/staff-queue` destination for
authorized staff and `/user/queue/messages` for authenticated users. Wildcards,
direct broker queues, and other users' private queues are rejected. Clients must
send messages to `/app/` handlers, not directly to `/topic/`, `/queue/`, or `/user/`
destinations. The host application must not broaden these subscription rules.

Once a staff member "accepts" the chat, bind their staffId to the user's conversationId in your database and switch communication to direct private queues.

Persistence (Database):

Save messages to PostgreSQL/MongoDB before calling convertAndSendToUser so users can view historical messages when reopening the chat or reloading the page.

Authentication & Security:

For JWT authentication, send `Authorization: Bearer <token>` as a native STOMP
`CONNECT` header, not as part of the JSON message payload. `WebSocketConfig`
registers an inbound interceptor that validates the token using `JwtService` and
`JwtClaimsMapper`, then attaches an authenticated `Authentication` containing
`JwtUser` to the STOMP session. Missing credentials, malformed headers, and invalid
or expired tokens are rejected during `CONNECT`. Both message handlers retain a
guard that requires this authenticated principal.

For example, with `@stomp/stompjs` and SockJS:

```javascript
const client = new Client({
    webSocketFactory: () => new SockJS("/ws-chat"),
    beforeConnect: async () => {
        client.connectHeaders = {
            Authorization: `Bearer ${await getAccessToken()}`
        };
    }
});
client.activate();
```

`Client` and `SockJS` come from the client's STOMP and SockJS libraries;
`getAccessToken()` must use the application's existing login/token refresh flow.
Supply fresh credentials on each reconnect. Browser SockJS clients cannot set
arbitrary HTTP handshake headers; the host application's HTTP security rules must
allow the `/ws-chat` transport endpoints to be reached for STOMP authentication.
This does not make application messages anonymous: unauthenticated `CONNECT`
frames are rejected.

Alternatively, when using an HTTP session, the handshake and SockJS transport
requests must carry that session's cookie. The handshake must expose an
authenticated `Authentication` with a `JwtUser` principal; that principal is
accepted when no STOMP `Authorization` header is supplied. An invalid supplied
token is always rejected, even if the handshake was authenticated.

Horizontal Scaling (Multiple Spring Instances):

The default enableSimpleBroker stores connections in-memory. If you run multiple instances of your Spring Boot app behind a load balancer, use a dedicated external STOMP message broker like RabbitMQ or ActiveMQ to relay messages across instances.