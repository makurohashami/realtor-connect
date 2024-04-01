package com.kotyk.realtorconnect.service.auth;

import com.kotyk.realtorconnect.dto.auth.AuthRequest;
import com.kotyk.realtorconnect.dto.auth.AuthResponse;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.mapper.UserMapper;
import com.kotyk.realtorconnect.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest request) {
        Authentication requestToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(requestToken);
        User user = (User) authentication.getPrincipal();
        userService.updateLastLogin(user.getId());
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(new AuthResponse.Token(jwtToken))
                .user(userMapper.toFullDto(user))
                .build();
    }
}
