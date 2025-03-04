package com.accenture.service.dto;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Détails de l'utilitaire")
public record UtilitaireResponseDto(
        @Schema(description = "ID de l'utilitaire", example = "1")
        long id,

        @Schema(description = "Marque de l'utilitaire", example = "Renault")
        String marque,

        @Schema(description = "Modèle de l'utilitaire", example = "Master")
        String modele,

        @Schema(description = "Couleur de l'utilitaire", example = "Blanc")
        String couleur,

        @Schema(description = "Type de l'utilitaire", example = "Fourgon")
        String type,

        @Schema(description = "Nombre de places dans l'utilitaire", example = "3")
        Integer nombreDePlace,

        @Schema(description = "Type de carburant de l'utilitaire", example = "DIESEL")
        Carburant carburant,

        @Schema(description = "Transmission de l'utilitaire", example = "Manuelle")
        String transmission,

        @Schema(description = "Climatisation de l'utilitaire", example = "true")
        Boolean clim,

        @Schema(description = "Charge maximale de l'utilitaire en kg", example = "1500")
        Integer chargeMax,

        @Schema(description = "Poids total autorisé en charge (PTAC) de l'utilitaire en kg", example = "3500")
        Integer poidsPATC,

        @Schema(description = "Capacité en m3 de l'utilitaire", example = "12")
        Integer capaciteM3,

        @Schema(description = "Liste des permis requis pour conduire l'utilitaire")
        List<Permis> listePermis,

        @Schema(description = "Tarif journalier de l'utilitaire", example = "80")
        long tarifJournalier,

        @Schema(description = "Kilométrage de l'utilitaire", example = "50000")
        long kilometrage,

        @Schema(description = "Statut actif de l'utilitaire", example = "true")
        Boolean actif,

        @Schema(description = "Statut de retrait du parc de l'utilitaire", example = "false")
        Boolean retireDuParc) {
}