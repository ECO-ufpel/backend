package com.ecoufpel.ecoufpelapp.domains.user;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDTORepository extends JpaRepository<Users, String> {
    UserDetails findBycpf(String cpf);
}