package com.accenture.service.dto;

import com.accenture.model.param.Accessoires;
import com.accenture.model.param.Etat;

import com.accenture.repository.entity.Client;
import com.accenture.repository.entity.Vehicule;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record LocationResponseDto(
        @Schema(description = "ID de la location", example = "1")
        int id,

        @Schema(description = "Détails du véhicule")
        Vehicule vehicule,

        @Schema(description = "Détails du client")
        Client client,

        @Schema(description = "Accessoire de la location")
        Accessoires accessoires,

        @Schema(description = "Date de début de la location", example = "2025-03-01")
        LocalDate dateDebut,

        @Schema(description = "Date de fin de la location", example = "2025-03-10")
        LocalDate dateFin,

        @Schema(description = "Kilomètres parcourus", example = "100")
        int kilometresParcourus,

        @Schema(description = "Montant total de la location", example = "500")
        int montantTotal,

        @Schema(description = "Date de validation de la location", example = "2025-03-05")
        LocalDate dateValidation,

        @Schema(description = "État de la location")
        Etat etat) {
}
