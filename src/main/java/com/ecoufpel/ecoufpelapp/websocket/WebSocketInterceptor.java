package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import com.ecoufpel.ecoufpelapp.security.SecurityFilter;
import com.ecoufpel.ecoufpelapp.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketInterceptor implements HandshakeInterceptor {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Autowired
    public WebSocketInterceptor(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String queryURI = request.getURI().getQuery();
        if (queryURI == null) {
            return false;
        }
        HashMap<String, String> queryMap = parseQueryString(queryURI);
        String token = queryMap.get("token");
        if (token == null) {
            return false;
        }

        var res = tokenService.validateToken(token.replace("Bearer ", "")); // Validate the token

        if (res.isEmpty()) {
            response.setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
            return false;
        }

        userRepository.findUserByCpf(res).ifPresent(user -> {
            attributes.put("user", user);
        });

        attributes.put("token", token); // Store the token in session attributes

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            System.out.println("Exception after Handshake: " + exception.getMessage());
            return;
        }
        response.setStatusCode(HttpStatus.SWITCHING_PROTOCOLS);
    }

    private HashMap<String, String> parseQueryString(String query) {
        HashMap<String, String> map = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
