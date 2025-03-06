package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.model.param.Permis;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {


    ClientResponseDto ajouter(ClientRequestDto clientRequestDto) throws UtilisateurException;

    ClientResponseDto trouver(String mail) throws EntityNotFoundException;

    List<ClientResponseDto> trouverTous();

    ClientResponseDto modifierPartiellement(String mail, ClientRequestDto clientRequestDto) throws UtilisateurException, EntityNotFoundException;

    void supprimer(String mail) throws EntityNotFoundException;

    List<ClientResponseDto> rechercher(String mail, String prenom, String nom, LocalDate dateNaissance, String rue, String codePostal,
                                       String ville, Boolean desactive, List<Permis> listePermis, LocalDate dateInscription);
}
