package com.erick.authserviceapi.security;

import com.erick.authserviceapi.security.dtos.UserDetailsDTO;
import com.erick.authserviceapi.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.requests.AuthenticateRequest;
import models.responses.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationImpl {

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        try {
            log.info("Authenticating user: {}", request.email());
            final var authResult = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            return buildAuthenticationResponse((UserDetailsDTO) authResult.getPrincipal());
        } catch (BadCredentialsException ex) {
            log.error("Error authenticating user: {}", request.email(), ex);
            throw new BadCredentialsException("Email or password invalid");
        }
    }

    protected AuthenticationResponse buildAuthenticationResponse(final UserDetailsDTO userDetailsDTO) {
        log.info("Successfully authenticated user: {}", userDetailsDTO.getUsername());
        final var token = jwtUtils.generateToken(userDetailsDTO);
        return AuthenticationResponse.builder()
                .type("Bearer")
                .token(token)
                .build();
    }

}
