package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Détails de la demande d'utilitaire")
public record UtilitaireRequestDto(
        @Schema(description = "Marque de l'utilitaire", example = "Renault")
        @NotBlank
        String marque,

        @Schema(description = "Modèle de l'utilitaire", example = "Master")
        @NotBlank
        String modele,

        @Schema(description = "Couleur de l'utilitaire", example = "Blanc")
        @NotBlank
        String couleur,

        @Schema(description = "Type de l'utilitaire", example = "Fourgon")
        @NotBlank
        String type,

        @Schema(description = "Nombre de places dans l'utilitaire", example = "3")
        @NotNull
        Integer nombreDePlace,

        @Schema(description = "Type de carburant de l'utilitaire", example = "DIESEL")
        @NotNull
        Carburant carburant,

        @Schema(description = "Transmission de l'utilitaire", example = "Manuelle")
        @NotBlank
        String transmission,

        @Schema(description = "Climatisation de l'utilitaire", example = "true")
        @NotNull
        Boolean clim,

        @Schema(description = "Charge maximale de l'utilitaire en kg", example = "1500")
        @NotNull
        Integer chargeMax,

        @Schema(description = "Poids total autorisé en charge (PTAC) de l'utilitaire en kg", example = "3500")
        @NotNull
        Integer poidsPATC,

        @Schema(description = "Capacité en m3 de l'utilitaire", example = "12")
        @NotNull
        Integer capaciteM3,

        @Schema(description = "Liste des permis requis pour conduire l'utilitaire")
        List<Permis> listePermis,

        @Schema(description = "Tarif journalier de l'utilitaire", example = "80")
        @NotNull
        long tarifJournalier,

        @Schema(description = "Kilométrage de l'utilitaire", example = "50000")
        @NotNull
        long kilometrage,

        @Schema(description = "Statut actif de l'utilitaire", example = "true")
        @NotNull
        Boolean actif,

        @Schema(description = "Statut de retrait du parc de l'utilitaire", example = "false")
        @NotNull
        Boolean retireDuParc) {
}