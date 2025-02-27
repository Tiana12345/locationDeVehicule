package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.entity.Utilitaire;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface UtilitaireService {
    UtilitaireResponseDto ajouter(UtilitaireRequestDto utilitaireRequestDto) throws VehiculeException;

    UtilitaireResponseDto trouver(long id) throws EntityNotFoundException;

    List<UtilitaireResponseDto> trouverToutes();

    UtilitaireResponseDto modifierPartiellement(Long id, UtilitaireRequestDto utilitaireRequestDto) throws VehiculeException, EntityNotFoundException;

    UtilitaireResponseDto getUtilitaireResponseDto(Utilitaire utilitaireEnreg);

    void supprimer(Long id) throws EntityNotFoundException;

    List<UtilitaireResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreDePlace,
                                           Carburant carburant, String transmission, Boolean clim, Integer chargeMax,
                                           Integer poidsPATC, Integer capaciteM3, List<Permis> listePermis, Long tarifJournalier,
                                           Long kilometrage, Boolean actif, Boolean retireDuParc);
}
