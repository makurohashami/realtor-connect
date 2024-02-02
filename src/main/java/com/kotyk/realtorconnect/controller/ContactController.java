package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.IsContactOwnerOrCanManageRealtorInfo;
import com.kotyk.realtorconnect.annotation.IsSameRealtorOrCanManageRealtorInfo;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.realtor.ContactDto;
import com.kotyk.realtorconnect.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @IsSameRealtorOrCanManageRealtorInfo
    @PostMapping("/{realtorId}/contacts")
    @Operation(summary = "Add new contact")
    public ResponseEntity<ApiSuccess<ContactDto>> create(@PathVariable long realtorId,
                                                         @RequestBody ContactDto contactDto) {
        return created(service.create(realtorId, contactDto));
    }

    @GetMapping("/contacts/{contactId}")
    @Operation(summary = "Get contact")
    public ResponseEntity<ApiSuccess<ContactDto>> readById(@PathVariable long contactId) {
        return ok(service.readById(contactId));
    }

    @GetMapping("/{realtorId}/contacts")
    @Operation(summary = "Get all contacts")
    public ResponseEntity<ApiSuccess<List<ContactDto>>> readAll(@PathVariable long realtorId) {
        return ok(service.readAll(realtorId));
    }

    @IsContactOwnerOrCanManageRealtorInfo
    @PutMapping("/contacts/{contactId}")
    @Operation(summary = "Update contact")
    public ResponseEntity<ApiSuccess<ContactDto>> update(@PathVariable long contactId,
                                                         @RequestBody ContactDto contactDto) {
        return ok(service.update(contactId, contactDto));
    }

    @IsContactOwnerOrCanManageRealtorInfo
    @DeleteMapping("/contacts/{contactId}")
    @Operation(summary = "Delete contact")
    public ResponseEntity<Void> delete(@PathVariable long contactId) {
        service.delete(contactId);
        return ResponseEntity.noContent().build();
    }

}
