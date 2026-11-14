package com.gossamercms.events.dtos.requests;

import java.util.List;
import java.util.UUID;

public record EventOrderRequest(String name, String email, String currency, List<Item> items) {
    public record Item(UUID eventTicketTypeId, Integer quantity) {}
}
