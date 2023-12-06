package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.user.AuthenticationDTO;
import com.ecoufpel.ecoufpelapp.domains.user.LoginResponseDTO;
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
import org.springframework.security.test.context.support.WithUserDetails;
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
public class MeControllerIntegrationTest {
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
    @WithAnonymousUser
    @DisplayName("Trying to access information without being logged in should fail")
    void me_ShouldFail() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/me");
        MvcResult result = mvc.perform(request).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Logged user should be able to access information")
    void me_ShouldPass() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AuthenticationDTO(
                        "00000000000", "Eu sou o Batman!")));
        MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();

        LoginResponseDTO loginResponse = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponseDTO.class);

        request = MockMvcRequestBuilders
                .get("/me")
                .header("Authorization", "Bearer " + loginResponse.token());

        result = mvc.perform(request).andExpect(status().isOk()).andReturn();
    }

}
