package com.gossamercms.stripe.providers;

import com.gossamercms.events.payments.EventPaymentAdapter;
import com.gossamercms.mvc.annotations.ModuleProvider;
import com.gossamercms.events.services.EventPaymentCompletionService;
import com.gossamercms.stripe.config.StripeMockProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.util.UUID;

@ModuleProvider
@ConditionalOnProperty(name = "gossamercms.stripe.mock.enabled", havingValue = "true")
public class StripeMockPaymentProvider implements EventPaymentAdapter {
    private final StripeMockProperties properties;
    private final EventPaymentCompletionService completion;
    private final Map<String, Session> sessions = new HashMap<>();
    private final Map<UUID, String> operations = new HashMap<>();

    public StripeMockPaymentProvider(StripeMockProperties properties, EventPaymentCompletionService completion) {
        this.properties = properties;
        this.completion = completion;
    }

    public record Session(CheckoutRequest request, String checkoutId, Instant expiresAt) {}

    @Override
    public String provider() {
        return "stripe-mock";
    }

    @Override
    public synchronized Checkout createCheckout(CheckoutRequest request) {
        String base = properties.getBaseUrl();
        if (base == null || !base.matches("https?://[^\\s]+")
                || URI.create(base).getHost() == null || URI.create(base).getRawQuery() != null
                || URI.create(base).getRawFragment() != null || URI.create(base).getUserInfo() != null)
            throw new IllegalStateException("Configure gossamercms.stripe.mock.base-url with the website's absolute base URL");
        String contextPath = properties.getContextPath();
        if (contextPath == null || (!contextPath.isEmpty()
                && !contextPath.matches("/[A-Za-z0-9._~/-]*")))
            throw new IllegalStateException("gossamercms.stripe.mock.context-path must be empty or an absolute URL path");
        base = base.replaceAll("/+$", "") + contextPath;
        String token = operations.get(request.operationId());
        if (token == null) {
            token = UUID.randomUUID().toString() + UUID.randomUUID();
            sessions.put(token, new Session(request, "mock_cs_" + request.operationId(),
                    Instant.now().plus(30, ChronoUnit.MINUTES)));
            operations.put(request.operationId(), token);
        } else if (!sessions.get(token).request().equals(request)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mock checkout operation parameters changed");
        }
        return new Checkout(sessions.get(token).checkoutId(),
                base.replaceAll("/+$", "") + "/mock-stripe/checkout/" + token);
    }

    public synchronized Session session(String token) {
        Session session = sessions.get(token);
        if (session == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mock checkout not found");
        if (!Instant.now().isBefore(session.expiresAt()))
            throw new ResponseStatusException(HttpStatus.GONE, "Mock checkout expired");
        return session;
    }

    public synchronized String finish(String token, boolean cancelled) {
        Session session = session(token);
        var request = session.request();
        completion.complete(request.operationId(), provider(), session.checkoutId(),
                cancelled ? null : "mock_pi_" + request.operationId(), request.total(), request.currency(), cancelled);
        return cancelled ? request.cancelUrl() : request.successUrl();
    }

    @Override
    public synchronized boolean cancelCheckout(UUID operationId, String checkoutId) {
        String token = operations.get(operationId);
        if (token == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mock checkout not found");
        Session session = sessions.get(token);
        if (!session.checkoutId().equals(checkoutId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mock checkout ID mismatch");
        return true;
    }

    @Override
    public Refund refund(UUID operationId, String paymentId, BigDecimal amount, String currency, String reason) {
        if (paymentId == null || !paymentId.startsWith("mock_pi_"))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not a mock payment");
        return new Refund("mock_re_" + operationId, true);
    }
}
