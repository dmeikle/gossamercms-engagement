package com.gossamercms.stripe.dtos.requests;

import lombok.Data;

@Data
public class CheckoutRequest {
    private long amount;
    private String currency = "usd";

}