package com.accenture.service.dto;

import com.accenture.model.param.Permis;
import com.accenture.repository.entity.Adresse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Détails de la réponse de client")
public record ClientResponseDto(
        @Schema(description = "Adresse email du client", example = "client@example.com")
        String mail,

        @Schema(description = "Mot de passe du client", example = "password123")
        String password,

        @Schema(description = "Nom du client", example = "Dupont")
        String nom,

        @Schema(description = "Prénom du client", example = "Jean")
        String prenom,

        @Schema(description = "Adresse du client")
        Adresse adresse,

        @Schema(description = "Date de naissance du client", example = "1990-01-01")
        LocalDate dateNaissance,

        @Schema(description = "Date d'inscription du client", example = "2023-01-01")
        LocalDate dateInscription,

        @Schema(description = "Liste des permis du client")
        List<Permis> listePermis,

        @Schema(description = "Statut de désactivation du client", example = "false")
        Boolean desactive) {
}