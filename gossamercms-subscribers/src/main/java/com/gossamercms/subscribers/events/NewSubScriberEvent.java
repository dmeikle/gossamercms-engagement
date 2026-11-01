package com.gossamercms.subscribers.events;

import com.gossamercms.outbox.eventbus.DomainEvent;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewSubScriberEvent implements DomainEvent {
    private NewsletterDto newsletter;
    private NewsletterSubscriptionDto newsletterSubscription;
    private NewsletterSubscriberDto newsletterSubscriber;
}