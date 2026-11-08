package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLocationRequest {
    private UUID eventId;
    private String name;
    private String locationType;
    private String address1;
    private String address2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String countryCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String onlineUrl;
    private String instructions;
}
