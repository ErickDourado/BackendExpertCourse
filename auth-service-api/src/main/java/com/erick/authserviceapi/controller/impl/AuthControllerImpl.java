package com.erick.authserviceapi.controller.impl;

import com.erick.authserviceapi.controller.AuthController;
import com.erick.authserviceapi.security.dtos.JWTAuthenticationImpl;
import com.erick.authserviceapi.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import models.requests.AuthenticateRequest;
import models.responses.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthControllerImpl implements AuthController {

    private final JWTUtils jwtUtils;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok(
                new JWTAuthenticationImpl(jwtUtils, authenticationConfiguration.getAuthenticationManager())
                        .authenticate(request)
        );
    }

}
