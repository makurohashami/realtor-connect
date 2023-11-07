package com.kotyk.realtorconnect.entity.realestate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner {

    @Column(name = "owner_name")
    private String name;
    @Column(name = "owner_phone")
    private String phone;
    @Column(name = "owner_email")
    private String email;

}
