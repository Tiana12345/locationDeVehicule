package com.accenture.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détails de l'adresse")
public record AdresseDto(
        @Schema(description = "Rue de l'adresse", example = "123 Rue de Paris")
        String rue,

        @Schema(description = "Code postal de l'adresse", example = "75000")
        String codePostal,

        @Schema(description = "Ville de l'adresse", example = "Paris")
        String ville) {
}