package com.erick.authserviceapi.service;

import com.erick.authserviceapi.model.RefreshToken;
import com.erick.authserviceapi.repository.RefreshTokenRepository;
import com.erick.authserviceapi.security.dtos.UserDetailsDTO;
import com.erick.authserviceapi.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import models.exceptions.RefreshTokenExpiredException;
import models.exceptions.ResourceNotFoundException;
import models.responses.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    @Value("${jwt.expiration-sec.refresh-token}")
    private Long refreshTokenExpirationInSeconds;

    public RefreshToken save(String username) {
        return repository.save(
                RefreshToken.builder()
                        .id(UUID.randomUUID().toString())
                        .username(username)
                        .createdAt(now())
                        .expiresAt(now().plusSeconds(refreshTokenExpirationInSeconds))
                        .build()
        );
    }

    public RefreshTokenResponse refreshToken(final String refreshTokenId) {
        var refreshToken = repository.findById(refreshTokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found. Id: " + refreshTokenId));

        if (refreshToken.getExpiresAt().isBefore(now()))
            throw new RefreshTokenExpiredException("Refresh token expired. Id: " + refreshTokenId);

        var userDetails = userDetailsService.loadUserByUsername(refreshToken.getUsername());
        var newAccessToken = jwtUtils.generateToken((UserDetailsDTO) userDetails);
        return new RefreshTokenResponse(newAccessToken);
    }

}
