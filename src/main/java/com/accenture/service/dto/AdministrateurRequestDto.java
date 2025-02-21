package com.accenture.service.dto;

import jakarta.validation.constraints.NotBlank;

public record AdministrateurRequestDto(
        @NotBlank(message= "renseignement obligatoire")
        String mail,
        @NotBlank(message= "renseignement obligatoire")
        String password,
        @NotBlank(message= "renseignement obligatoire")
        String nom,
        @NotBlank(message= "renseignement obligatoire")
        String prenom,
        @NotBlank(message= "renseignement obligatoire")
        String fonction) {
}
