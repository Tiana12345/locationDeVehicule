package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;

import java.util.List;

public interface AdministrateurService {
    AdministrateurResponseDto ajouter(AdministrateurRequestDto administrateurRequestDto) throws UtilisateurException;

    void supprimer(String mail) throws EntityActionVetoException;

    AdministrateurResponseDto modifierPartiellement(String mail, AdministrateurRequestDto administrateurRequestDto) throws UtilisateurException, EntityNotFoundException;

    List<AdministrateurResponseDto> rechercher(String mail, String prenom, String nom, String fonction );
}
