package com.accenture.repository;

import com.accenture.model.Permis;
import com.accenture.repository.entity.Adresse;
import com.accenture.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientDao extends JpaRepository<Client, String> {

    Optional<Client> findByMailContaining(String mail);

    List<Client> findByPrenomContaining(String prenom);

    List<Client> findBynomContaining(String nom);

    List<Client> findByDateNaissance(LocalDate dateNaissance);

    List<Client> findByAdresse_RueContaining(String rue);
    List<Client> findByAdresse_CodePostalContaining(String codePostal);
    List<Client> findByAdresse_VilleContaining(String ville);



    List<Client> findByDesactive(Boolean desactive);

    List<Client> findByDateInscription(LocalDate dateInscription);

    List<Client> findByListePermisContaining(Permis permis);
}
