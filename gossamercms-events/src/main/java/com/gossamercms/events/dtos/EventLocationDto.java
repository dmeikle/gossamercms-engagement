package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventLocation;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLocationDto implements DtoWithId{
    private UUID id;
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
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventLocation toEntity() {
        return EventLocation.builder()
            .id(id)
            .eventId(eventId)
            .name(name)
            .locationType(locationType)
            .address1(address1)
            .address2(address2)
            .city(city)
            .stateProvince(stateProvince)
            .postalCode(postalCode)
            .countryCode(countryCode)
            .latitude(latitude)
            .longitude(longitude)
            .onlineUrl(onlineUrl)
            .instructions(instructions)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
