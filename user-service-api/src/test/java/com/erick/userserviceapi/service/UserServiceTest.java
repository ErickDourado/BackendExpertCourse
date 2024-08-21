package com.erick.userserviceapi.service;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.mapper.UserMapper;
import com.erick.userserviceapi.repository.UserRepository;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.erick.userserviceapi.creator.CreatorUtils.generateMock;
import static java.util.Collections.emptyList;
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
        when(mapper.toUserResponse(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final UserResponse response = service.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(repository).findById(anyString());
        verify(mapper).toUserResponse(any(User.class));
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

        verify(repository).findById(anyString());
        verify(mapper, times(0)).toUserResponse(any(User.class));
    }

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {
        var userEntityList = List.of(new User(), new User());
        var userResponseList = List.of(generateMock(UserResponse.class), generateMock(UserResponse.class));

        when(repository.findAll()).thenReturn(userEntityList);
        when(mapper.toUserResponseList(anyList())).thenReturn(userResponseList);

        final List<UserResponse> response = service.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(repository).findAll();
        verify(mapper).toUserResponseList(userEntityList);
    }

    @Test
    void whenCallFindAllReturnEmptyListAndThenThrowResourceNotFoundException() {
        when(repository.findAll()).thenReturn(emptyList());

        try {
            service.findAll();
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("No users found", e.getMessage());
        }

        verify(repository).findAll();
        verify(mapper, times(0)).toUserResponseList(anyList());
    }

    @Test
    void whenCallSaveThenSuccess() {
        final CreateUserRequest request = generateMock(CreateUserRequest.class);

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(mapper.toUser(any())).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any(User.class))).thenReturn(new User());

        service.save(request);

        verify(repository).findByEmail(request.email());
        verify(mapper).toUser(request);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
    }

    @Test
    void whenCallSaveWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final CreateUserRequest request = generateMock(CreateUserRequest.class);

        when(repository.findByEmail(anyString())).thenReturn(Optional.of(generateMock(User.class)));

        try {
            service.save(request);
        } catch (Exception e) {
            assertEquals(DataIntegrityViolationException.class, e.getClass());
            assertEquals("Email [ " + request.email() + " ] already exists", e.getMessage());
        }

        verify(repository).findByEmail(request.email());
        verify(mapper, times(0)).toUser(any(CreateUserRequest.class));
        verify(encoder, times(0)).encode(anyString());
        verify(repository, times(0)).save(any(User.class));
    }

    @Test
    void WhenCallUpdateThenSuccess() {
        final String id = "1";
        final User oldUser = generateMock(User.class).withId(id);
        final User newUser = generateMock(User.class).withId(id);
        final UserResponse response = generateMock(UserResponse.class);
        final UpdateUserRequest request = generateMock(UpdateUserRequest.class);

        when(repository.findById(id)).thenReturn(Optional.of(oldUser));
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(oldUser));
        when(mapper.updateToUser(request, oldUser)).thenReturn(newUser);
        when(encoder.encode(request.password())).thenReturn("encoded");
        when(repository.save(any(User.class))).thenReturn(newUser);
        when(mapper.toUserResponse(newUser)).thenReturn(response);

        final UserResponse userResponse = service.update(request, id);

        assertNotNull(userResponse);
        assertEquals(UserResponse.class, userResponse.getClass());

        verify(repository).findById(id);
        verify(repository).findByEmail(request.email());
        verify(mapper).updateToUser(request, oldUser);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
        verify(mapper).toUserResponse(newUser);
    }

    @Test
    void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
        final String id = "1";
        final UpdateUserRequest request = generateMock(UpdateUserRequest.class);

        when(repository.findById(id)).thenReturn(Optional.empty());

        try {
            service.update(request, id);
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Object not found. Id: 1, Type: UserResponse", e.getMessage());
        }

        verify(repository).findById(id);
        verify(repository, times(0)).findByEmail(anyString());
        verify(mapper, times(0)).updateToUser(any(UpdateUserRequest.class), any(User.class));
        verify(encoder, times(0)).encode(anyString());
        verify(repository, times(0)).save(any(User.class));
        verify(mapper, times(0)).toUserResponse(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final String id = "1";
        final User oldUser = generateMock(User.class);
        final UpdateUserRequest request = generateMock(UpdateUserRequest.class);

        when(repository.findById(id)).thenReturn(Optional.of(oldUser));
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(generateMock(User.class)));

        try {
            service.update(request, id);
        } catch (Exception e) {
            assertEquals(DataIntegrityViolationException.class, e.getClass());
            assertEquals("Email [ " + request.email() + " ] already exists", e.getMessage()); 
        }

        verify(repository).findById(id);
        verify(repository).findByEmail(request.email());
        verify(mapper, times(0)).updateToUser(any(UpdateUserRequest.class), any(User.class));
        verify(encoder, times(0)).encode(anyString());
        verify(repository, times(0)).save(any(User.class));
        verify(mapper, times(0)).toUserResponse(any(User.class));
    }

}
