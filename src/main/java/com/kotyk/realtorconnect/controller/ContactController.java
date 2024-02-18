package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.security.IsContactOwner;
import com.kotyk.realtorconnect.annotation.security.IsSameRealtor;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.realtor.ContactDto;
import com.kotyk.realtorconnect.service.contact.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.created;
import static com.kotyk.realtorconnect.util.ApiResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/realtors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Contact Controller")
public class ContactController {

    private final ContactService service;

    @IsSameRealtor
    @PostMapping("/{realtorId}/contacts")
    @Operation(summary = "Add new contact")
    public ResponseEntity<ApiSuccess<ContactDto>> create(@PathVariable long realtorId,
                                                         @RequestBody ContactDto contactDto) {
        return created(service.create(realtorId, contactDto));
    }

    @GetMapping("/contacts/{contactId}")
    @Operation(summary = "Get contact")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSuccess<ContactDto>> readById(@PathVariable long contactId) {
        return ok(service.readById(contactId));
    }

    @GetMapping("/{realtorId}/contacts")
    @Operation(summary = "Get all contacts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSuccess<List<ContactDto>>> readAll(@PathVariable long realtorId) {
        return ok(service.readAll(realtorId));
    }

    @IsContactOwner
    @PutMapping("/contacts/{contactId}")
    @Operation(summary = "Update contact")
    public ResponseEntity<ApiSuccess<ContactDto>> update(@PathVariable long contactId,
                                                         @RequestBody ContactDto contactDto) {
        return ok(service.update(contactId, contactDto));
    }

    @IsContactOwner
    @DeleteMapping("/contacts/{contactId}")
    @Operation(summary = "Delete contact")
    public ResponseEntity<Void> delete(@PathVariable long contactId) {
        service.delete(contactId);
        return ResponseEntity.noContent().build();
    }

}
