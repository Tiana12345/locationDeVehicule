package com.accenture.service.dto;


import com.accenture.model.param.TypeVehiculeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Détails du véhicule")
public record VehiculeDto(
        @Schema(description = "ID du véhicule", example = "1")
        long id,

        @Schema(description = "Type de véhicule", example = "Voiture")
        @NotBlank(message = "Le type du véhicule est obligatoire")
        TypeVehiculeEnum typeVehiculeEnum,

        @Schema(description = "Marque du véhicule", example = "Toyota")
        @NotBlank(message = "La marque du véhicule est obligatoire")
        String marque,

        @Schema(description = "Modèle du véhicule", example = "Corolla")
        @NotBlank(message = "Le modèle du véhicule est obligatoire")
        String modele,

        @Schema(description = "Couleur du véhicule", example = "Rouge")
        String couleur,

        @Schema(description = "Type de véhicule", example = "Voiture")
        @NotBlank(message = "Le type de véhicule est obligatoire")
        String type,

        @Schema(description = "Tarif journalier", example = "50")
        long tarifJournalier,

        @Schema(description = "Kilométrage", example = "20000")
        long kilometrage,

        @Schema(description = "Actif", example = "true")
        Boolean actif,

        @Schema(description = "Retiré du parc", example = "false")
        Boolean retireDuParc) {
}