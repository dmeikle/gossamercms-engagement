package com.gossamercms.contact.adapters.inbound;


import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.contact.dtos.ContactFormFieldDto;
import com.gossamercms.contact.dtos.requests.ContactFormFieldRequest;
import com.gossamercms.contact.dtos.responses.ContactFormFieldResponse;
import com.gossamercms.contact.handlers.ContactFormFieldsHandler;
import com.gossamercms.contact.models.ContactFormField;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/contact-form-fields")
public class AdminContactFormFieldsController extends BaseController<ContactFormField, ContactFormFieldDto, ContactFormFieldRequest, ContactFormFieldResponse, ContactFormFieldResponse> {

    public AdminContactFormFieldsController(ContactFormFieldsHandler handler) {
        super(handler, ContactFormFieldResponse.class);
    }

    @Override
    protected ContactFormFieldDto toDto(ContactFormFieldRequest request) {
        return ContactFormFieldDto.builder()
                .contactFormId(request.getContactFormId())
                .name(request.getName())
                .label(request.getLabel())
                .type(request.getType())
                .placeholder(request.getPlaceholder())
                .helpText(request.getHelpText())
                .defaultValue(request.getDefaultValue())
                .isRequired(request.getIsRequired())
                .isVisible(request.getIsVisible())
                .isActive(request.getIsActive())
                .sortOrder(request.getSortOrder())
                .options(request.getOptions())
                .validation(request.getValidation())
                .build();
    }

    @Override
    protected ContactFormFieldResponse toResponse(ContactFormFieldDto dto) {
        return ContactFormFieldResponse.builder()
                .id(dto.getId())
                .contactFormId(dto.getContactFormId())
                .name(dto.getName())
                .label(dto.getLabel())
                .type(dto.getType())
                .placeholder(dto.getPlaceholder())
                .helpText(dto.getHelpText())
                .defaultValue(dto.getDefaultValue())
                .isRequired(dto.getIsRequired())
                .isVisible(dto.getIsVisible())
                .isActive(dto.getIsActive())
                .sortOrder(dto.getSortOrder())
                .options(dto.getOptions())
                .validation(dto.getValidation())
                .build();
    }

    @PostMapping("{contactForm}")
    public ApiResponse<ContactFormFieldResponse> addContactFormField(
            @CurrentUser JwtUser jwtUser,
            @PathVariable("contactForm") ContactFormDto contactForm,
            @RequestBody ContactFormFieldRequest request) {
        request.setContactFormId(contactForm.getId());

        return super.create(jwtUser, request);
    }
}
