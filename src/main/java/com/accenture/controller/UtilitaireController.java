package com.accenture.controller;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.service.UtilitaireService;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux utilitaires.
 */

@RestController
@RequestMapping("/utilitaires")
@Tag(name = "Utilitaires", description = "Gestion des utilitaires")
public class UtilitaireController {

    private final UtilitaireService utilitaireService;
    private Logger logger = LoggerFactory.getLogger(UtilitaireController.class);

    public UtilitaireController(UtilitaireService utilitaireService) {
        this.utilitaireService = utilitaireService;
    }

    /**
     * Ajouter un nouvel utilitaire.
     *
     * @param utilitaireRequestDto les détails de l'utilitaire à ajouter
     * @return les détails de l'utilitaire ajouté
     */
    @Operation(summary = "Ajouter un nouvel utilitaire")
    @PostMapping
    public ResponseEntity<UtilitaireResponseDto> ajouter(@RequestBody UtilitaireRequestDto utilitaireRequestDto) {
        logger.info("Ajout d'un nouvel utilitaire : {}", utilitaireRequestDto);
        UtilitaireResponseDto utilitaireResponseDto = utilitaireService.ajouter(utilitaireRequestDto);
        logger.info("Utilitaire ajouté avec succès : {}", utilitaireResponseDto);
        return ResponseEntity.ok(utilitaireResponseDto);
    }

    /**
     * Trouver un utilitaire par son identifiant.
     *
     * @param id l'identifiant de l'utilitaire
     * @return les détails de l'utilitaire trouvé
     */
    @Operation(summary = "Trouver un utilitaire par son identifiant")
    @GetMapping("/{id}")
    public ResponseEntity<UtilitaireResponseDto> trouver(@Parameter(description = "Identifiant de l'utilitaire") @PathVariable long id) {
        logger.info("Recherche de l'utilitaire avec ID : {}", id);
        UtilitaireResponseDto utilitaireResponseDto = utilitaireService.trouver(id);
        if (utilitaireResponseDto != null) {
            logger.info("Utilitaire trouvé : {}", utilitaireResponseDto);
            return ResponseEntity.ok(utilitaireResponseDto);
        } else {
            logger.error("Utilitaire non trouvé avec ID : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Trouver tous les utilitaires.
     *
     * @return la liste de tous les utilitaires
     */
    @Operation(summary = "Trouver tous les utilitaires")
    @GetMapping
    public ResponseEntity<List<UtilitaireResponseDto>> trouverToutes() {
        logger.info("Recherche de tous les utilitaires");
        List<UtilitaireResponseDto> utilitaires = utilitaireService.trouverToutes();
        logger.info("Nombre d'utilitaires trouvés : {}", utilitaires.size());
        return ResponseEntity.ok(utilitaires);
    }

    /**
     * Modifier partiellement un utilitaire.
     *
     * @param id                   l'identifiant de l'utilitaire
     * @param utilitaireRequestDto les détails de l'utilitaire à modifier
     * @return les détails de l'utilitaire modifié
     */
    @Operation(summary = "Modifier partiellement un utilitaire")
    @PatchMapping("/{id}")
    public ResponseEntity<UtilitaireResponseDto> modifierPartiellement(@Parameter(description = "Identifiant de l'utilitaire") @PathVariable Long id, @RequestBody UtilitaireRequestDto utilitaireRequestDto) {
        logger.info("Modification partielle de l'utilitaire avec ID : {}", id);
        UtilitaireResponseDto utilitaireResponseDto = utilitaireService.modifierPartiellement(id, utilitaireRequestDto);
        logger.info("Utilitaire modifié avec succès : {}", utilitaireResponseDto);
        return ResponseEntity.ok(utilitaireResponseDto);
    }

    /**
     * Supprimer un utilitaire par son identifiant.
     *
     * @param id l'identifiant de l'utilitaire
     * @return une réponse vide
     */
    @Operation(summary = "Supprimer un utilitaire par son identifiant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@Parameter(description = "Identifiant de l'utilitaire") @PathVariable Long id) {
        logger.info("Suppression de l'utilitaire avec ID : {}", id);
        utilitaireService.supprimer(id);
        logger.info("Utilitaire supprimé avec succès : {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechercher des utilitaires par critères.
     *
     * @param id              l'identifiant de l'utilitaire
     * @param marque          la marque de l'utilitaire
     * @param modele          le modèle de l'utilitaire
     * @param couleur         la couleur de l'utilitaire
     * @param nombreDePlace   le nombre de places de l'utilitaire
     * @param carburant       le type de carburant de l'utilitaire
     * @param transmission    la transmission de l'utilitaire
     * @param clim            l'utilitaire a-t-il la climatisation
     * @param chargeMax       la charge maximale de l'utilitaire
     * @param poidsPATC       le poids PATC de l'utilitaire
     * @param capaciteM3      la capacité en m3 de l'utilitaire
     * @param permis          le permis requis pour l'utilitaire
     * @param tarifJournalier le tarif journalier de l'utilitaire
     * @param kilometrage     le kilométrage de l'utilitaire
     * @param actif           l'utilitaire est-il actif
     * @param retireDuParc    l'utilitaire est-il retiré du parc
     * @return la liste des utilitaires correspondant aux critères
     */
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
            @Parameter(description = "Permis requis pour l'utilitaire") @RequestParam(required = false) Permis permis,
            @Parameter(description = "Tarif journalier de l'utilitaire") @RequestParam(required = false) Long tarifJournalier,
            @Parameter(description = "Kilométrage de l'utilitaire") @RequestParam(required = false) Long kilometrage,
            @Parameter(description = "L'utilitaire est-il actif") @RequestParam(required = false) Boolean actif,
            @Parameter(description = "L'utilitaire est-il retiré du parc") @RequestParam(required = false) Boolean retireDuParc) {
        logger.info("Recherche d'utilitaires avec les critères : id={}, marque={}, modele={}, couleur={}, nombreDePlace={}, carburant={}, transmission={}, clim={}, chargeMax={}, poidsPATC={}, capaciteM3={}, permis={}, tarifJournalier={}, kilometrage={}, actif={}, retireDuParc={}",
                id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax, poidsPATC, capaciteM3, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        List<UtilitaireResponseDto> utilitaires = utilitaireService.rechercher(id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax, poidsPATC, capaciteM3, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        logger.info("Nombre d'utilitaires trouvés : {}", utilitaires.size());
        return ResponseEntity.ok(utilitaires);
    }
}