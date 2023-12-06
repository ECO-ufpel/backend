package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.user.AuthenticationDTO;
import com.ecoufpel.ecoufpelapp.domains.user.RegisterDTO;
import com.ecoufpel.ecoufpelapp.domains.user.User;
import com.ecoufpel.ecoufpelapp.domains.user.UserRole;
import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import com.ecoufpel.ecoufpelapp.services.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

//    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Database integration test, should add one user to the database")
    void populate_db() throws Exception {
        var number_of_users_before = userRepository.findAll().size();
        userRepository.save(new User(
                "000.055.000-00",
                "Alfred",
                "alfred@wayne.corp",
                "00030",
                "https://i.pravatar.cc/150?u=a042581f4e29026704ds",
                passwordEncoder.encode("Master Bruce!")));
        assert userRepository.findAll().size() == number_of_users_before + 1;
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Login should pass beacause all fields are valid")
    void login_ShouldPass() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                                    .post("/auth/login")
                                    .contentType("application/json")
                                    .content(objectMapper.writeValueAsString(
                                            new AuthenticationDTO(
                                            "000.000.000-00",
                                            "Eu sou o Batman!")
                                            )
                                    );
        MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Login should fail if password is wrong")
    void login_ShouldFail_wrong_password() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(
                                new AuthenticationDTO(
                                        "000.000.000-00",
                                        "password123")
                        )
                );
        MvcResult result = mvc.perform(request).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Login should fail if cpf is not valid")
    void login_ShouldFail_wrong_cpf() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(
                                new AuthenticationDTO(
                                        "abcdefghijk",
                                        "Eu sou o Batman!")
                        )
                );
        MvcResult result = mvc.perform(request).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Login should fail if user is not registered")
    void login_ShouldFail_not_registered() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(
                                new AuthenticationDTO(
                                        "999.999.999-99",
                                        "")
                        )
                );
        MvcResult result = mvc.perform(request).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Register should pass beacause all fields are valid")
    void register_ShouldPass() throws Exception {
        RequestBuilder request =
                MockMvcRequestBuilders
                .post("/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(
                        new RegisterDTO(
                        "000.010.010-00",
                        "joao",
                        "joao@gmail.com",
                        "123456",
                        "https://i.pravatar.cc/150?u=a042581f4e29026704ds",
                        UserRole.USER,
                        "12346")));
        MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Register should fail if cpf is not valid")
    void register_ShouldFail() throws Exception {
        RequestBuilder request =
                MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                new RegisterDTO(
                                        "11.010.010-00",
                                        "joao",
                                        "joao@gmail.com",
                                        "123456",
                                        "https://i.pravatar.cc/150?u=a042581f4e29026704ds",
                                        UserRole.USER,
                                        "12346")));
        MvcResult result = mvc.perform(request).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Should not be able to access /greeting without authentication")
    void greetings_shouldFail() throws  Exception {
        RequestBuilder request =
                MockMvcRequestBuilders
                        .get("/greeting");
        MvcResult result = mvc.perform(request).andExpect(status().isForbidden()).andReturn();
    }

}