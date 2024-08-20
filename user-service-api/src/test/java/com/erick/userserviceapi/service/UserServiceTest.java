package com.erick.userserviceapi.service;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.mapper.UserMapper;
import com.erick.userserviceapi.repository.UserRepository;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    @Test
    void whenCallFindByIdWithValidIdThenReturnUserResponse() {
        when(repository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(mapper.toUserResponse(any(User.class))).thenReturn(mock(UserResponse.class));

        final UserResponse response = service.findById("1");

        assertNotNull(response);
        assertEquals(response.getClass(), UserResponse.class);

        verify(repository, times(1)).findById(anyString());
        verify(mapper, times(1)).toUserResponse(any(User.class));
    }

}
