package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Permis;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ClientRequestDto(
        @NotBlank(message = "L'adresse mail est obligatoire")
        @Email(message = "l'adresse mail doit être valide")
        String mail,
        @NotBlank(message = "Le mot e passe est obligatoire")
        @Size(min = 8, max = 16, message = "Le mot de passe doit contenir entre 8 et 16 caractères")
        String password,
        @NotBlank(message = "renseignement obligatoire")
        String nom,
        @NotBlank(message = "renseignement obligatoire")
        String prenom,
        AdresseDto adresse,
        @Past(message = "La date de naissance ne peut pas être présente ou future")
        LocalDate dateNaissance,
        List<Permis> listePermis
) {
}
