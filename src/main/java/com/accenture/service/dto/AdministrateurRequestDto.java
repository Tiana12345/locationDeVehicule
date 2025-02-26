package com.accenture.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Détails de la demande d'administrateur")
public record AdministrateurRequestDto(
        @Schema(description = "Adresse email de l'administrateur", example = "admin@example.com")
        @NotBlank(message = "L'adresse mail est obligatoire")
        @Email(message = "L'adresse mail doit être valide")
        String mail,

        @Schema(description = "Mot de passe de l'administrateur", example = "password123")
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, max = 16, message = "Le mot de passe doit contenir entre 8 et 16 caractères")
        String password,

        @Schema(description = "Nom de l'administrateur", example = "Martin")
        @NotBlank(message= "Renseignement obligatoire")
        String nom,

        @Schema(description = "Prénom de l'administrateur", example = "Sophie")
        @NotBlank(message= "Renseignement obligatoire")
        String prenom,

        @Schema(description = "Fonction de l'administrateur", example = "Manager")
        @NotBlank(message= "Renseignement obligatoire")
        String fonction) {
}