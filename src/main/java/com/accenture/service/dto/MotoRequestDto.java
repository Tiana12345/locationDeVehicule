package com.accenture.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Schema(description = "Détails de la demande de moto")
public record MotoRequestDto(
        @Schema(description = "Marque de la moto", example = "Yamaha")
        @NotBlank
        String marque,

        @Schema(description = "Modèle de la moto", example = "MT-07")
        @NotBlank
        String modele,

        @Schema(description = "Couleur de la moto", example = "Noir")
        @NotBlank
        String couleur,

        @Schema(description = "Type de la moto", example = "Sportive")
        @NotBlank
        String type,

        @Schema(description = "Nombre de cylindres de la moto", example = "2")
        @NotNull
        Integer nombreCylindres,

        @Schema(description = "Poids de la moto", example = "180")
        @NotNull
        Integer poids,

        @Schema(description = "Puissance en kW de la moto", example = "55")
        @NotNull
        Integer puissanceEnkW,

        @Schema(description = "Hauteur de selle de la moto", example = "805")
        @NotNull
        Integer hauteurSelle,

        @Schema(description = "Transmission de la moto", example = "Manuelle")
        @NotBlank
        String transmission,

        @Schema(description = "Tarif journalier de la moto", example = "50")
        @NotNull
        long tarifJournalier,

        @Schema(description = "Kilométrage de la moto", example = "10000")
        @NotNull
        long kilometrage,

        @Schema(description = "Statut actif de la moto", example = "true")
        @NotNull
        Boolean actif,

        @Schema(description = "Statut de retrait du parc de la moto", example = "false")
        @NotNull
        Boolean retireDuParc) {
}