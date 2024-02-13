package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Management Controller")
public class AdminManagementController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create admin")
    @PreAuthorize("hasAuthority('MANAGE_ADMINS')")
    public ResponseEntity<ApiSuccess<UserFullDto>> createAdmin(@RequestBody @Valid UserAddDto dto) {
        return created(userService.create(dto, Role.ADMIN));
    }

    @GetMapping
    @Operation(summary = "Get all admins")
    @PreAuthorize("hasAuthority('MANAGE_ADMINS')")
    public ResponseEntity<ApiSuccess<List<UserFullDto>>> getAllAdmins() {
        return ok(userService.readAllFulls(UserFilter.builder().roles(List.of(Role.ADMIN)).build()));
    }

}
