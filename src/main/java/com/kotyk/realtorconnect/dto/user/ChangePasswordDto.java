package com.kotyk.realtorconnect.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @NotNull
    private UUID token;
    @NotNull
    @Size(min = 3, max = 255)
    private String password;
    @NotNull
    @Size(min = 3, max = 255)
    private String passwordConfirm;

    public boolean passwordsMatch() {
        return password.equals(passwordConfirm);
    }

}
