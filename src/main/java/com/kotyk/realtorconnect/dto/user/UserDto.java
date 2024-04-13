package com.kotyk.realtorconnect.dto.user;

import com.kotyk.realtorconnect.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String avatar;
    private Role role;

}
