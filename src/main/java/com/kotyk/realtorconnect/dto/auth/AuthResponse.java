package com.kotyk.realtorconnect.dto.auth;

import com.kotyk.realtorconnect.dto.user.UserAuthDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private Token token;
    private UserAuthDto user;

    @Getter
    @AllArgsConstructor
    public static class Token {
        private String authToken;
    }

}
