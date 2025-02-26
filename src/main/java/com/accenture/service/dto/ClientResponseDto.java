package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.entity.Adresse;

import java.time.LocalDate;
import java.util.List;

public record ClientResponseDto
        (String mail,
         String password,
         String nom,
         String prenom,
         Adresse adresse,
         LocalDate dateNaissance,
         LocalDate dateInscription,
         List<Permis> listePermis,
         Boolean desactive) {

}
