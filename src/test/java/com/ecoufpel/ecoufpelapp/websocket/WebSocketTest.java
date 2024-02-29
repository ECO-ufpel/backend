package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.user.AuthenticationDTO;
import com.ecoufpel.ecoufpelapp.domains.user.RegisterDTO;
import com.ecoufpel.ecoufpelapp.domains.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.DeploymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@WebAppConfiguration
public class WebSocketTest
{
    private WebApplicationContext context;
    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private WebSocketHandler webSocketHandler;
    @LocalServerPort
    private Integer port;

    @Autowired
    public WebSocketTest(WebApplicationContext context, MockMvc mvc, ObjectMapper objectMapper, WebSocketHandler webSocketHandler) {
        this.context = context;
        this.mvc = mvc;
        this.objectMapper = objectMapper;
        this.webSocketHandler = webSocketHandler;
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Try to connect to websocket without a valid token")
    void WebSocketWithoutValidToken() throws Exception {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        var future = webSocketClient.execute(
                this.webSocketHandler,
                "ws://localhost:" + port + "/ws"
        );

        try {
            var socketSession = future.join();
        } catch (Exception e) {
            assert e.getCause().getMessage().equals("The HTTP response from the server [200] did not permit the HTTP upgrade to WebSocket");
            return;
        }

        assert false;
    }

    @Test
    @DisplayName("Try to connect to websocket with a valid token")
    void WebSocketWithValidToken() throws Exception {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        // Login and get toker
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

        var token = "Bearer " + result
                .getResponse()
                .getContentAsString()
                .split(":")[1]
                .replace("\"", "")
                .replace("}", "")
                .strip();

        var future = webSocketClient.execute(
                this.webSocketHandler,
                "ws://localhost:" + port + "/ws" + "?token=" + token
        );

        try {
            var socketSession = future.join();
        } catch (Exception e) {
            assert false;
        }
    }
}
