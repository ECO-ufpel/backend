package com.ecoufpel.ecoufpelapp.domains.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.ecoufpel.ecoufpelapp.domains.user.PasswordDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordDTORepository extends JpaRepository<PasswordDTO, String> {
    UserDetails findBycpf(String cpf);
}

//ALTER TABLE users.users
//ADD role varchar(16) NOT NULL DEFAULT 'user';