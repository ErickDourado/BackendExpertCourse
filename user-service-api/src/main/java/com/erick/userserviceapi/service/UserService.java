package com.erick.userserviceapi.service;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.mapper.UserMapper;
import com.erick.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        User user = find(id);
        return userMapper.toUserResponse(user);
    }

    public void save(CreateUserRequest request) {
        verifyIfEmailAlreadyExists(request.email(), null);
        User user = userMapper.toUser(request);
        userRepository.save(user);
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userMapper.toUserResponseList(users);
    }

    public UserResponse update(final UpdateUserRequest request, final String id) {
        User oldUser = find(id);
        verifyIfEmailAlreadyExists(request.email(), id);
        User newUser = userRepository.save(userMapper.updateToUser(request, oldUser));
        return userMapper.toUserResponse(newUser);
    }

    private User find(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
    }

    private void verifyIfEmailAlreadyExists(final String email, final String id) {
        userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                });
    }

}
