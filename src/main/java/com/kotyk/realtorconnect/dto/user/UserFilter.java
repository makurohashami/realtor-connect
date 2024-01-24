package com.kotyk.realtorconnect.dto.user;

import com.kotyk.realtorconnect.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {

    private String name;
    private String phone;
    private String email;
    private Role role;

}
