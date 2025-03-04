package com.accenture.controller;

import com.accenture.service.VeloService;
import com.accenture.service.dto.VeloRequestDto;
import com.accenture.service.dto.VeloResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux vélos.
 */


@RestController
@RequestMapping("/velos")
@Tag(name = "Velos", description = "Gestion des velos")
public class VeloController {

    private final VeloService veloService;
    private Logger logger = LoggerFactory.getLogger(VeloController.class);

    public VeloController(VeloService veloService) {
        this.veloService = veloService;
    }

    /**
     * Ajouter un nouveau vélo.
     *
     * @param veloRequestDto les détails du vélo à ajouter
     * @return les détails du vélo ajouté
     */
    @Operation(summary = "Ajouter un nouveau vélo")
    @PostMapping
    public ResponseEntity<VeloResponseDto> ajouter(@RequestBody VeloRequestDto veloRequestDto) {
        logger.info("Ajout d'un nouveau vélo : {}", veloRequestDto);
        VeloResponseDto veloResponseDto = veloService.ajouter(veloRequestDto);
        logger.info("Vélo ajouté avec succès : {}", veloResponseDto);
        return ResponseEntity.ok(veloResponseDto);
    }

    /**
     * Trouver un vélo par son identifiant.
     *
     * @param id l'identifiant du vélo
     * @return les détails du vélo trouvé
     */
    @Operation(summary = "Trouver un vélo par son identifiant")
    @GetMapping("/{id}")
    public ResponseEntity<VeloResponseDto> trouver(@Parameter(description = "Identifiant du vélo") @PathVariable long id) {
        logger.info("Recherche du vélo avec ID : {}", id);
        VeloResponseDto veloResponseDto = veloService.trouver(id);
        if (veloResponseDto != null) {
            logger.info("Vélo trouvé : {}", veloResponseDto);
            return ResponseEntity.ok(veloResponseDto);
        } else {
            logger.error("Vélo non trouvé avec ID : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Trouver tous les vélos.
     *
     * @return la liste de tous les vélos
     */
    @Operation(summary = "Trouver tous les vélos")
    @GetMapping
    public ResponseEntity<List<VeloResponseDto>> trouverToutes() {
        logger.info("Recherche de tous les vélos");
        List<VeloResponseDto> velos = veloService.trouverToutes();
        logger.info("Nombre de vélos trouvés : {}", velos.size());
        return ResponseEntity.ok(velos);
    }

    /**
     * Modifier partiellement un vélo.
     *
     * @param id             l'identifiant du vélo
     * @param veloRequestDto les détails du vélo à modifier
     * @return les détails du vélo modifié
     */
    @Operation(summary = "Modifier partiellement un vélo")
    @PatchMapping("/{id}")
    public ResponseEntity<VeloResponseDto> modifierPartiellement(@Parameter(description = "Identifiant du vélo") @PathVariable Long id, @RequestBody VeloRequestDto veloRequestDto) {
        logger.info("Modification partielle du vélo avec ID : {}", id);
        VeloResponseDto veloResponseDto = veloService.modifierPartiellement(id, veloRequestDto);
        logger.info("Vélo modifié avec succès : {}", veloResponseDto);
        return ResponseEntity.ok(veloResponseDto);
    }

    /**
     * Supprimer un vélo par son identifiant.
     *
     * @param id l'identifiant du vélo
     * @return une réponse vide
     */
    @Operation(summary = "Supprimer un vélo par son identifiant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@Parameter(description = "Identifiant du vélo") @PathVariable Long id) {
        logger.info("Suppression du vélo avec ID : {}", id);
        veloService.supprimer(id);
        logger.info("Vélo supprimé avec succès : {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechercher des vélos par critères.
     *
     * @param id               l'identifiant du vélo
     * @param marque           la marque du vélo
     * @param modele           le modèle du vélo
     * @param couleur          la couleur du vélo
     * @param tailleCadre      la taille du cadre du vélo
     * @param poids            le poids du vélo
     * @param electrique       le vélo est-il électrique
     * @param capaciteBatterie la capacité de la batterie du vélo
     * @param autonomie        l'autonomie du vélo
     * @param freinsADisque    le vélo a-t-il des freins à disque
     * @param tarifJournalier  le tarif journalier du vélo
     * @param kilometrage      le kilométrage du vélo
     * @param actif            le vélo est-il actif
     * @param retireDuParc     le vélo est-il retiré du parc
     * @return la liste des vélos correspondant aux critères
     */
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
        logger.info("Recherche de vélos avec les critères : id={}, marque={}, modele={}, couleur={}, tailleCadre={}, poids={}, electrique={}, capaciteBatterie={}, autonomie={}, freinsADisque={}, tarifJournalier={}, kilometrage={}, actif={}, retireDuParc={}",
                id, marque, modele, couleur, tailleCadre, poids, electrique, capaciteBatterie, autonomie, freinsADisque, tarifJournalier, kilometrage, actif, retireDuParc);
        List<VeloResponseDto> velos = veloService.rechercher(id, marque, modele, couleur, tailleCadre, poids, electrique, capaciteBatterie, autonomie, freinsADisque, tarifJournalier, kilometrage, actif, retireDuParc);
        logger.info("Nombre de vélos trouvés : {}", velos.size());
        return ResponseEntity.ok(velos);
    }
}