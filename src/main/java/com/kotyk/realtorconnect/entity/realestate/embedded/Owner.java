package com.kotyk.realtorconnect.entity.realestate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner {

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "owner_name")
    private String name;
    @Size(max = 20)
    @Pattern(regexp = "\\+380\\d{9}", message = "must match '+380111111111'")
    @Column(name = "owner_phone")
    private String phone;
    @Email
    @Size(min = 3, max = 255)
    @Column(name = "owner_email")
    private String email;

}
