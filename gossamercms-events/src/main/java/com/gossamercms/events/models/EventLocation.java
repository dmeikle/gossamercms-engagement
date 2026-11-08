package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLocation implements BaseModel{
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

    public EventLocationDto toDto() {
        return EventLocationDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_locations")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("name", String.class, 255)
    .column("locationType", String.class)
    .column("address1", String.class, 255)
    .column("address2", String.class, 255)
    .column("city", String.class, 100)
    .column("stateProvince", String.class, 100)
    .column("postalCode", String.class, 20)
    .column("countryCode", String.class, 2)
    .column("latitude", BigDecimal.class)
    .column("longitude", BigDecimal.class)
    .column("onlineUrl", String.class, 1000)
    .column("instructions", String.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
    .column("deletedAt", Instant.class)
    .defaultSort("id desc")
    .build();



    @Override
    public ModelMeta meta() {
        return META;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }

}
