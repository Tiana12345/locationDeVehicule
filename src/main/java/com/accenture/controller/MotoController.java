package com.accenture.controller;

import com.accenture.model.param.Permis;
import com.accenture.service.MotoService;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
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
 * Contrôleur pour gérer les opérations liées aux motos.
 */


@RestController
@RequestMapping("/motos")
@Tag(name = "Motos", description = "Gestion des motos")
public class MotoController {

    private final MotoService motoService;
    private Logger logger = LoggerFactory.getLogger(MotoController.class);

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    /**
     * Ajouter une nouvelle moto.
     *
     * @param motoRequestDto les détails de la moto à ajouter
     * @return les détails de la moto ajoutée
     */
    @Operation(summary = "Ajouter une nouvelle moto")
    @PostMapping
    public ResponseEntity<MotoResponseDto> ajouter(@RequestBody MotoRequestDto motoRequestDto) {
        logger.info("Ajout d'une nouvelle moto : {}", motoRequestDto);
        MotoResponseDto motoResponseDto = motoService.ajouter(motoRequestDto);
        logger.info("Moto ajoutée avec succès : {}", motoResponseDto);
        return ResponseEntity.ok(motoResponseDto);
    }

    /**
     * Trouver une moto par son identifiant.
     *
     * @param id l'identifiant de la moto
     * @return les détails de la moto trouvée
     */
    @Operation(summary = "Trouver une moto par son identifiant")
    @GetMapping("/{id}")
    public ResponseEntity<MotoResponseDto> trouver(@Parameter(description = "Identifiant de la moto") @PathVariable long id) {
        logger.info("Recherche de la moto avec ID : {}", id);
        MotoResponseDto motoResponseDto = motoService.trouver(id);
        if (motoResponseDto != null) {
            logger.info("Moto trouvée : {}", motoResponseDto);
            return ResponseEntity.ok(motoResponseDto);
        } else {
            logger.error("Moto non trouvée avec ID : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Trouver toutes les motos.
     *
     * @return la liste de toutes les motos
     */
    @Operation(summary = "Trouver toutes les motos")
    @GetMapping
    public ResponseEntity<List<MotoResponseDto>> trouverToutes() {
        logger.info("Recherche de toutes les motos");
        List<MotoResponseDto> motos = motoService.trouverToutes();
        logger.info("Nombre de motos trouvées : {}", motos.size());
        return ResponseEntity.ok(motos);
    }

    /**
     * Modifier partiellement une moto.
     *
     * @param id             l'identifiant de la moto
     * @param motoRequestDto les détails de la moto à modifier
     * @return les détails de la moto modifiée
     */
    @Operation(summary = "Modifier partiellement une moto")
    @PatchMapping("/{id}")
    public ResponseEntity<MotoResponseDto> modifierPartiellement(@Parameter(description = "Identifiant de la moto") @PathVariable Long id, @RequestBody MotoRequestDto motoRequestDto) {
        logger.info("Modification partielle de la moto avec ID : {}", id);
        MotoResponseDto motoResponseDto = motoService.modifierPartiellement(id, motoRequestDto);
        logger.info("Moto modifiée avec succès : {}", motoResponseDto);
        return ResponseEntity.ok(motoResponseDto);
    }

    /**
     * Supprimer une moto par son identifiant.
     *
     * @param id l'identifiant de la moto
     * @return une réponse vide
     */
    @Operation(summary = "Supprimer une moto par son identifiant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@Parameter(description = "Identifiant de la moto") @PathVariable Long id) {
        logger.info("Suppression de la moto avec ID : {}", id);
        motoService.supprimer(id);
        logger.info("Moto supprimée avec succès : {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechercher des motos par critères.
     *
     * @param id              l'identifiant de la moto
     * @param marque          la marque de la moto
     * @param modele          le modèle de la moto
     * @param couleur         la couleur de la moto
     * @param nombreCylindres le nombre de cylindres de la moto
     * @param poids           le poids de la moto
     * @param puissanceEnkW   la puissance en kW de la moto
     * @param hauteurSelle    la hauteur de selle de la moto
     * @param transmission    la transmission de la moto
     * @param permis          le permis requis pour la moto
     * @param tarifJournalier le tarif journalier de la moto
     * @param kilometrage     le kilométrage de la moto
     * @param actif           la moto est-elle active
     * @param retireDuParc    la moto est-elle retirée du parc
     * @return la liste des motos correspondant aux critères
     */
    @Operation(summary = "Rechercher des motos par critères")
    @GetMapping("/rechercher")
    public ResponseEntity<List<MotoResponseDto>> rechercher(
            @Parameter(description = "Identifiant de la moto") @RequestParam(required = false) Long id,
            @Parameter(description = "Marque de la moto") @RequestParam(required = false) String marque,
            @Parameter(description = "Modèle de la moto") @RequestParam(required = false) String modele,
            @Parameter(description = "Couleur de la moto") @RequestParam(required = false) String couleur,
            @Parameter(description = "Nombre de cylindres de la moto") @RequestParam(required = false) Integer nombreCylindres,
            @Parameter(description = "Poids de la moto") @RequestParam(required = false) Integer poids,
            @Parameter(description = "Puissance en kW de la moto") @RequestParam(required = false) Integer puissanceEnkW,
            @Parameter(description = "Hauteur de selle de la moto") @RequestParam(required = false) Integer hauteurSelle,
            @Parameter(description = "Transmission de la moto") @RequestParam(required = false) String transmission,
            @Parameter(description = "Liste des permis requis pour la moto") @RequestParam(required = false) Permis permis,
            @Parameter(description = "Tarif journalier de la moto") @RequestParam(required = false) Long tarifJournalier,
            @Parameter(description = "Kilométrage de la moto") @RequestParam(required = false) Long kilometrage,
            @Parameter(description = "La moto est-elle active") @RequestParam(required = false) Boolean actif,
            @Parameter(description = "La moto est-elle retirée du parc") @RequestParam(required = false) Boolean retireDuParc) {
        logger.info("Recherche de motos avec les critères : id={}, marque={}, modele={}, couleur={}, nombreCylindres={}, poids={}, puissanceEnkW={}, hauteurSelle={}, transmission={}, permis={}, tarifJournalier={}, kilometrage={}, actif={}, retireDuParc={}",
                id, marque, modele, couleur, nombreCylindres, poids, puissanceEnkW, hauteurSelle, transmission, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        List<MotoResponseDto> motos = motoService.rechercher(id, marque, modele, couleur, nombreCylindres, poids, puissanceEnkW, hauteurSelle, transmission, permis, tarifJournalier, kilometrage, actif, retireDuParc);
        logger.info("Nombre de motos trouvées : {}", motos.size());
        return ResponseEntity.ok(motos);
    }
}