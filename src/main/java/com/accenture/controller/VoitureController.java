package com.accenture.controller;

import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import com.accenture.service.VoitureService;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/voitures")
public class VoitureController {

    private final VoitureService voitureService;
    private Logger logger = LoggerFactory.getLogger(VoitureController.class);

    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }

    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody @Valid VoitureRequestDto voitureRequestDto) {
        logger.info("Ajout d'une nouvelle voiture : {}", voitureRequestDto);
        VoitureResponseDto voitureEnreg = voitureService.ajouter(voitureRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    List<VoitureResponseDto> voiture() {
        return voitureService.trouverToutes();
    }

    @GetMapping("/{id}")
    ResponseEntity<VoitureResponseDto> uneVoiture(@PathVariable("id") Long id) {
        VoitureResponseDto trouve = voitureService.trouver(id);
        return ResponseEntity.ok(trouve);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> supprimer(@PathVariable("id") Long id) {
        voitureService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<VoitureResponseDto> modifier(@PathVariable("id") Long id, @RequestBody @Valid VoitureRequestDto voitureRequestDto) {
        VoitureResponseDto reponse = voitureService.modifier(id, voitureRequestDto);
        return ResponseEntity.ok(reponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<VoitureResponseDto> modifierPartiellement(@PathVariable("id") Long id, @RequestBody VoitureRequestDto voitureRequestDto) {
        return ResponseEntity.ok(voitureService.modifierPartiellement(id, voitureRequestDto));
    }

    @GetMapping("(/search)")
    List<VoitureResponseDto> recherche(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String marque,
            @RequestParam(required = false) String modele,
            @RequestParam(required = false) String couleur,
            @RequestParam(required = false) int nombreDePlaces,
            @RequestParam(required = false) Carburant carburant,
            @RequestParam(required = false) int nombreDePortes,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) Boolean clim,
            @RequestParam(required = false) int nombreDeBagages,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) List<Permis> listePermis,
            @RequestParam(required = false) Long tarifJournalier,
            @RequestParam(required = false) Long kilometrage,
            @RequestParam(required = false) Boolean actif,
            @RequestParam(required = false) Boolean retireDuParc
    ) {
        return voitureService.rechercher(id, marque, modele, couleur, nombreDePlaces, carburant, nombreDePortes, transmission, clim,
                nombreDeBagages, type, listePermis, tarifJournalier, kilometrage, actif, retireDuParc);
    }
}
