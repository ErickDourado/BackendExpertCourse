package com.erick.userserviceapi.service;

import com.erick.userserviceapi.mapper.UserMapper;
import com.erick.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElse(null)
        );
    }

}
