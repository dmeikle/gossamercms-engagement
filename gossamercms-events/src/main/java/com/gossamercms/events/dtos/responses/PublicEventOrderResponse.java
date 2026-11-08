package com.gossamercms.events.dtos.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record PublicEventOrderResponse(UUID eventId, String currency, BigDecimal total, String status) {}
