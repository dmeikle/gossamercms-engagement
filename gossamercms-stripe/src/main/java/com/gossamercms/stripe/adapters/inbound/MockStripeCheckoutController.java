package com.gossamercms.stripe.adapters.inbound;

import com.gossamercms.stripe.providers.StripeMockPaymentProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.net.URI;

@RestController
@RequestMapping("/mock-stripe/checkout")
@ConditionalOnProperty(name = "gossamercms.stripe.mock.enabled", havingValue = "true")
public class MockStripeCheckoutController {
    private final StripeMockPaymentProvider provider;

    public MockStripeCheckoutController(StripeMockPaymentProvider provider) {
        this.provider = provider;
    }

    @GetMapping(value = "/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> checkout(@PathVariable("token") String token, HttpServletRequest request) {
        StripeMockPaymentProvider.Session session = provider.session(token);
        String csrfInput = "";
        if (request.getAttribute(CsrfToken.class.getName()) instanceof CsrfToken csrf) {
            csrfInput = "<input type=\"hidden\" name=\"" + escape(csrf.getParameterName())
                    + "\" value=\"" + escape(csrf.getToken()) + "\">";
        }
        String html = """
                <!doctype html><html lang="en"><head><meta charset="utf-8">
                <title>Mock payment</title></head><body>
                <h1>Mock payment - no real charge</h1>
                <p>Order: %s</p><p>Total: %s %s</p>
                <form method="post">%s
                <button name="action" value="pay">Simulate successful payment</button>
                <button name="action" value="cancel">Cancel purchase</button>
                </form></body></html>
                """.formatted(session.request().operationId(),
                escape(session.request().total().toPlainString()), escape(session.request().currency()), csrfInput);
        return ResponseEntity.ok().header(HttpHeaders.CACHE_CONTROL, "no-store")
                .header("Referrer-Policy", "same-origin")
                .header("Content-Security-Policy", "default-src 'none'; form-action 'self'; frame-ancestors 'none'; base-uri 'none'")
                .body(html);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Void> finish(@PathVariable("token") String token,
                                       @RequestParam("action") String action) {
        if (!"pay".equals(action) && !"cancel".equals(action))
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Choose pay or cancel");
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .header("Referrer-Policy", "no-referrer")
                .location(URI.create(provider.finish(token, "cancel".equals(action)))).build();
    }

    private static String escape(String value) {
        return HtmlUtils.htmlEscape(value);
    }
}
