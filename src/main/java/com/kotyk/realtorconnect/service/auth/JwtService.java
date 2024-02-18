package com.kotyk.realtorconnect.service.auth;

import com.kotyk.realtorconnect.dto.auth.JwtToken;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String ROLE_CLAIM = "role";

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.token-ttl}")
    private Long tokenTtl;

    public String generateToken(User user) {
        return Jwts
                .builder()
                .setClaims(Map.of(ROLE_CLAIM, user.getRole().name()))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenTtl))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(JwtToken token) {
        return token.getExpiration().isAfter(Instant.now());
    }

    public JwtToken parseToken(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return new JwtToken(
                claims.getSubject(),
                Role.valueOf((String) claims.get(ROLE_CLAIM)),
                claims.getExpiration().toInstant()
        );
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

