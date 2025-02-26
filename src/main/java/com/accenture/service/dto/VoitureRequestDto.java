package com.accenture.service.dto;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VoitureRequestDto(
        @NotBlank
        String marque,
        @NotBlank
        String modele,
        @NotBlank
        String couleur,
        @NotBlank
        String type,
        @NotNull
        Integer nombreDePlaces,
        @NotNull
        Integer nombreDePortes,
        @NotBlank
        String transmission,
        @NotNull
        Boolean clim,
        @NotNull
        Integer nombreDeBagages,
        List<Permis> listePermis,
        Carburant carburant,
        @NotNull
        long tarifJournalier,
        @NotNull
        long kilometrage,
        @NotNull
        Boolean actif,
        @NotNull
        Boolean retireDuParc)
        {
}
