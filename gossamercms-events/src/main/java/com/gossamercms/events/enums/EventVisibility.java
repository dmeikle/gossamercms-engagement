package com.gossamercms.events.enums;

public enum EventVisibility {
    PUBLIC,      // Anyone can find and view the event (Example: Public music festival)
    UNLISTED,    // Anyone with the direct link can view it, but it doesn't appear in public discovery (Example: Private invitation link)
    PRIVATE,     // Only authorized users can view it (Example: Internal company event)
    INVITE_ONLY  // Only invited or specifically authorized users can access it (Example: Exclusive conference)
}
