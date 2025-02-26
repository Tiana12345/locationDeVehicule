package com.accenture.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Détails de la demande de vélo")
public record VeloRequestDto(
        @Schema(description = "Marque du vélo", example = "Giant")
        @NotBlank
        String marque,

        @Schema(description = "Modèle du vélo", example = "Defy Advanced")
        @NotBlank
        String modele,

        @Schema(description = "Couleur du vélo", example = "Rouge")
        @NotBlank
        String couleur,

        @Schema(description = "Type du vélo", example = "Route")
        @NotBlank
        String type,

        @Schema(description = "Taille du cadre du vélo", example = "54")
        @NotNull
        Integer tailleCadre,

        @Schema(description = "Poids du vélo", example = "8")
        @NotNull
        Integer poids,

        @Schema(description = "Le vélo est-il électrique ?", example = "true")
        @NotNull
        Boolean electrique,

        @Schema(description = "Capacité de la batterie du vélo en Wh", example = "500")
        @NotNull
        Integer capaciteBatterie,

        @Schema(description = "Autonomie du vélo en km", example = "100")
        @NotNull
        Integer autonomie,

        @Schema(description = "Le vélo a-t-il des freins à disque ?", example = "true")
        @NotNull
        Boolean freinsADisque,

        @Schema(description = "Tarif journalier du vélo", example = "20")
        @NotNull
        long tarifJournalier,

        @Schema(description = "Kilométrage du vélo", example = "500")
        @NotNull
        long kilometrage,

        @Schema(description = "Statut actif du vélo", example = "true")
        @NotNull
        Boolean actif,

        @Schema(description = "Statut de retrait du parc du vélo", example = "false")
        @NotNull
        Boolean retireDuParc) {
}