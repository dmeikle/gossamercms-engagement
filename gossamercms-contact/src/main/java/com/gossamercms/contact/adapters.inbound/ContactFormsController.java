package com.gossamercms.contact.adapters.inbound;


import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.contact.dtos.ContactFormFieldDto;
import com.gossamercms.contact.dtos.requests.ContactFormRequest;
import com.gossamercms.contact.dtos.responses.ContactFormFieldResponse;
import com.gossamercms.contact.dtos.responses.ContactFormResponse;
import com.gossamercms.contact.handlers.ContactFormFieldsHandler;
import com.gossamercms.contact.handlers.ContactFormsHandler;
import com.gossamercms.contact.models.ContactForm;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import gossamercms.globals.config.SystemPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/contact-forms")
public class ContactFormsController extends TypedBaseController<ContactForm, ContactFormDto, ContactFormRequest, ContactFormResponse> {

    private final ContactFormFieldsHandler contactFormFieldsHandler;

    public ContactFormsController(ContactFormsHandler handler, ContactFormFieldsHandler contactFormFieldsHandler) {
        super(handler);
        this.contactFormFieldsHandler = contactFormFieldsHandler;
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
    protected ContactFormResponse toResponse(ContactFormDto dto) {
        return ContactFormResponse.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .allowAttachments(dto.getAllowAttachments())
                .maximumAttachments(dto.getMaximumAttachments())
                .maximumAttachmentSize(dto.getMaximumAttachmentSize())
                .build();
    }


    @GetMapping("/draw/{contactForm}")
    public ApiResponse<ContactFormResponse> getBySlug(@PathVariable("contactForm") ContactFormDto existing) {

        ListResultset<ContactFormFieldDto> fields = contactFormFieldsHandler.getAll(QueryOptions.builder()
                .filters(Map.of("contactFormId", existing.getId()))
                .build());

        ContactFormResponse response = toResponse(existing);
        System.out.println("ContactFormsController.getBySlug: fields.size() = " + fields.list().size());
        response.setFields(fields.list().stream().map(field -> ContactFormFieldResponse.builder()
                .name(field.getName())
                .label(field.getLabel())
                .type(field.getType())
                .placeholder(field.getPlaceholder())
                .helpText(field.getHelpText())
                .defaultValue(field.getDefaultValue())
                .isRequired(field.getIsRequired())
                .isVisible(field.getIsVisible())
                .isActive(field.getIsActive())
                .sortOrder(field.getSortOrder())
                .options(field.getOptions())
                .validation(field.getValidation())
                .build()).toList());

        return ApiResponse.ok(response);
    }


    @GetMapping
    public ApiResponse<ListResultset<ContactFormResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }


}
