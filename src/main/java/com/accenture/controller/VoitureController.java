package com.accenture.controller;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.service.VoitureService;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/voitures")
@Tag(name = "Voitures", description = "Gestion des voitures")
public class VoitureController {

    private final VoitureService voitureService;

    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une nouvelle voiture", description = "Ajoute une nouvelle voiture à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voiture créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    ResponseEntity<Void> ajouter(
            @Parameter(description = "Détails de la voiture à ajouter", required = false) @RequestBody @Valid VoitureRequestDto voitureRequestDto,

            @Parameter(description = "Marque de la voiture") @RequestParam(required = false) String marque,
            @Parameter(description = "Modèle de la voiture") @RequestParam(required = false) String modele,
            @Parameter(description = "Couleur de la voiture") @RequestParam(required = false) String couleur,
            @Parameter(description = "Nombre de places dans la voiture") @RequestParam(required = false) Integer nombreDePlaces,
            @Parameter(description = "Type de carburant de la voiture") @RequestParam(required = false) Carburant carburant,
            @Parameter(description = "Nombre de portes de la voiture") @RequestParam(required = false) Integer nombreDePortes,
            @Parameter(description = "Transmission de la voiture") @RequestParam(required = false) String transmission,
            @Parameter(description = "Climatisation de la voiture") @RequestParam(required = false) Boolean clim,
            @Parameter(description = "Nombre de bagages que la voiture peut contenir") @RequestParam(required = false) Integer nombreDeBagages,
            @Parameter(description = "Type de la voiture") @RequestParam(required = false) String type,
            @Parameter(description = "Tarif journalier de la voiture") @RequestParam(required = false) Long tarifJournalier,
            @Parameter(description = "Kilométrage de la voiture") @RequestParam(required = false) Long kilometrage,
            @Parameter(description = "Statut actif de la voiture") @RequestParam(required = false) Boolean actif,
            @Parameter(description = "Statut de retrait du parc de la voiture") @RequestParam(required = false) Boolean retireDuParc) {
        log.info("Ajout d'une nouvelle voiture : {}", voitureRequestDto);
        VoitureResponseDto voitureEnreg = voitureService.ajouter(voitureRequestDto);

        URI voiture = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.id())
                .toUri();
        log.info("Voiture ajoutée avec succès : {}", voitureEnreg);
        return ResponseEntity.created(voiture).build();
    }

    @GetMapping
    @Operation(summary = "Obtenir toutes les voitures", description = "Récupère la liste de toutes les voitures disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des voitures récupérée avec succès")
    })
    List<VoitureResponseDto> voiture() {
        log.info("Récupération de toutes les voitures");
        List<VoitureResponseDto> voitures = voitureService.trouverToutes();
        log.info("Nombre de voitures récupérées : {}", voitures.size());
        return voitures;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une voiture par ID", description = "Récupère les détails d'une voiture spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voiture trouvée avec succès"),
            @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    })
    ResponseEntity<VoitureResponseDto> uneVoiture(
            @Parameter(description = "ID de la voiture à récupérer", required = true) @PathVariable("id") Long id) {
        log.info("Récupération de la voiture avec ID : {}", id);
        VoitureResponseDto trouve = voitureService.trouver(id);
        if (trouve != null) {
            log.info("Voiture trouvée : {}", trouve);
            return ResponseEntity.ok(trouve);
        } else {
            log.error("Voiture non trouvée avec ID : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une voiture", description = "Supprime une voiture de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Voiture supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    })
    ResponseEntity<Void> supprimer(
            @Parameter(description = "ID de la voiture à supprimer", required = true) @PathVariable("id") Long id) {
        log.info("Suppression de la voiture avec ID : {}", id);
        voitureService.supprimer(id);
        log.info("Voiture supprimée avec succès : {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement une voiture", description = "Met à jour partiellement les détails d'une voiture existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voiture mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    })
    ResponseEntity<VoitureResponseDto> modifierPartiellement(
            @Parameter(description = "ID de la voiture à modifier", required = false) @PathVariable("id") Long id,
            @Parameter(description = "Détails de la voiture à modifier", required = false) @RequestBody VoitureRequestDto voitureRequestDto,
            @Parameter(description = "Marque de la voiture") @RequestParam(required = false) String marque,
            @Parameter(description = "Modèle de la voiture") @RequestParam(required = false) String modele,
            @Parameter(description = "Couleur de la voiture") @RequestParam(required = false) String couleur,
            @Parameter(description = "Nombre de places dans la voiture") @RequestParam(required = false) Integer nombreDePlaces,
            @Parameter(description = "Type de carburant de la voiture") @RequestParam(required = false) Carburant carburant,
            @Parameter(description = "Nombre de portes de la voiture") @RequestParam(required = false) Integer nombreDePortes,
            @Parameter(description = "Transmission de la voiture") @RequestParam(required = false) String transmission,
            @Parameter(description = "Climatisation de la voiture") @RequestParam(required = false) Boolean clim) {
        log.info("Modification partielle de la voiture avec ID : {}", id);
        VoitureResponseDto voitureModifiee = voitureService.modifierPartiellement(id, voitureRequestDto);
        log.info("Voiture modifiée avec succès : {}", voitureModifiee);
        return ResponseEntity.ok(voitureModifiee);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des voitures", description = "Recherche des voitures en fonction de différents critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    })
    List<VoitureResponseDto> recherche(
            @Parameter(description = "ID de la voiture") @RequestParam(required = false) Long id,
            @Parameter(description = "Marque de la voiture") @RequestParam(required = false) String marque,
            @Parameter(description = "Modèle de la voiture") @RequestParam(required = false) String modele,
            @Parameter(description = "Couleur de la voiture") @RequestParam(required = false) String couleur,
            @Parameter(description = "Nombre de places dans la voiture") @RequestParam(required = false) Integer nombreDePlaces,
            @Parameter(description = "Type de carburant de la voiture") @RequestParam(required = false) Carburant carburant,
            @Parameter(description = "Nombre de portes de la voiture") @RequestParam(required = false) Integer nombreDePortes,
            @Parameter(description = "Transmission de la voiture") @RequestParam(required = false) String transmission,
            @Parameter(description = "Climatisation de la voiture") @RequestParam(required = false) Boolean clim,
            @Parameter(description = "Nombre de bagages que la voiture peut contenir") @RequestParam(required = false) Integer nombreDeBagages,
            @Parameter(description = "Type de la voiture") @RequestParam(required = false) String type,
            @Parameter(description = "Permis requis pour conduire la voiture") @RequestParam(required = false) Permis permis,
            @Parameter(description = "Tarif journalier de la voiture") @RequestParam(required = false) Long tarifJournalier,
            @Parameter(description = "Kilométrage de la voiture") @RequestParam(required = false) Long kilometrage,
            @Parameter(description = "Statut actif de la voiture") @RequestParam(required = false) Boolean actif,
            @Parameter(description = "Statut de retrait du parc de la voiture") @RequestParam(required = false) Boolean retireDuParc
    ) {
        log.info("Recherche de voitures avec les critères : id={}, marque={}, modele={}, couleur={}, nombreDePlaces={}, carburant={}, nombreDePortes={}, transmission={}, clim={}, nombreDeBagages={}, type={}, permis={}, tarifJournalier={}, kilometrage={}, actif={}, retireDuParc={}",
                id, marque, modele, couleur, nombreDePlaces, carburant, nombreDePortes, transmission, clim, nombreDeBagages, type, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        List<VoitureResponseDto> voitures = voitureService.rechercher(id, marque, modele, couleur, nombreDePlaces, carburant, nombreDePortes, transmission, clim,
                nombreDeBagages, type, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        log.info("Nombre de voitures trouvées : {}", voitures.size());
        return voitures;
    }
}