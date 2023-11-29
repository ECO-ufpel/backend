package com.ecoufpel.ecoufpelapp.controllers;

import com.ecoufpel.ecoufpelapp.domains.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/users")
public class UsersController {
    @Autowired
    private UsersDTORepository usersDTORepository;

    @Autowired
    private PasswordDTORepository passwordDTORepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (
            @RequestParam String cpf,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String registration,
            @RequestParam String password
    ){
        Users n = new Users();
        n.setUsername(name);
        n.setEmail(email);
        var cpf_sanitized = cpf.replaceAll("\\D", "");
        n.setCpf(cpf_sanitized);
        n.setRegistration(registration);
        n.setRole(UserRole.USER);

        usersDTORepository.save(n);
        // /add?cpf=01234567891&name=Gustavo&email=gh@gmail&matricula=12121212&password=123abcde


//      TODO generate hash and salt for storing in database
        PasswordDTO p = new PasswordDTO(cpf_sanitized, "SALT_", password);
        passwordDTORepository.save(p);

        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Users> getAllUsers() {
        // This returns a JSON or XML with the users
        return usersDTORepository.findAll();
    }
}
