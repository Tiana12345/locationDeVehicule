package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VoitureService {
    VoitureResponseDto ajouter(VoitureRequestDto voitureRequestDto) throws VehiculeException;

    VoitureResponseDto trouver(long id) throws EntityNotFoundException;

    List<VoitureResponseDto> trouverToutes();

    VoitureResponseDto modifierPartiellement(Long id, VoitureRequestDto voitureRequestDto) throws VehiculeException, EntityNotFoundException;

    VoitureResponseDto getVoitureResponseDto(Voiture voitureEnreg);

    void supprimer(Long id) throws EntityNotFoundException;

    List<VoitureResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreDePlaces, Carburant carburant, Integer nombreDePortes,
                                        String transmission, Boolean clim, Integer nombreDeBagages, String type, List<Permis> listePermis, Long tarifJournalier,
                                        Long kilometrage, Boolean actif, Boolean retireDuParc);
}
