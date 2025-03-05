package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.param.Permis;
import com.accenture.repository.entity.Moto;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface MotoService {
    MotoResponseDto ajouter(MotoRequestDto motoRequestDto) throws VehiculeException;

    MotoResponseDto trouver(long id) throws EntityNotFoundException;

    List<MotoResponseDto> trouverToutes();

    MotoResponseDto modifierPartiellement(Long id, MotoRequestDto motoRequestDto) throws VehiculeException, EntityNotFoundException;

    MotoResponseDto getMotoResponseDto(Moto motoEnreg);

    void supprimer(Long id) throws EntityNotFoundException;

    List<MotoResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreCylindres,
                                     Integer poids, Integer puissanceEnkW, Integer hauteurSelle, String transmission,
                                     Permis permis, Long tarifJournalier, Long kilometrage, Boolean actif,
                                     Boolean retireDuParc);
}
