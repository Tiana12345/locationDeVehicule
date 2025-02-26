package com.accenture.controller;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.service.VoitureService;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Voitures", description = "Gestion des voitures")
public class VoitureController {

    private final VoitureService voitureService;
    private Logger logger = LoggerFactory.getLogger(VoitureController.class);

    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une nouvelle voiture", description = "Ajoute une nouvelle voiture à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voiture créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    ResponseEntity<Void> ajouter(@RequestBody @Valid VoitureRequestDto voitureRequestDto) {
        VoitureResponseDto voitureEnreg = voitureService.ajouter(voitureRequestDto);

        URI voiture = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.id())
                .toUri();
        return ResponseEntity.created(voiture).build();
    }

    @GetMapping
    @Operation(summary = "Obtenir toutes les voitures", description = "Récupère la liste de toutes les voitures disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des voitures récupérée avec succès")
    })
    List<VoitureResponseDto> voiture() {
        return voitureService.trouverToutes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une voiture par ID", description = "Récupère les détails d'une voiture spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voiture trouvée avec succès"),
            @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    })
    ResponseEntity<VoitureResponseDto> uneVoiture(@Parameter(description = "ID de la voiture à récupérer") @PathVariable("id") Long id) {
        VoitureResponseDto trouve = voitureService.trouver(id);
        return ResponseEntity.ok(trouve);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une voiture", description = "Supprime une voiture de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Voiture supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    })
    ResponseEntity<Void> supprimer(@Parameter(description = "ID de la voiture à supprimer") @PathVariable("id") Long id) {
        voitureService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement une voiture", description = "Met à jour partiellement les détails d'une voiture existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voiture mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Voiture non trouvée")
    })
    ResponseEntity<VoitureResponseDto> modifierPartiellement(@Parameter(description = "ID de la voiture à modifier") @PathVariable("id") Long id, @RequestBody VoitureRequestDto voitureRequestDto) {
        return ResponseEntity.ok(voitureService.modifierPartiellement(id, voitureRequestDto));
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
            @Parameter(description = "Liste des permis requis pour conduire la voiture") @RequestParam(required = false) List<Permis> listePermis,
            @Parameter(description = "Tarif journalier de la voiture") @RequestParam(required = false) Long tarifJournalier,
            @Parameter(description = "Kilométrage de la voiture") @RequestParam(required = false) Long kilometrage,
            @Parameter(description = "Statut actif de la voiture") @RequestParam(required = false) Boolean actif,
            @Parameter(description = "Statut de retrait du parc de la voiture") @RequestParam(required = false) Boolean retireDuParc
    ) {
        return voitureService.rechercher(id, marque, modele, couleur, nombreDePlaces, carburant, nombreDePortes, transmission, clim,
                nombreDeBagages, type, listePermis, tarifJournalier, kilometrage, actif, retireDuParc);
    }
}