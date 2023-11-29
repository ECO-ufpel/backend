package com.ecoufpel.ecoufpelapp.domains.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users", schema = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@Transactional(propagation=Propagation.REQUIRES_NEW)
public class User implements UserDetails {
    @Id
    private String cpf;
    private String name;
    private String email;
    private String registration;
    private String password;
    private UserRole role;

    public User(String cpf, String name, String email, String registration, String password) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.registration = registration;
        this.password = password;
        this.role = UserRole.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}