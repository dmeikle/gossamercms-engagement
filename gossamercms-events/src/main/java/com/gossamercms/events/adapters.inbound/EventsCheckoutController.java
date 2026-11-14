package com.gossamercms.events.adapters.inbound;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("events/checkout")
public class EventsCheckoutController {

    @GetMapping("/success")
    public String success() {
        return "Checkout successful!";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "Checkout canceled.";
    }
}
