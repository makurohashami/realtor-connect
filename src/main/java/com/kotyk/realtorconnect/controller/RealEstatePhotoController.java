package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.security.IsRealEstateOwner;
import com.kotyk.realtorconnect.annotation.security.IsRealEstatePhotoOwner;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoDto;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoUpdateDto;
import com.kotyk.realtorconnect.service.realestate.RealEstatePhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/realtors/real-estates", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Real Estate Photo Controller", description = "Allows you manage your own real estates photos, and see other's real estates photos")
public class RealEstatePhotoController {

    private final RealEstatePhotoService realEstatePhotoService;

    @IsRealEstateOwner
    @PostMapping(path = "/{realEstateId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add photos to real estate")
    public ResponseEntity<ApiSuccess<List<RealEstatePhotoDto>>> create(@PathVariable long realEstateId,
                                                                       @RequestParam Set<MultipartFile> photos) {
        return created(realEstatePhotoService.create(realEstateId, photos));
    }

    @GetMapping("/{realEstateId}/photos")
    @Operation(summary = "Get all photos of real estate")
    public ResponseEntity<ApiSuccess<List<RealEstatePhotoDto>>> readAll(@PathVariable long realEstateId) {
        return ok(realEstatePhotoService.readAll(realEstateId));
    }

    @IsRealEstatePhotoOwner
    @PutMapping("/photos/{realEstatePhotoId}")
    @Operation(summary = "Update real estate photo")
    public ResponseEntity<ApiSuccess<RealEstatePhotoDto>> update(@PathVariable long realEstatePhotoId,
                                                                 @RequestBody RealEstatePhotoUpdateDto dto) {
        return ok(realEstatePhotoService.update(realEstatePhotoId, dto));
    }

    @IsRealEstateOwner
    @PutMapping("/{realEstateId}/photos")
    @Operation(summary = "Set photos order")
    public ResponseEntity<ApiSuccess<List<RealEstatePhotoDto>>> customiseOrder(@PathVariable long realEstateId,
                                                                               @RequestBody LinkedHashSet<Long> idsOrder) {
        return ok(realEstatePhotoService.customiseOrder(realEstateId, idsOrder));
    }

    @IsRealEstatePhotoOwner
    @DeleteMapping("/photos/{realEstatePhotoId}")
    @Operation(summary = "Delete real estate photo")
    public ResponseEntity<Void> delete(@PathVariable long realEstatePhotoId) {
        realEstatePhotoService.delete(realEstatePhotoId);
        return noContent();
    }

}
