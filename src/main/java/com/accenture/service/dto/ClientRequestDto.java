package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Permis;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Détails de la demande de client")
public record ClientRequestDto(
        @Schema(description = "Adresse email du client", example = "client@example.com")
        @NotBlank(message = "L'adresse mail est obligatoire")
        @Email(message = "L'adresse mail doit être valide")
        String mail,

        @Schema(description = "Mot de passe du client", example = "password123")
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, max = 16, message = "Le mot de passe doit contenir entre 8 et 16 caractères")
        String password,

        @Schema(description = "Nom du client", example = "Dupont")
        @NotBlank(message = "Renseignement obligatoire")
        String nom,

        @Schema(description = "Prénom du client", example = "Jean")
        @NotBlank(message = "Renseignement obligatoire")
        String prenom,

        @Schema(description = "Adresse du client")
        AdresseDto adresse,

        @Schema(description = "Date de naissance du client", example = "1990-01-01")
        @Past(message = "La date de naissance ne peut pas être présente ou future")
        LocalDate dateNaissance,

        @Schema(description = "Liste des permis du client")
        List<Permis> listePermis) {
}