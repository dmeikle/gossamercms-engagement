package com.gossamercms.events.dtos.requests;

import java.math.BigDecimal;

public record EventOrderRefundRequest(BigDecimal amount, String reason) {}
