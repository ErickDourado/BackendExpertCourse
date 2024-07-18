package com.erick.userserviceapi.service;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.mapper.UserMapper;
import com.erick.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
        return userMapper.toUserResponse(user);
    }

    public void save(CreateUserRequest request) {
        User user = userMapper.toUser(request);
        userRepository.save(user);
    }

}
