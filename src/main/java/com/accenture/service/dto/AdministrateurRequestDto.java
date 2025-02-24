package com.accenture.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdministrateurRequestDto(
        @NotBlank(message = "L'adresse mail est obligatoire")
        @Email(message = "l'adresse mail doit être valide")
        String mail,
        @NotBlank(message = "Le mot e passe est obligatoire")
        @Size(min = 8, max = 16, message = "Le mot de passe doit contenir entre 8 et 16 caractères")
        String password,
        @NotBlank(message= "renseignement obligatoire")
        String nom,
        @NotBlank(message= "renseignement obligatoire")
        String prenom,
        @NotBlank(message= "renseignement obligatoire")
        String fonction) {
}
