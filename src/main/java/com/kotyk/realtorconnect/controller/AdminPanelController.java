package com.kotyk.realtorconnect.controller;

import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.service.RealtorService;
import com.kotyk.realtorconnect.service.UserService;
import com.kotyk.realtorconnect.service.realestate.RealEstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static com.kotyk.realtorconnect.util.ApiResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin-panel", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Panel")
@PreAuthorize("hasAuthority('ACCESS_TO_ADMIN_PANEL')")
public class AdminPanelController {

    private final UserService userService;
    private final RealtorService realtorService;
    private final RealEstateService realEstateService;

    @PostMapping("/block-user")
    @Operation(summary = "Block user")
    public ResponseEntity<ApiSuccess<Boolean>> blockUser(@RequestParam long userId) {
        return ok(userService.updateBlocked(userId, true));
    }

    @PostMapping("/unblock-user")
    @Operation(summary = "Unblock user")
    public ResponseEntity<ApiSuccess<Boolean>> unblockUser(@RequestParam long userId) {
        return ok(userService.updateBlocked(userId, false));
    }

    @PostMapping("/verify-real-estate")
    @Operation(summary = "Verify real estate")
    public ResponseEntity<ApiSuccess<Boolean>> verifyRealEstate(@RequestParam long realEstateId) {
        return ok(realEstateService.updateVerified(realEstateId, true));
    }

    @PostMapping("/give-premium-to-realtor")
    @Operation(summary = "Add a premium to a realtor for a specified duration in months")
    public ResponseEntity<ApiSuccess<Instant>> givePremiumToRealtor(@RequestParam long realtorId,
                                                                    @RequestParam @Min(1) @Max(50) short durationInMonths) {
        return ok(realtorService.givePremiumToRealtor(realtorId, durationInMonths));
    }

    @GetMapping
    @Operation(summary = "Get page of users")
    public ResponseEntity<ApiSuccess<Page<UserFullDto>>> readAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "15") int size,
                                                                      @ModelAttribute UserFilter filter) {
        Pageable paging = PageRequest.of(page, size);
        return ok(userService.readAllFulls(filter, paging));
    }
}
