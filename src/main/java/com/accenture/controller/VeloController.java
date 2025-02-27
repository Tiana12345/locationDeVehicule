package com.accenture.controller;

import com.accenture.service.VeloService;
import com.accenture.service.dto.VeloRequestDto;
import com.accenture.service.dto.VeloResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/velos")
@Tag(name = "Velos", description = "Gestion des velos")
public class VeloController {

    private final VeloService veloService;

    public VeloController(VeloService veloService) {
        this.veloService = veloService;
    }

    @Operation(summary = "Ajouter un nouveau vélo")
    @PostMapping
    public ResponseEntity<VeloResponseDto> ajouter(@RequestBody VeloRequestDto veloRequestDto) {
        VeloResponseDto veloResponseDto = veloService.ajouter(veloRequestDto);
        return ResponseEntity.ok(veloResponseDto);
    }

    @Operation(summary = "Trouver un vélo par son identifiant")
    @GetMapping("/{id}")
    public ResponseEntity<VeloResponseDto> trouver(@Parameter(description = "Identifiant du vélo") @PathVariable long id) {
        VeloResponseDto veloResponseDto = veloService.trouver(id);
        return ResponseEntity.ok(veloResponseDto);
    }

    @Operation(summary = "Trouver tous les vélos")
    @GetMapping
    public ResponseEntity<List<VeloResponseDto>> trouverToutes() {
        List<VeloResponseDto> velos = veloService.trouverToutes();
        return ResponseEntity.ok(velos);
    }

    @Operation(summary = "Modifier partiellement un vélo")
    @PatchMapping("/{id}")
    public ResponseEntity<VeloResponseDto> modifierPartiellement(@Parameter(description = "Identifiant du vélo") @PathVariable Long id, @RequestBody VeloRequestDto veloRequestDto) {
        VeloResponseDto veloResponseDto = veloService.modifierPartiellement(id, veloRequestDto);
        return ResponseEntity.ok(veloResponseDto);
    }

    @Operation(summary = "Supprimer un vélo par son identifiant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@Parameter(description = "Identifiant du vélo") @PathVariable Long id) {
        veloService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Rechercher des vélos par critères")
    @GetMapping("/rechercher")
    public ResponseEntity<List<VeloResponseDto>> rechercher(
            @Parameter(description = "Identifiant du vélo") @RequestParam(required = false) Long id,
            @Parameter(description = "Marque du vélo") @RequestParam(required = false) String marque,
            @Parameter(description = "Modèle du vélo") @RequestParam(required = false) String modele,
            @Parameter(description = "Couleur du vélo") @RequestParam(required = false) String couleur,
            @Parameter(description = "Taille du cadre du vélo") @RequestParam(required = false) Integer tailleCadre,
            @Parameter(description = "Poids du vélo") @RequestParam(required = false) Integer poids,
            @Parameter(description = "Le vélo est-il électrique") @RequestParam(required = false) Boolean electrique,
            @Parameter(description = "Capacité de la batterie du vélo") @RequestParam(required = false) Integer capaciteBatterie,
            @Parameter(description = "Autonomie du vélo") @RequestParam(required = false) Integer autonomie,
            @Parameter(description = "Le vélo a-t-il des freins à disque") @RequestParam(required = false) Boolean freinsADisque,
            @Parameter(description = "Tarif journalier du vélo") @RequestParam(required = false) Long tarifJournalier,
            @Parameter(description = "Kilométrage du vélo") @RequestParam(required = false) Long kilometrage,
            @Parameter(description = "Le vélo est-il actif") @RequestParam(required = false) Boolean actif,
            @Parameter(description = "Le vélo est-il retiré du parc") @RequestParam(required = false) Boolean retireDuParc) {
        List<VeloResponseDto> velos = veloService.rechercher(id, marque, modele, couleur, tailleCadre, poids, electrique, capaciteBatterie, autonomie, freinsADisque, tarifJournalier, kilometrage, actif, retireDuParc);
        return ResponseEntity.ok(velos);
    }
}