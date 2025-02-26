package com.accenture.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détails de la réponse d'administrateur")
public record AdministrateurResponseDto(
        @Schema(description = "Adresse email de l'administrateur", example = "admin@example.com")
        String mail,

        @Schema(description = "Mot de passe de l'administrateur", example = "password123")
        String password,

        @Schema(description = "Nom de l'administrateur", example = "Martin")
        String nom,

        @Schema(description = "Prénom de l'administrateur", example = "Sophie")
        String prenom,

        @Schema(description = "Fonction de l'administrateur", example = "Manager")
        String fonction) {
}