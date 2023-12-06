package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.user.MeResponseDTO;
import com.ecoufpel.ecoufpelapp.domains.user.User;
import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MeController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/me")
    public ResponseEntity me(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();

            Optional<UserDetails> userDetailsOptional = (Optional<UserDetails>) principal;

            UserDetails userDetails = userDetailsOptional.get();
            MeResponseDTO response = new MeResponseDTO(
                    ((User) userDetails).getCpf(),
                    ((User) userDetails).getName(),
                    ((User) userDetails).getEmail(),
                    ((User) userDetails).getRegistration(),
                    ((User) userDetails).getImage());

            return ResponseEntity.status(200).body(response);
        }
        return ResponseEntity.status(403).body("Usuário não autenticado.");
    }

    public Optional<String> sanitizeCpf(String cpf) {
        var result = cpf.replaceAll("\\D", "");

        if (result.length() != 11) { return Optional.empty(); }
        return Optional.of(result);
    }
}
