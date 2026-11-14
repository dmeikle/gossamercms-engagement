package com.gossamercms.contact.adapters.inbound;


import com.gossamercms.contact.dtos.ContactSubmissionAttachmentDto;
import com.gossamercms.contact.dtos.requests.ContactSubmissionAttachmentRequest;
import com.gossamercms.contact.dtos.responses.ContactSubmissionAttachmentResponse;
import com.gossamercms.contact.handlers.ContactSubmissionAttachmentsHandler;
import com.gossamercms.contact.models.ContactSubmissionAttachment;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/contact-submission-attachments")
public class ContactSubmissionAttachmentsController extends TypedBaseController<ContactSubmissionAttachment, ContactSubmissionAttachmentDto, ContactSubmissionAttachmentRequest, ContactSubmissionAttachmentResponse, ContactSubmissionAttachmentResponse> {

    public ContactSubmissionAttachmentsController(ContactSubmissionAttachmentsHandler handler) {
        super(handler, ContactSubmissionAttachmentResponse.class);
    }

    @Override
    protected ContactSubmissionAttachmentDto toDto(ContactSubmissionAttachmentRequest request) {
        return ContactSubmissionAttachmentDto.builder()
                .contactSubmissionId(request.getContactSubmissionId())
                .fileName(request.getFileName())
                .contentType(request.getContentType())
                .fileSize(request.getFileSize())
                .storageKey(request.getStorageKey())
                .uploadedBy(request.getUploadedBy())
                .uploadedAt(request.getUploadedAt())
                .build();
    }

    @Override
    protected ContactSubmissionAttachmentResponse toResponse(ContactSubmissionAttachmentDto dto) {
        return ContactSubmissionAttachmentResponse.builder()
                .id(dto.getId())
                .contactSubmissionId(dto.getContactSubmissionId())
                .fileName(dto.getFileName())
                .contentType(dto.getContentType())
                .fileSize(dto.getFileSize())
                .storageKey(dto.getStorageKey())
                .uploadedBy(dto.getUploadedBy())
                .uploadedAt(dto.getUploadedAt())
                .build();
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<ContactSubmissionAttachmentResponse> getById(@PathVariable("id") ContactSubmissionAttachmentDto existing) {
        return super.getById(existing);
    }

    @GetMapping("/get")
    public ApiResponse<ContactSubmissionAttachmentResponse> get(@RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_get(params));
    }

    @GetMapping
    public ApiResponse<ListResultset<ContactSubmissionAttachmentResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @PostMapping
    public ApiResponse<ContactSubmissionAttachmentResponse> create(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestBody ContactSubmissionAttachmentRequest request) {
        return ApiResponse.ok(_create(jwtUser, request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ContactSubmissionAttachmentResponse> updateById(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("id") ContactSubmissionAttachmentDto existing,
            @RequestBody ContactSubmissionAttachmentRequest request) {
        return ApiResponse.ok(_updateById(jwtUser, existing, request));
    }

    @PutMapping("/update")
    public ApiResponse<ContactSubmissionAttachmentResponse> update(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestBody ContactSubmissionAttachmentRequest request,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_update(jwtUser, request, params));
    }

    @DeleteMapping("/{id}")
    public void deleteById(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("id") ContactSubmissionAttachmentDto existing) {
        _deleteById(jwtUser, existing);
    }

    @DeleteMapping
    public void delete(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam Map<String, Object> params) {
        _delete(jwtUser, params);
    }

    @PostMapping("/{id}/restore")
    public ApiResponse<ContactSubmissionAttachmentResponse> restore(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("id") UUID id) {
        return ApiResponse.ok(_restore(jwtUser, id));
    }

    @PostMapping("/bulk")
    public ApiResponse<ListResultset<ContactSubmissionAttachmentResponse>> createOrReplaceBulk(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestBody List<ContactSubmissionAttachmentRequest> requests,
            @RequestParam Map<String, Object> deleteExistingKey) {
        return ApiResponse.ok(_createOrReplaceBulk(jwtUser, requests, deleteExistingKey));
    }
}
