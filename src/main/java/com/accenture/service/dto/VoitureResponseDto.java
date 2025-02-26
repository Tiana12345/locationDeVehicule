package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;

import java.util.List;

public record VoitureResponseDto(  long id,
                                   String marque,
                                   String modele,
                                   String couleur,
                                   String type,
                                   Integer nombreDePlaces,
                                   Integer nombreDePortes,
                                   String transmission,
                                   Boolean clim,
                                   Integer nombreDeBagages,
                                   List<Permis> listePermis,
                                   Carburant carburant,
                                   long tarifJournalier,
                                   long kilometrage,
                                   Boolean actif,
                                   Boolean retireDuParc) {


}
