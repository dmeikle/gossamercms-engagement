package com.gossamercms.subscribers.adapters.inbound;

import com.gossamercms.subscribers.handlers.NewsletterSubscribersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newsletter-subscribers")
@RequiredArgsConstructor
public class NewsletterSubscribersController {
    private final NewsletterSubscribersHandler handler;

//    @PostMapping("subscribe")
//    public ApiResponse subscribe(@RequestBody AddSubscriberRequest request) {
//          NewsletterSubscriberDto dto = handler.create(SystemPrincipal.ID, NewsletterSubscriberDto.builder()
//                  .email(request.getEmail())
//                  .firstName(request.getFirstName())
//                  .lastName(request.getLastName())
//                  .build());
//
//          return ApiResponse.ok(
//                  AddSubscriberResponse.builder()
//                          .email(dto.getEmail())
//                          .firstname(dto.getFirstName())
//                          .lastname(dto.getLastName())
//                          .build()
//          );
//    }
}
