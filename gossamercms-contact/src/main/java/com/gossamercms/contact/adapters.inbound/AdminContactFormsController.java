package com.gossamercms.contact.adapters.inbound;


import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.contact.dtos.requests.ContactFormRequest;
import com.gossamercms.contact.dtos.responses.AdminContactFormListResponse;
import com.gossamercms.contact.dtos.responses.AdminContactFormResponse;
import com.gossamercms.contact.handlers.ContactFormsHandler;
import com.gossamercms.contact.models.ContactForm;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/admin/contact-forms")
public class AdminContactFormsController extends BaseController<ContactForm, ContactFormDto, ContactFormRequest, AdminContactFormResponse, AdminContactFormListResponse> {

    public AdminContactFormsController(ContactFormsHandler handler) {
        super(handler, AdminContactFormListResponse.class);
    }

    @Override
    protected ContactFormDto toDto(ContactFormRequest request) {
        return ContactFormDto.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .status(request.getStatus())
                .successMessage(request.getSuccessMessage())
                .notificationRecipients(request.getNotificationRecipients())
                .sendConfirmationEmail(request.getSendConfirmationEmail())
                .confirmationEmailSubject(request.getConfirmationEmailSubject())
                .confirmationEmailTemplate(request.getConfirmationEmailTemplate())
                .allowAttachments(request.getAllowAttachments())
                .maximumAttachments(request.getMaximumAttachments())
                .maximumAttachmentSize(request.getMaximumAttachmentSize())
                .settings(request.getSettings())
                .build();
    }

    @Override
    protected AdminContactFormResponse toResponse(ContactFormDto dto) {
        return AdminContactFormResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .successMessage(dto.getSuccessMessage())
                .notificationRecipients(dto.getNotificationRecipients())
                .sendConfirmationEmail(dto.getSendConfirmationEmail())
                .confirmationEmailSubject(dto.getConfirmationEmailSubject())
                .confirmationEmailTemplate(dto.getConfirmationEmailTemplate())
                .allowAttachments(dto.getAllowAttachments())
                .maximumAttachments(dto.getMaximumAttachments())
                .maximumAttachmentSize(dto.getMaximumAttachmentSize())
                .settings(dto.getSettings())
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<ListResultset<AdminContactFormListResponse>> getAllList(
            CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(handler.getAll(
                QueryOptions.of(page, size, params, Map.of()),
                AdminContactFormListResponse.class));
    }
}
