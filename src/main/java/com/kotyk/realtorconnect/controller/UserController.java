package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.annotation.security.IsSameUser;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get short user")
    public ResponseEntity<ApiSuccess<UserDto>> readById(@PathVariable long id) {
        return ok(service.readById(id));
    }

    @IsSameUser
    @GetMapping("/{id}/full")
    @Operation(summary = "Get full user")
    public ResponseEntity<ApiSuccess<UserFullDto>> readFullById(@PathVariable long id) {
        return ok(service.readFullById(id));
    }

    @IsSameUser
    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<ApiSuccess<UserFullDto>> update(@PathVariable long id,
                                                          @RequestBody @Valid UserAddDto dto) {
        return ok(service.update(id, dto));
    }

    @IsSameUser
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verifyEmail/{token}")
    @PreAuthorize("isAnonymous()")
    @Operation(summary = "Verify email of anonymous user")
    public ResponseEntity<ApiSuccess<Boolean>> verifyEmail(@PathVariable String token) {
        return ok(service.verifyEmail(token));
    }

}
