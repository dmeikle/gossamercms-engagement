package com.gossamercms.events.enums;

public enum EventTicketTypeStatuses {
    SCHEDULED,  // Configured to go on sale at a future date.
    ON_SALE,    // Available for purchase.
    PAUSED,     // Sales temporarily stopped.
    SOLD_OUT,   // No remaining inventory.
    SALES_ENDED, // Sales period has ended.
    CANCELLED,  // Offering has been cancelled.
    ARCHIVED    // Retained for historical purposes.
}