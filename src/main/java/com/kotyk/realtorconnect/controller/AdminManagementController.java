package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFilter;
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
@RequestMapping(value = "/admin-management", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Management Controller")
public class AdminManagementController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create admin")
    @PreAuthorize("hasAuthority('MANAGE_ADMINS')")
    public ResponseEntity<ApiSuccess<UserDto>> createAdmin(@RequestBody @Valid UserAddDto dto) {
        return created(userService.create(dto, Role.ADMIN));
    }

    @GetMapping
    @Operation(summary = "Get all admins")
    @PreAuthorize("hasAuthority('MANAGE_ADMINS')")
    public ResponseEntity<ApiSuccess<List<UserDto>>> getAllAdmins() {
        return ok(userService.readAll(UserFilter.builder().role(Role.ADMIN).build()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete admin")
    @PreAuthorize("hasAuthority('MANAGE_ADMINS')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable long id) {
        userService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

}
