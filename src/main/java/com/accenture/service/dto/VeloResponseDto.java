package com.accenture.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détails du vélo")
public record VeloResponseDto(
        @Schema(description = "ID du vélo", example = "1")
        long id,

        @Schema(description = "Marque du vélo", example = "Giant")
        String marque,

        @Schema(description = "Modèle du vélo", example = "Defy Advanced")
        String modele,

        @Schema(description = "Couleur du vélo", example = "Rouge")
        String couleur,

        @Schema(description = "Type du vélo", example = "Route")
        String type,

        @Schema(description = "Taille du cadre du vélo", example = "54")
        Integer tailleCadre,

        @Schema(description = "Poids du vélo", example = "8")
        Integer poids,

        @Schema(description = "Le vélo est-il électrique ?", example = "true")
        Boolean electrique,

        @Schema(description = "Capacité de la batterie du vélo en Wh", example = "500")
        Integer capaciteBatterie,

        @Schema(description = "Autonomie du vélo en km", example = "100")
        Integer autonomie,

        @Schema(description = "Le vélo a-t-il des freins à disque ?", example = "true")
        Boolean freinsADisque,

        @Schema(description = "Tarif journalier du vélo", example = "20")
        long tarifJournalier,

        @Schema(description = "Kilométrage du vélo", example = "500")
        long kilometrage,

        @Schema(description = "Statut actif du vélo", example = "true")
        Boolean actif,

        @Schema(description = "Statut de retrait du parc du vélo", example = "false")
        Boolean retireDuParc) {
}