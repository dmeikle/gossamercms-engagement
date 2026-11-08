package com.gossamercms.contact.adapters.inbound;


import com.gossamercms.contact.services.ContactFormEmailService;
import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.contact.dtos.ContactSubmissionDto;
import com.gossamercms.contact.dtos.requests.ContactSubmissionRequest;
import com.gossamercms.contact.dtos.responses.ContactSubmissionResponse;
import com.gossamercms.contact.enums.ContactFormStatus;
import com.gossamercms.contact.enums.ContactSubmissionPriority;
import com.gossamercms.contact.handlers.ContactSubmissionsHandler;
import com.gossamercms.contact.models.ContactSubmission;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.annotations.CurrentUser;

import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.notifications.dtos.EmailMessageDto;
import gossamercms.globals.config.SystemPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/contact-submissions")
public class ContactSubmissionsController extends TypedBaseController<ContactSubmission, ContactSubmissionDto, ContactSubmissionRequest, ContactSubmissionResponse> {

    private final ContactFormEmailService contactFormEmailService;

    public ContactSubmissionsController(ContactSubmissionsHandler handler,
                                        ContactFormEmailService contactFormEmailService) {
        super(handler);
        this.contactFormEmailService = contactFormEmailService;
    }

    @Override
    protected ContactSubmissionDto toDto(ContactSubmissionRequest request) {
        return ContactSubmissionDto.builder()
                .contactFormId(request.getContactFormId())
                .submitterName(request.getSubmitterName())
                .submitterEmail(request.getSubmitterEmail())
                .subject(request.getSubject())
                .values(request.getValues())
                .status(request.getStatus())
                .priority(request.getPriority())
                .assignedTo(request.getAssignedTo())
                .sourceUrl(request.getSourceUrl())
                .referrerUrl(request.getReferrerUrl())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .submittedAt(request.getSubmittedAt())
                .resolvedAt(request.getResolvedAt())
                .build();
    }

    @Override
    protected ContactSubmissionResponse toResponse(ContactSubmissionDto dto) {
        return ContactSubmissionResponse.builder()
                .id(dto.getId())
                .contactFormId(dto.getContactFormId())
                .submitterName(dto.getSubmitterName())
                .submitterEmail(dto.getSubmitterEmail())
                .subject(dto.getSubject())
                .values(dto.getValues())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .assignedTo(dto.getAssignedTo())
                .sourceUrl(dto.getSourceUrl())
                .referrerUrl(dto.getReferrerUrl())
                .ipAddress(dto.getIpAddress())
                .userAgent(dto.getUserAgent())
                .submittedAt(dto.getSubmittedAt())
                .resolvedAt(dto.getResolvedAt())
                .build();
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<ContactSubmissionResponse> getById(@PathVariable("id") ContactSubmissionDto existing) {
        return super.getById(existing);
    }



    @PostMapping("/{contactForm}")
    public ApiResponse<ContactSubmissionResponse> create(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("contactForm") ContactFormDto contactForm,
            @RequestBody Map<String, Object> values,
            HttpServletRequest httpRequest) {
        UUID userId = jwtUser == null ? SystemPrincipal.ID : jwtUser.getUserId();
        ContactSubmissionRequest request = ContactSubmissionRequest.builder()
                .contactFormId(contactForm.getId())
                .submitterName(requiredString(values, "fullName"))
                .submitterEmail(requiredString(values, "email"))
                .subject(requiredString(values, "inquiryType"))
                .values(new LinkedHashMap<>(values))
                .status(ContactFormStatus.PENDING.toString())
                .priority(getStringOrDefault(values, "priority", ContactSubmissionPriority.NORMAL.toString()))
                .referrerUrl(httpRequest.getHeader("Referer"))
                .ipAddress(httpRequest.getRemoteAddr())
                .userAgent(httpRequest.getHeader("User-Agent"))
                .submittedAt(Instant.now())
                .build();
        ContactSubmissionDto submission = handler.create(userId, this.toDto(request));
        contactFormEmailService.sendAdminNotification(EmailMessageDto.builder()
                .fromName("dave meikle")
                .fromAddress("davemeikle@ymail.com")
                .subject("New Contact Form Submission: " + submission.getSubject())
                .body("You have a new contact form submission from " + submission.getSubmitterName() +
                        " (" + submission.getSubmitterEmail() + ").\n\n" +
                        "Subject: " + submission.getSubject() + "\n" +
                        "Values: " + submission.getValues().toString() + "\n" +
                        "Submitted At: " + submission.getSubmittedAt() + "\n" +
                        "IP Address: " + submission.getIpAddress() + "\n" +
                        "User Agent: " + submission.getUserAgent() + "\n" +
                        "Referrer URL: " + submission.getReferrerUrl() + "\n" +
                        "Source URL: " + submission.getSourceUrl())
                .build(), List.of("davemeikle@ymail.com"));

        return ApiResponse.ok(this.toResponse(submission));

    }

    private static String requiredString(Map<String, Object> values, String field) {
        if (!(values.get(field) instanceof String value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be a string");
        }
        return value;
    }

    private static String getStringOrDefault(Map<String, Object> values, String field, String defaultValue) {
        if (values.get(field) == null) {
            return defaultValue;
        }
        if (!(values.get(field) instanceof String value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be a string");
        }
        return value;
    }


    @DeleteMapping("/{id}")
    public void deleteById(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("id") ContactSubmissionDto existing) {
        _deleteById(jwtUser, existing);
    }



}
