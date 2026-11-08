package com.gossamercms.events.dtos.responses;

public record EventOrderCheckoutResponse(EventOrderResponse order, String checkoutUrl, String accessToken) {}
