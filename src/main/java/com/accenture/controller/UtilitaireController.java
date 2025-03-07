package com.accenture.controller;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.service.UtilitaireService;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux utilitaires.
 */
@Slf4j
@RestController
@RequestMapping("/utilitaires")
@Tag(name = "Utilitaires", description = "Gestion des utilitaires")
public class UtilitaireController {

    private final UtilitaireService utilitaireService;

    public UtilitaireController(UtilitaireService utilitaireService) {
        this.utilitaireService = utilitaireService;
    }

    /**
     * Ajouter un nouvel utilitaire.
     *
     * @param utilitaireRequestDto les détails de l'utilitaire à ajouter
     * @return les détails de l'utilitaire ajouté
     */
    @PostMapping
    @Operation(summary = "Ajouter un nouvel utilitaire", description = "Ajoute un nouvel utilitaire à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilitaire créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<UtilitaireResponseDto> ajouter(
            @Parameter(description = "Détails de l'utilitaire à ajouter", required = false) @RequestBody @Valid UtilitaireRequestDto utilitaireRequestDto,
            @Parameter(description = "Marque de l'utilitaire") @RequestParam(required = true) String marque,
            @Parameter(description = "Modèle de l'utilitaire") @RequestParam(required = true) String modele,
            @Parameter(description = "Couleur de l'utilitaire") @RequestParam(required = true) String couleur,
            @Parameter(description = "Type de l'utilitaire") @RequestParam(required = true) String type,
            @Parameter(description = "Nombre de places dans l'utilitaire") @RequestParam(required = true) Integer nombreDePlace,
            @Parameter(description = "Type de carburant de l'utilitaire") @RequestParam(required = true) Carburant carburant,
            @Parameter(description = "Transmission de l'utilitaire") @RequestParam(required = true) String transmission,
            @Parameter(description = "Climatisation de l'utilitaire") @RequestParam(required = true) Boolean clim,
            @Parameter(description = "Charge maximale de l'utilitaire") @RequestParam(required = true) Integer chargeMax,
            @Parameter(description = "Poids total autorisé en charge (PTAC) de l'utilitaire") @RequestParam(required = true) Double poidsPATC,
            @Parameter(description = "Capacité en mètres cubes de l'utilitaire") @RequestParam(required = true) Integer capaciteM3,
            @Parameter(description = "Tarif journalier de l'utilitaire") @RequestParam(required = true) Long tarifJournalier,
            @Parameter(description = "Kilométrage de l'utilitaire") @RequestParam(required = true) Long kilometrage,
            @Parameter(description = "Statut actif de l'utilitaire") @RequestParam(required = true) Boolean actif,
            @Parameter(description = "Statut de retrait du parc de l'utilitaire") @RequestParam(required = true) Boolean retireDuParc) {
        log.info("Ajout d'un nouvel utilitaire : {}", utilitaireRequestDto);
        UtilitaireResponseDto utilitaireResponseDto = utilitaireService.ajouter(utilitaireRequestDto);
        log.info("Utilitaire ajouté avec succès : {}", utilitaireResponseDto);
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
        log.info("Recherche de l'utilitaire avec ID : {}", id);
        UtilitaireResponseDto utilitaireResponseDto = utilitaireService.trouver(id);
        if (utilitaireResponseDto != null) {
            log.info("Utilitaire trouvé : {}", utilitaireResponseDto);
            return ResponseEntity.ok(utilitaireResponseDto);
        } else {
            log.error("Utilitaire non trouvé avec ID : {}", id);
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
        log.info("Recherche de tous les utilitaires");
        List<UtilitaireResponseDto> utilitaires = utilitaireService.trouverToutes();
        log.info("Nombre d'utilitaires trouvés : {}", utilitaires.size());
        return ResponseEntity.ok(utilitaires);
    }

    /**
     * Modifier partiellement un utilitaire.
     *
     * @param id                   l'identifiant de l'utilitaire
     * @param utilitaireRequestDto les détails de l'utilitaire à modifier
     * @return les détails de l'utilitaire modifié
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement un utilitaire", description = "Met à jour partiellement les détails d'un utilitaire existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilitaire mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilitaire non trouvé")
    })
    public ResponseEntity<UtilitaireResponseDto> modifierPartiellement(
            @Parameter(description = "Identifiant de l'utilitaire", required = true) @PathVariable Long id,
            @Parameter(description = "Détails de l'utilitaire à modifier", required = true) @RequestBody UtilitaireRequestDto utilitaireRequestDto,
            @Parameter(description = "Marque de l'utilitaire") @RequestParam(required = true) String marque,
            @Parameter(description = "Modèle de l'utilitaire") @RequestParam(required = true) String modele,
            @Parameter(description = "Couleur de l'utilitaire") @RequestParam(required = true) String couleur,
            @Parameter(description = "Type de l'utilitaire") @RequestParam(required = true) String type,
            @Parameter(description = "Nombre de places dans l'utilitaire") @RequestParam(required = true) Integer nombreDePlace,
            @Parameter(description = "Type de carburant de l'utilitaire") @RequestParam(required = true) Carburant carburant,
            @Parameter(description = "Transmission de l'utilitaire") @RequestParam(required = true) String transmission,
            @Parameter(description = "Climatisation de l'utilitaire") @RequestParam(required = true) Boolean clim,
            @Parameter(description = "Charge maximale de l'utilitaire") @RequestParam(required = true) Integer chargeMax,
            @Parameter(description = "Poids total autorisé en charge (PTAC) de l'utilitaire") @RequestParam(required = true) Double poidsPATC,
            @Parameter(description = "Capacité en mètres cubes de l'utilitaire") @RequestParam(required = true) Integer capaciteM3,
            @Parameter(description = "Tarif journalier de l'utilitaire") @RequestParam(required = true) Long tarifJournalier,
            @Parameter(description = "Kilométrage de l'utilitaire") @RequestParam(required = true) Long kilometrage,
            @Parameter(description = "Statut actif de l'utilitaire") @RequestParam(required = true) Boolean actif,
            @Parameter(description = "Statut de retrait du parc de l'utilitaire") @RequestParam(required = true) Boolean retireDuParc) {
        log.info("Modification partielle de l'utilitaire avec ID : {}", id);
        UtilitaireResponseDto utilitaireResponseDto = utilitaireService.modifierPartiellement(id, utilitaireRequestDto);
        log.info("Utilitaire modifié avec succès : {}", utilitaireResponseDto);
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
        log.info("Suppression de l'utilitaire avec ID : {}", id);
        utilitaireService.supprimer(id);
        log.info("Utilitaire supprimé avec succès : {}", id);
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
        log.info("Recherche d'utilitaires avec les critères : id={}, marque={}, modele={}, couleur={}, nombreDePlace={}, carburant={}, transmission={}, clim={}, chargeMax={}, poidsPATC={}, capaciteM3={}, permis={}, tarifJournalier={}, kilometrage={}, actif={}, retireDuParc={}",
                id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax, poidsPATC, capaciteM3, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        List<UtilitaireResponseDto> utilitaires = utilitaireService.rechercher(id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax, poidsPATC, capaciteM3, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        log.info("Nombre d'utilitaires trouvés : {}", utilitaires.size());
        return ResponseEntity.ok(utilitaires);
    }
}