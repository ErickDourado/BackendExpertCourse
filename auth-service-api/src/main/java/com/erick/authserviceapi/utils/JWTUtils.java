package com.erick.authserviceapi.utils;

import com.erick.authserviceapi.security.dtos.UserDetailsDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    private final String secret;
    private final Long expiration;

    public JWTUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public String generateToken(final UserDetailsDTO userDetailsDTO) {
        return Jwts.builder()
                .claim("id", userDetailsDTO.getId())
                .claim("name", userDetailsDTO.getName())
                .claim("authorities", userDetailsDTO.getAuthorities())
                .setSubject(userDetailsDTO.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

}
