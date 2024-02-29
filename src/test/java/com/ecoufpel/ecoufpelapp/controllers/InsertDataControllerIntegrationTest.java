package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.ufpel_data.Classrooms;
import com.ecoufpel.ecoufpelapp.domains.user.AuthenticationDTO;
import com.ecoufpel.ecoufpelapp.repositories.ClassroomsRepository;
import com.ecoufpel.ecoufpelapp.services.InsertDataConsuptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class InsertDataControllerIntegrationTest {
    private WebApplicationContext context;
    private MockMvc mvc;
    private ClassroomsRepository classroomsRepository;

    @Autowired
    InsertDataControllerIntegrationTest(WebApplicationContext context, MockMvc mvc, ClassroomsRepository classroomsRepository) {
        this.context = context;
        this.mvc = mvc;
        this.classroomsRepository = classroomsRepository;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Insert data consumption without a valid token")
    public void insertDataConsumptionWithoutValidToken() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/sensor/insert")
                .param("room_id", "1")
                .param("consumption", "7.0")
                .param("time", "2021-10-10T10:10:10-00:00");

        MvcResult result = mvc.perform(request).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Insert data consumption with a valid token")
    public void insertDataConsumptionWithValidToken() throws Exception {
        // Generate a token
        RequestBuilder request_create_token = MockMvcRequestBuilders
                .post("/api/create");

        MvcResult result_create_token = mvc.perform(request_create_token).andExpect(status().isOk()).andReturn();

        // Create a Room with the id
        classroomsRepository.save(new Classrooms("1"));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/sensor/insert")
                .param("room_id", "1")
                .param("consumption", "7.0")
                .param("time", "2021-10-10T10:10:10-00:00")
                .param("API-Key", result_create_token.getResponse().getContentAsString());

        MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("Try to insert data in the wrong format")
    public void insertDataWithWrongFormat() throws Exception {
        // Generate a token
        RequestBuilder request_create_token = MockMvcRequestBuilders
                .post("/api/create");

        MvcResult result_create_token = mvc.perform(request_create_token).andExpect(status().isOk()).andReturn();

        // Create a Room with the id
        classroomsRepository.save(new Classrooms("1"));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/sensor/insert")
                .param("room_id", "")
                .param("consumption", "abc")
                .param("time", "diasdepois")
                .param("API-Key", result_create_token.getResponse().getContentAsString());

        MvcResult result = mvc.perform(request).andExpect(status().isBadRequest()).andReturn();
    }

    /* TODO: Change test database to have the constraints of the real database
    @Test
    @WithMockUser("admin")
    @DisplayName("Insert data consumption with a valid token on a non existent room")
    public void insertDataConsumptionWithValidTokenInNonExistentRoom() throws Exception {
        // Generate a token
        RequestBuilder request_create_token = MockMvcRequestBuilders
                .post("/api/create");

        MvcResult result_create_token = mvc.perform(request_create_token).andExpect(status().isOk()).andReturn();

        RequestBuilder request = MockMvcRequestBuilders
                .post("/sensor/insert")
                .param("room_id", "18")
                .param("consumption", "7.0")
                .param("time", "2021-10-10T10:10:10-00:00")
                .param("API-Key", result_create_token.getResponse().getContentAsString());

        MvcResult result = mvc.perform(request).andExpect(status().isForbidden()).andReturn();
    }
    */
}
