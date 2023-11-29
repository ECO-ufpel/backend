package com.ecoufpel.ecoufpelapp.domains.user;

public record RegisterDTO(String cpf, String name, String email, String registration, UserRole role, String password) {
}
