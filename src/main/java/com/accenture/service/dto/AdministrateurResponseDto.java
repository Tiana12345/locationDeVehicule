package com.accenture.service.dto;

public record AdministrateurResponseDto(
        String mail,
        String password,
        String nom,
        String prenom,
        String fonction) {
}
