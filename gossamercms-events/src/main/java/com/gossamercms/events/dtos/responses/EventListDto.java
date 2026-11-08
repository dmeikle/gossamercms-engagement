package com.gossamercms.events.dtos.responses;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventListDto {
    private UUID id;
    private String title;
}
