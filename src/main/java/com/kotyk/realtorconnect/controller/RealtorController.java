package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.security.IsSameUser;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.realtor.RealtorAddDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorFilter;
import com.kotyk.realtorconnect.dto.realtor.RealtorFullDto;
import com.kotyk.realtorconnect.service.realtor.RealtorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/realtors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Realtor Controller", description = "Allows you manage your information(if you are realtor), and see other's realtors")
public class RealtorController {

    private final RealtorService service;

    @IsSameUser
    @GetMapping("/{id}/full")
    @Operation(summary = "Get full realtor")
    public ResponseEntity<ApiSuccess<RealtorFullDto>> readFullById(@PathVariable long id) {
        return ok(service.readFullById(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get short realtor")
    public ResponseEntity<ApiSuccess<RealtorDto>> readShortById(@PathVariable long id) {
        return ok(service.readShortById(id));
    }

    @GetMapping
    @Operation(summary = "Get page of short realtors")
    public ResponseEntity<ApiSuccess<Page<RealtorDto>>> getAllShorts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @ModelAttribute RealtorFilter filter
    ) {
        Pageable paging = PageRequest.of(page, size);
        return ok(service.getAllShorts(filter, paging));
    }

    @IsSameUser
    @PutMapping("/{id}")
    @Operation(summary = "Update realtor")
    public ResponseEntity<ApiSuccess<RealtorFullDto>> update(@PathVariable long id,
                                                             @RequestBody @Valid RealtorAddDto dto) {
        return ok(service.update(id, dto));
    }

    @IsSameUser
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete realtor")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
