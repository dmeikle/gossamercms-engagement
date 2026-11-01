package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.handlers.NewslettersHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/newsletters")
public class NewslettersController {
    private final NewslettersHandler handler;

    public NewslettersController(NewslettersHandler handler) {
        this.handler = handler;
    }

    @GetMapping
    public ApiResponse<ListResultset<NewsletterDto>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page",defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        params.remove("page");
        params.remove("size");
        QueryOptions options = QueryOptions.of(page, size, params, Map.of());

        return ApiResponse.ok(this.handler.getAll(options));
    }


    @GetMapping({"/{newsletter}"})
    public ApiResponse<NewsletterDto> getById(@PathVariable("newsletter") NewsletterDto newsletter) {
        return ApiResponse.ok(newsletter);
    }

}
