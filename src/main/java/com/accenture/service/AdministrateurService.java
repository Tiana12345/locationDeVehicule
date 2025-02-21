package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
import org.hibernate.action.internal.EntityActionVetoException;

public interface AdministrateurService {
    AdministrateurResponseDto ajouter(AdministrateurRequestDto administrateurRequestDto) throws UtilisateurException;

    void supprimer(String mail) throws EntityActionVetoException;
}
