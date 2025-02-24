package com.accenture.repository;

import com.accenture.repository.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdministrateurDao extends JpaRepository<Administrateur, String> {

    Optional<Administrateur> findByMailContaining(String mail) ;

    List<Administrateur> findByPrenomContaining(String prenom) ;

    List<Administrateur> findByNomContaining(String nom) ;

    List<Administrateur> findByFonctionContaining(String fonction) ;


}
