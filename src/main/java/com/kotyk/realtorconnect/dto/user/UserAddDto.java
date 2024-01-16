package com.kotyk.realtorconnect.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddDto {

    @NotNull
    @Size(min = 3, max = 255)
    private String name;
    @Email
    @NotNull
    @Size(min = 3, max = 255)
    @Schema(example = "example@mail.com")
    private String email;
    @NotNull
    @Size(min = 3, max = 50)
    private String username;
    @NotNull
    @Size(min = 3, max = 255)
    private String password;
    @Size(max = 20)
    @Pattern(regexp = "\\+380\\d{9}", message = "must match '+380111111111'")
    private String phone;

}
