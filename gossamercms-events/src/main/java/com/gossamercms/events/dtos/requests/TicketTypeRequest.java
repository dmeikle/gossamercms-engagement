package com.gossamercms.events.dtos.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeRequest {
    private String name;
    private String description;
}
