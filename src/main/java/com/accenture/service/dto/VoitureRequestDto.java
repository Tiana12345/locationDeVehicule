package com.accenture.service.dto;

import com.accenture.model.Carburant;
import com.accenture.model.Permis;

import java.util.List;

public record VoitureRequestDto(long id,
                                String marque,
                                String modele,
                                String couleur,
                                String type,
                                int nombreDePlaces,
                                int nombreDePortes,
                                String transmission,
                                Boolean clim,
                                int nombreDeBagages,
                                List<Permis> listePermis,
                                Carburant carburant,
                                long tarifJournalier,
                                long kilometrage,
                                Boolean actif,
                                Boolean retireDuParc) {
}
