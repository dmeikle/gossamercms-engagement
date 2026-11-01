package com.gossamercms.subscribers.dtos.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddSubscriberResponse {
    String firstname;
    String lastname;
    String email;
}
