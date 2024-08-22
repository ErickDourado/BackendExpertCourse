package com.erick.userserviceapi.controller.impl;

import com.erick.userserviceapi.entity.User;
import com.erick.userserviceapi.repository.UserRepository;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.erick.userserviceapi.creator.CreatorUtils.generateMock;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Test
    void testFindByIdWithSuccess() throws Exception {
        final User entity = generateMock(User.class);
        final String userId = repository.save(entity).getId();

        mockMvc.perform(get("/api/users/{id}", userId))
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
        final String path = "/api/users/" + id;

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

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].profiles").isArray())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[1].profiles").isArray());

        repository.deleteAll(entities);
    }

}
