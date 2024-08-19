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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final BCryptPasswordEncoder encoder;

    public UserResponse findById(final String id) {
        User user = find(id);
        return mapper.toUserResponse(user);
    }

    public void save(CreateUserRequest request) {
        verifyIfEmailAlreadyExists(request.email(), null);
        User user = mapper.toUser(request).withPassword(encoder.encode(request.password()));
        repository.save(user);
    }

    public List<UserResponse> findAll() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return mapper.toUserResponseList(users);
    }

    public UserResponse update(final UpdateUserRequest request, final String id) {
        User oldUser = find(id);
        verifyIfEmailAlreadyExists(request.email(), id);
        User newUser = repository.save(mapper.updateToUser(request, oldUser)
                .withPassword(request.password() != null ? encoder.encode(request.password()) : oldUser.getPassword()));
        return mapper.toUserResponse(newUser);
    }

    private User find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
    }

    private void verifyIfEmailAlreadyExists(final String email, final String id) {
        repository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                });
    }

}
