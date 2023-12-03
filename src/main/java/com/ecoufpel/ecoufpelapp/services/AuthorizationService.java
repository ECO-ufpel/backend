package com.ecoufpel.ecoufpelapp.services;

import com.ecoufpel.ecoufpelapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = repository.findBycpf(username);
        if (result.isPresent()) return result.get();
        throw new UsernameNotFoundException("Username " + username + " not found");
    }
}