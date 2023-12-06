package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.user.AuthenticationDTO;
import com.ecoufpel.ecoufpelapp.domains.user.LoginResponseDTO;
import com.ecoufpel.ecoufpelapp.domains.user.RegisterDTO;
import com.ecoufpel.ecoufpelapp.domains.user.User;
import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import com.ecoufpel.ecoufpelapp.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var sanitized_cpf = sanitizeCpf(data.cpf());
        if (sanitized_cpf.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF not valid, 11 digits necessary");
        }
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(sanitized_cpf.get(), data.password());

            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException exception){
            System.err.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data){
        var sanitized_cpf = sanitizeCpf(data.cpf());
        if (sanitized_cpf.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF not valid, 11 digits necessary");
        }
        if(this.repository.findByCpfOrRegistration(sanitized_cpf.get(), data.registration()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe em nossa base de dados.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(sanitized_cpf.get(), data.name(), data.email(), data.registration(), data.image(), encryptedPassword);

        this.repository.save(newUser);

        return ResponseEntity.status(HttpStatus.OK).body("Usuário criado com sucesso em nossa base de dados.");
    }

    /**
     * This function only validate if cpf has digits and has 11 digits
     * it does not check if is a valid cpf with verification digit
     * @param cpf - the string to verify
     * @return Optional - an option containing a stripped string with only digits or None
     */
    public Optional<String> sanitizeCpf(String cpf) {
        var result = cpf.replaceAll("\\D", "");

        if (result.length() != 11) { return Optional.empty(); }
        return Optional.of(result);
    }

}
