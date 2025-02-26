package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Détails de la voiture")
public record VoitureResponseDto(
        @Schema(description = "ID de la voiture", example = "1")
        long id,

        @Schema(description = "Marque de la voiture", example = "Toyota")
        String marque,

        @Schema(description = "Modèle de la voiture", example = "Aygo")
        String modele,

        @Schema(description = "Couleur de la voiture", example = "Gris")
        String couleur,

        @Schema(description = "Type de la voiture", example = "Voiture de luxe")
        String type,

        @Schema(description = "Nombre de places dans la voiture", example = "5")
        Integer nombreDePlaces,

        @Schema(description = "Nombre de portes de la voiture", example = "3")
        Integer nombreDePortes,

        @Schema(description = "Transmission de la voiture", example = "auto")
        String transmission,

        @Schema(description = "Climatisation de la voiture", example = "true")
        Boolean clim,

        @Schema(description = "Nombre de bagages que la voiture peut contenir", example = "5")
        Integer nombreDeBagages,

        @Schema(description = "Liste des permis requis pour conduire la voiture")
        List<Permis> listePermis,

        @Schema(description = "Type de carburant de la voiture", example = "ESSENCE")
        Carburant carburant,

        @Schema(description = "Tarif journalier de la voiture", example = "1000")
        long tarifJournalier,

        @Schema(description = "Kilométrage de la voiture", example = "100000")
        long kilometrage,

        @Schema(description = "Statut actif de la voiture", example = "true")
        Boolean actif,

        @Schema(description = "Statut de retrait du parc de la voiture", example = "false")
        Boolean retireDuParc) {
}