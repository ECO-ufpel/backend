package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<UserDetails> findBycpf(String cpf);
    Optional<UserDetails> findByRegistration(String registration);

    Optional<UserDetails> findByCpfOrRegistration(String cpf, String registration);
}
