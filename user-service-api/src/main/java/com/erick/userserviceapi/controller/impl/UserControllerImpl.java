package com.erick.userserviceapi.controller.impl;

import com.erick.userserviceapi.controller.UserController;
import com.erick.userserviceapi.service.UserService;
import lombok.RequiredArgsConstructor;
import models.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> findById(String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

}
