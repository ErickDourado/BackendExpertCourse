package com.erick.userserviceapi.controller.impl;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.erick.userserviceapi.creator.CreatorUtils.generateMock;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerImplTest {

    public static final String BASE_PATH = "/api/users";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Test
    void testFindByIdWithSuccess() throws Exception {
        final User entity = generateMock(User.class);
        final String userId = repository.save(entity).getId();

        mockMvc.perform(get(BASE_PATH.concat("/{id}"), userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.email").value(entity.getEmail()))
                .andExpect(jsonPath("$.password").value(entity.getPassword()))
                .andExpect(jsonPath("$.profiles").isArray());

        repository.deleteById(userId);
    }

    @Test
    void testFindByIdWithNotFoundException() throws Exception {
        final String id = "123";
        final String expectedMessage = "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName();
        final String path = BASE_PATH.concat("/").concat(id);

        mockMvc.perform(get(path))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.path").value(path));
    }

    @Test
    void testFindAllWithSuccess() throws Exception {
        final List<User> entities = List.of(generateMock(User.class), generateMock(User.class));
        repository.saveAll(entities);

        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].profiles").isArray())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[1].profiles").isArray());

        repository.deleteAll(entities);
    }

    @Test
    void testSaveWithSuccess() throws Exception {
        final String validEmail = "integrationTest@mail.com";
        final CreateUserRequest request = generateMock(CreateUserRequest.class).withEmail(validEmail);

        mockMvc.perform(post(BASE_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isCreated());

        repository.deleteByEmail(validEmail);
    }

    @Test
    void testSaveWithConflict() throws Exception {
        final String validEmail = "integrationTest@mail.com";
        final User entity = generateMock(User.class).withEmail(validEmail);
        final CreateUserRequest invalidRequest = generateMock(CreateUserRequest.class).withEmail(validEmail);

        repository.save(entity);

        mockMvc.perform(post(BASE_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(invalidRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(CONFLICT.value()))
                .andExpect(jsonPath("$.error").value(CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Email [ " + validEmail + " ] already exists"))
                .andExpect(jsonPath("$.path").value(BASE_PATH));

        repository.deleteById(entity.getId());
    }

}
