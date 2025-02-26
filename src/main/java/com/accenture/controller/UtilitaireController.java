package com.accenture.controller;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.service.UtilitaireService;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilitaires")
@Tag(name = "Utilitaires", description = "Gestion des utilitaires")
public class UtilitaireController {

    private final UtilitaireService utilitaireService;

        public UtilitaireController(UtilitaireService utilitaireService) {
            this.utilitaireService = utilitaireService;
        }

        @Operation(summary = "Ajouter un nouvel utilitaire")
        @PostMapping
        public ResponseEntity<UtilitaireResponseDto> ajouter(@RequestBody UtilitaireRequestDto utilitaireRequestDto) {
            UtilitaireResponseDto utilitaireResponseDto = utilitaireService.ajouter(utilitaireRequestDto);
            return ResponseEntity.ok(utilitaireResponseDto);
        }

        @Operation(summary = "Trouver un utilitaire par son identifiant")
        @GetMapping("/{id}")
        public ResponseEntity<UtilitaireResponseDto> trouver(@Parameter(description = "Identifiant de l'utilitaire") @PathVariable long id) {
            UtilitaireResponseDto utilitaireResponseDto = utilitaireService.trouver(id);
            return ResponseEntity.ok(utilitaireResponseDto);
        }

        @Operation(summary = "Trouver tous les utilitaires")
        @GetMapping
        public ResponseEntity<List<UtilitaireResponseDto>> trouverToutes() {
            List<UtilitaireResponseDto> utilitaires = utilitaireService.trouverToutes();
            return ResponseEntity.ok(utilitaires);
        }

        @Operation(summary = "Modifier partiellement un utilitaire")
        @PatchMapping("/{id}")
        public ResponseEntity<UtilitaireResponseDto> modifierPartiellement(@Parameter(description = "Identifiant de l'utilitaire") @PathVariable Long id, @RequestBody UtilitaireRequestDto utilitaireRequestDto) {
            UtilitaireResponseDto utilitaireResponseDto = utilitaireService.modifierPartiellement(id, utilitaireRequestDto);
            return ResponseEntity.ok(utilitaireResponseDto);
        }

        @Operation(summary = "Supprimer un utilitaire par son identifiant")
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> supprimer(@Parameter(description = "Identifiant de l'utilitaire") @PathVariable Long id) {
            utilitaireService.supprimer(id);
            return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Rechercher des utilitaires par critères")
        @GetMapping("/rechercher")
        public ResponseEntity<List<UtilitaireResponseDto>> rechercher(
                @Parameter(description = "Identifiant de l'utilitaire") @RequestParam(required = false) Long id,
                @Parameter(description = "Marque de l'utilitaire") @RequestParam(required = false) String marque,
                @Parameter(description = "Modèle de l'utilitaire") @RequestParam(required = false) String modele,
                @Parameter(description = "Couleur de l'utilitaire") @RequestParam(required = false) String couleur,
                @Parameter(description = "Nombre de places de l'utilitaire") @RequestParam(required = false) Integer nombreDePlace,
                @Parameter(description = "Type de carburant de l'utilitaire") @RequestParam(required = false) Carburant carburant,
                @Parameter(description = "Transmission de l'utilitaire") @RequestParam(required = false) String transmission,
                @Parameter(description = "L'utilitaire a-t-il la climatisation") @RequestParam(required = false) Boolean clim,
                @Parameter(description = "Charge maximale de l'utilitaire") @RequestParam(required = false) Integer chargeMax,
                @Parameter(description = "Poids PATC de l'utilitaire") @RequestParam(required = false) Integer poidsPATC,
                @Parameter(description = "Capacité en m3 de l'utilitaire") @RequestParam(required = false) Integer capaciteM3,
                @Parameter(description = "Liste des permis requis pour l'utilitaire") @RequestParam(required = false) List<Permis> listePermis,
                @Parameter(description = "Tarif journalier de l'utilitaire") @RequestParam(required = false) Long tarifJournalier,
                @Parameter(description = "Kilométrage de l'utilitaire") @RequestParam(required = false) Long kilometrage,
                @Parameter(description = "L'utilitaire est-il actif") @RequestParam(required = false) Boolean actif,
                @Parameter(description = "L'utilitaire est-il retiré du parc") @RequestParam(required = false) Boolean retireDuParc) {
            List<UtilitaireResponseDto> utilitaires = utilitaireService.rechercher(id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax, poidsPATC, capaciteM3, listePermis, tarifJournalier, kilometrage, actif, retireDuParc);
            return ResponseEntity.ok(utilitaires);
        }
    }