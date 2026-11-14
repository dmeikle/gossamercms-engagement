package com.gossamercms.events.enums;

import lombok.Getter;

@Getter
public enum TicketTypeStatuses {

    ACTIVE,//	Available for organizers to use when creating event ticket offerings.
    INACTIVE,//	Temporarily unavailable for new offerings, but existing offerings remain valid.
    ARCHIVED//	Retained for historical records; no longer available for new use.

}