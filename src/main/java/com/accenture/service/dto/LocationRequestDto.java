package com.accenture.service.dto;
import java.time.LocalDate;
import java.util.List;

import com.accenture.model.param.Accessoires;
import com.accenture.model.param.Etat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Détails de la demande de location")
public record LocationRequestDto(

        @Schema(description = "Email du client", example = "client@example.com")
        @NotNull(message = "L'email du client est obligatoire")
        String clientMail,

        @Schema(description = "Détails du véhicule")
        @NotNull(message = "Les détails du véhicule sont obligatoires")
        Long vehiculeId,

        @Schema(description = "Accessoire de la location")
        List<Accessoires> accessoires,

        @Schema(description = "Date de début de la location", example = "2025-03-01")
        @NotNull(message = "La date de début est obligatoire")
        LocalDate dateDebut,

        @Schema(description = "Date de fin de la location", example = "2025-03-10")
        LocalDate dateFin,

        @Schema(description = "Kilomètres parcourus", example = "100")
        int kilometresParcourus,

        @Schema(description = "Montant total de la location", example = "500")
        int montantTotal,

        @Schema(description = "État de la location")
        @NotNull(message = "L'état de la location est obligatoire")
        Etat etat) {
}