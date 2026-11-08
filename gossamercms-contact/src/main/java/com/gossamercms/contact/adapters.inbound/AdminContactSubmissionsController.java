package com.gossamercms.contact.adapters.inbound;


import com.gossamercms.contact.dtos.ContactSubmissionDto;
import com.gossamercms.contact.dtos.requests.ContactSubmissionRequest;
import com.gossamercms.contact.dtos.responses.AdminContactSubmissionListResponse;
import com.gossamercms.contact.dtos.responses.ContactSubmissionResponse;
import com.gossamercms.contact.handlers.ContactSubmissionsHandler;
import com.gossamercms.contact.models.ContactSubmission;
import com.gossamercms.mvc.annotations.CurrentUser;
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
@RequestMapping("/admin/contact-submissions")
public class AdminContactSubmissionsController extends BaseController<ContactSubmission, ContactSubmissionDto, ContactSubmissionRequest, ContactSubmissionResponse, ContactSubmissionResponse> {

    public AdminContactSubmissionsController(ContactSubmissionsHandler handler) {
        super(handler, ContactSubmissionResponse.class);
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


    @GetMapping("list")
    public ApiResponse<ListResultset<AdminContactSubmissionListResponse>> getAllSubmissions(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {

        return ApiResponse.ok(handler.getAll(
                QueryOptions.of(page, size, params, Map.of()),
                AdminContactSubmissionListResponse.class));

    }
}
