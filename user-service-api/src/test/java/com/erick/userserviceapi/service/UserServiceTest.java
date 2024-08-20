package com.erick.userserviceapi.service;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.mapper.UserMapper;
import com.erick.userserviceapi.repository.UserRepository;
import models.exceptions.ResourceNotFoundException;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
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
        assertEquals(UserResponse.class, response.getClass());

        verify(repository, times(1)).findById(anyString());
        verify(mapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        try {
            service.findById("1");
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Object not found. Id: 1, Type: UserResponse", e.getMessage());
        }

        verify(repository, times(1)).findById(anyString());
        verify(mapper, times(0)).toUserResponse(any(User.class));
    }

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {
        List<User> userEntityList = List.of(new User(), new User());
        List<UserResponse> userResponseList = List.of(mock(UserResponse.class), mock(UserResponse.class));

        when(repository.findAll()).thenReturn(userEntityList);
        when(mapper.toUserResponseList(anyList())).thenReturn(userResponseList);

        final List<UserResponse> response = service.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toUserResponseList(userEntityList);
    }

    @Test
    void whenCallFindAllReturnEmptyListAndThenThrowResourceNotFoundException() {
        when(repository.findAll()).thenReturn(List.of());

        try {
            service.findAll();
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("No users found", e.getMessage());
        }

        verify(repository, times(1)).findAll();
        verify(mapper, times(0)).toUserResponseList(anyList());
    }

}
