package com.gossamercms.subscribers.models;

import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscription implements BaseModel{
    private UUID id;
    private UUID subscriberId;
    private UUID newsletterId;
    private String status;
    private String consentType;
    private Instant consentedAt;
    private String consentSource;
    private String consentText;
    private Instant subscribedAt;
    private Instant confirmedAt;
    private Instant unsubscribedAt;
    private String confirmationToken;
    private Instant confirmationTokenExpiresAt;
    private String ipAddress;
    private String userAgent;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public NewsletterSubscriptionDto toDto() {
        return NewsletterSubscriptionDto.builder()
            .id(id)
            .subscriberId(subscriberId)
            .newsletterId(newsletterId)
            .status(status)
            .consentType(consentType)
            .consentedAt(consentedAt)
            .consentSource(consentSource)
            .consentText(consentText)
            .subscribedAt(subscribedAt)
            .confirmedAt(confirmedAt)
            .unsubscribedAt(unsubscribedAt)
            .confirmationToken(confirmationToken)
            .confirmationTokenExpiresAt(confirmationTokenExpiresAt)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("newsletter_subscriptions")
    .column("id", UUID.class)
    .column("subscriberId", UUID.class)
    .column("newsletterId", UUID.class)
    .column("status", String.class, 30)
    .column("consentType", String.class, 20)
    .column("consentedAt", Instant.class)
    .column("consentSource", String.class, 255)
    .column("consentText", String.class)
    .column("subscribedAt", Instant.class)
    .column("confirmedAt", Instant.class)
    .column("unsubscribedAt", Instant.class)
    .column("confirmationToken", String.class, 255)
    .column("confirmationTokenExpiresAt", Instant.class)
    .column("ipAddress", String.class, 45)
    .column("userAgent", String.class)
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
