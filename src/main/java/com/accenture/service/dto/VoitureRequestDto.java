package com.accenture.service.dto;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Détails de la demande de voiture")
public record VoitureRequestDto(
        @Schema(description = "Marque de la voiture", example = "Toyota")
        @NotBlank
        String marque,

        @Schema(description = "Modèle de la voiture", example = "Aygo")
        @NotBlank
        String modele,

        @Schema(description = "Couleur de la voiture", example = "Gris")
        @NotBlank
        String couleur,

        @Schema(description = "Type de la voiture", example = "Voiture de luxe")
        @NotBlank
        String type,

        @Schema(description = "Nombre de places dans la voiture", example = "5")
        @NotNull
        Integer nombreDePlaces,

        @Schema(description = "Nombre de portes de la voiture", example = "3")
        @NotNull
        Integer nombreDePortes,

        @Schema(description = "Transmission de la voiture", example = "auto")
        @NotBlank
        String transmission,

        @Schema(description = "Climatisation de la voiture", example = "true")
        @NotNull
        Boolean clim,

        @Schema(description = "Nombre de bagages que la voiture peut contenir", example = "5")
        @NotNull
        Integer nombreDeBagages,

        @Schema(description = "Type de carburant de la voiture", example = "ESSENCE")
        Carburant carburant,

        @Schema(description = "Tarif journalier de la voiture", example = "1000")
        @NotNull
        long tarifJournalier,

        @Schema(description = "Kilométrage de la voiture", example = "100000")
        @NotNull
        long kilometrage,

        @Schema(description = "Statut actif de la voiture", example = "true")
        @NotNull
        Boolean actif,

        @Schema(description = "Statut de retrait du parc de la voiture", example = "false")
        @NotNull
        Boolean retireDuParc) {
}