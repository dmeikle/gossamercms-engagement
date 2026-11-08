package com.gossamercms.events.dtos.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record EventOrderRefundResponse(UUID id, BigDecimal amount, String currency, String status) {}
