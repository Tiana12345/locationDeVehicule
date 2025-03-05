package com.accenture.service.dto;

import com.accenture.model.param.Permis;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détails de la moto")
public record MotoResponseDto(
        @Schema(description = "ID de la moto", example = "1")
        long id,

        @Schema(description = "Marque de la moto", example = "Yamaha")
        String marque,

        @Schema(description = "Modèle de la moto", example = "MT-07")
        String modele,

        @Schema(description = "Couleur de la moto", example = "Noir")
        String couleur,

        @Schema(description = "Type de la moto", example = "Sportive")
        String type,

        @Schema(description = "Nombre de cylindres de la moto", example = "2")
        Integer nombreCylindres,

        @Schema(description = "Poids de la moto", example = "180")
        Integer poids,

        @Schema(description = "Puissance en kW de la moto", example = "55")
        Integer puissanceEnkW,

        @Schema(description = "Hauteur de selle de la moto", example = "805")
        Integer hauteurSelle,

        @Schema(description = "Transmission de la moto", example = "Manuelle")
        String transmission,

        @Schema(description = "Liste des permis requis pour conduire la moto")
        Permis permis,

        @Schema(description = "Tarif journalier de la moto", example = "50")
        long tarifJournalier,

        @Schema(description = "Kilométrage de la moto", example = "10000")
        long kilometrage,

        @Schema(description = "Statut actif de la moto", example = "true")
        Boolean actif,

        @Schema(description = "Statut de retrait du parc de la moto", example = "false")
        Boolean retireDuParc) {
}