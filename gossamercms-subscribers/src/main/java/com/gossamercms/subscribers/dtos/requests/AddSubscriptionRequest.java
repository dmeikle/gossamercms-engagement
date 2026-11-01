package com.gossamercms.subscribers.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSubscriptionRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String ipAddress;
    private String userAgent;
    private boolean consented;
}
