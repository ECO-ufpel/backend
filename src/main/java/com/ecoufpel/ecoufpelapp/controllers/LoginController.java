package com.ecoufpel.ecoufpelapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Hello");
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        System.err.println(loginRequest);
        if (loginRequest.username.equals("Joao") && loginRequest.password.equals("123")) {
//        if (true){
            authenticationResponse.setAuthenticated(true);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        authenticationResponse.setAuthenticated(false);
        return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
    }

    public record LoginRequest(String username, String password) {
    }

}