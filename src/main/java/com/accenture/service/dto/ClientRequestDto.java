package com.accenture.service.dto;

import com.accenture.model.Permis;
import com.accenture.repository.entity.Adresse;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record ClientRequestDto(
        @NotBlank(message = "renseignement obligatoire")
        String mail,
        @NotBlank(message = "renseignement obligatoire")
        String password,
        @NotBlank(message = "renseignement obligatoire")
        String nom,
        @NotBlank(message = "renseignement obligatoire")
        String prenom,
        AdresseDto adresse,
        @Past(message = "La date de naissance ne peut pas être présente ou future")
        LocalDate dateNaissance,
        @FutureOrPresent(message = "La date d'inscription ne peut pas être passée")
        LocalDate dateInscription,

        List<Permis> listePermis,
        @NotNull(message = "Cette information est obligatoire")
        Boolean desactive) {
}
