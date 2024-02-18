package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.security.IsRealEstateOwner;
import com.kotyk.realtorconnect.annotation.security.IsRealEstatePublic;
import com.kotyk.realtorconnect.annotation.security.IsSameRealtor;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.realestate.RealEstateAddDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFilter;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFullDto;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.service.PermissionService;
import com.kotyk.realtorconnect.service.RealEstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.created;
import static com.kotyk.realtorconnect.util.ApiResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/realtors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Real Estate Controller")
public class RealEstateController {

    private final RealEstateService service;
    private final PermissionService permissionService;

    @IsSameRealtor
    @PostMapping("/{realtorId}/real-estates")
    @Operation(summary = "Add real estate")
    public ResponseEntity<ApiSuccess<RealEstateFullDto>> create(@PathVariable long realtorId, @RequestBody RealEstateAddDto realEstateDto) {
        return created(service.create(realtorId, realEstateDto));
    }

    @IsRealEstatePublic
    @GetMapping("/real-estates/{realEstateId}")
    @Operation(summary = "Get short real estate")
    public ResponseEntity<ApiSuccess<RealEstateDto>> readShortById(@PathVariable long realEstateId) {
        boolean canSeePrivatePhotos = permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_PHOTOS);
        return ok(service.readShortById(realEstateId, !canSeePrivatePhotos));
    }

    @IsRealEstateOwner
    @GetMapping("/real-estates/{realEstateId}/full")
    @Operation(summary = "Get full real estate")
    public ResponseEntity<ApiSuccess<RealEstateFullDto>> readFullById(@PathVariable long realEstateId) {
        return ok(service.readFullById(realEstateId));
    }

    @GetMapping("/real-estates")
    @Operation(summary = "Get short real estates")
    public ResponseEntity<ApiSuccess<Page<RealEstateDto>>> readAllShorts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "15") int size,
                                                                         @ModelAttribute RealEstateFilter filter) {
        boolean canSeePrivate = permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_REAL_ESTATES);
        boolean canSeePrivatePhotos = permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_PHOTOS);
        return ok(service.readAllShorts(filter, PageRequest.of(page, size), !canSeePrivate, !canSeePrivatePhotos));
    }

    @IsSameRealtor
    @GetMapping("/{realtorId}/real-estates/fulls")
    @Operation(summary = "Get full real estates. RealtorId in filter will be ignored")
    public ResponseEntity<ApiSuccess<Page<RealEstateFullDto>>> readAllFulls(@PathVariable long realtorId,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "15") int size,
                                                                            @ModelAttribute RealEstateFilter filter) {
        filter.setRealtorId(realtorId);
        return ok(service.readAllFulls(filter, PageRequest.of(page, size)));
    }


    @IsRealEstateOwner
    @PutMapping("/real-estates/{realEstateId}")
    @Operation(summary = "Update real estate")
    public ResponseEntity<ApiSuccess<RealEstateFullDto>> update(@PathVariable long realEstateId, @RequestBody RealEstateAddDto realEstateDto) {
        return ok(service.update(realEstateId, realEstateDto));
    }


    @IsRealEstateOwner
    @DeleteMapping("/real-estates/{realEstateId}")
    @Operation(summary = "Delete real estate")
    public ResponseEntity<Void> delete(@PathVariable long realEstateId) {
        service.delete(realEstateId);
        return ResponseEntity.noContent().build();
    }

    @IsRealEstateOwner
    @PutMapping("/real-estates/{realEstateId}/mark-called")
    @Operation(summary = "Mark real estate called")
    public ResponseEntity<ApiSuccess<Boolean>> updateCalled(@PathVariable long realEstateId) {
        return ok(service.updateCalled(realEstateId, true));
    }

}
