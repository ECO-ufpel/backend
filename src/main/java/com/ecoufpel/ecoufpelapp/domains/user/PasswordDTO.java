package com.ecoufpel.ecoufpelapp.domains.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "password", schema = "users")
@Entity(name = "password")
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class PasswordDTO /*implements UserDetails */ {
    @Id
    private String cpf;
    private String hash;
    private String salt;

    public PasswordDTO(String cpfNumber, String s, String password) {
        this.cpf = cpfNumber;
        this.salt = s;
        this.hash = password;
    }

    public void setCpf(String cpf) {this.cpf = cpf;}
    public void setHash(String hash){ this.hash = hash;}
    public void setSalt(String salt){ this.salt = salt;}
}