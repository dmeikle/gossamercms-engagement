package com.gossamercms.events.payments;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PaymentAdapterUnavailableException extends ResponseStatusException {
    public PaymentAdapterUnavailableException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "Event payment adapter is not configured");
    }
}
