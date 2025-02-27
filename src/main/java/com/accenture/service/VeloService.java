package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.repository.entity.Velo;
import com.accenture.service.dto.VeloRequestDto;
import com.accenture.service.dto.VeloResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VeloService {
    VeloResponseDto ajouter(VeloRequestDto veloRequestDto) throws VehiculeException;

    VeloResponseDto trouver(long id) throws EntityNotFoundException;

    List<VeloResponseDto> trouverToutes();

    VeloResponseDto modifierPartiellement(Long id, VeloRequestDto veloRequestDto) throws VehiculeException, EntityNotFoundException;

    VeloResponseDto getVeloResponseDto(Velo veloEnreg);

    void supprimer(Long id) throws EntityNotFoundException;

    List<VeloResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer tailleCadre,
                                     Integer poids, Boolean electrique, Integer capaciteBatterie, Integer autonomie,
                                     Boolean freinsADisque, Long tarifJournalier, Long kilometrage, Boolean actif,
                                     Boolean retireDuParc);
}
