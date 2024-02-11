package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.IsSameUserOrCanManageUsers;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller")
public class UserController {

    private final UserService service;

    @IsSameUserOrCanManageUsers
    @GetMapping("/{id}")
    @Operation(summary = "Get user")
    public ResponseEntity<ApiSuccess<UserDto>> readById(@PathVariable long id) {
        return ok(service.readById(id));
    }

    @GetMapping
    @Operation(summary = "Get page of users")
    public ResponseEntity<ApiSuccess<Page<UserDto>>> readAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "15") int size,
                                                             @ModelAttribute UserFilter filter) {
        Pageable paging = PageRequest.of(page, size);
        return ok(service.readAll(filter, paging));
    }

    @IsSameUserOrCanManageUsers
    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<ApiSuccess<UserDto>> update(@PathVariable long id,
                                                      @RequestBody @Valid UserAddDto dto) {
        return ok(service.update(id, dto));
    }

    @IsSameUserOrCanManageUsers
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verifyEmail")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verify email of authenticated user")
    public ResponseEntity<ApiSuccess<Boolean>> verifyEmail() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ok(service.verifyEmail(username));
    }

}
