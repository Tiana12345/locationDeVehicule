package com.accenture.controller;

import com.accenture.service.AdministrateurService;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/administrateurs")
@Tag(name = "Administrateurs", description = "Gestion des administrateurs")
public class AdministrateurController {

    private final AdministrateurService administrateurService;
    private Logger logger = LoggerFactory.getLogger(AdministrateurController.class);

    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouvel administrateur", description = "Ajoute un nouvel administrateur à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    ResponseEntity<Void> ajouter(@RequestBody @Valid AdministrateurRequestDto administrateurRequestDto) {
        logger.info("Ajout d'un nouvel administrateur avec l'email : {}", administrateurRequestDto.mail());
        AdministrateurResponseDto adminEnreg = administrateurService.ajouter(administrateurRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adminEnreg.mail())
                .toUri();
        logger.debug("Administrateur créé avec succès : {}", adminEnreg);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement un administrateur", description = "Met à jour partiellement les détails d'un administrateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    ResponseEntity<AdministrateurResponseDto> modifierPartiellement(@Parameter(description = "ID de l'administrateur à modifier") @PathVariable("id") String mail, @RequestBody AdministrateurRequestDto administrateurRequestDto) {
        logger.info("Modification partielle de l'administrateur avec l'email : {}", mail);
        AdministrateurResponseDto adminModifie = administrateurService.modifierPartiellement(mail, administrateurRequestDto);
        logger.debug("Administrateur modifié avec succès : {}", adminModifie);
        return ResponseEntity.ok(adminModifie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un administrateur", description = "Supprime un administrateur de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    ResponseEntity<Void> supprimer(@Parameter(description = "ID de l'administrateur à supprimer") @PathVariable("id") String mail) {
        logger.info("Suppression de l'administrateur avec l'email : {}", mail);
        administrateurService.supprimer(mail);
        logger.debug("Administrateur supprimé avec succès");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des administrateurs", description = "Recherche des administrateurs en fonction de différents critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    })
    List<AdministrateurResponseDto> recherche(
            @Parameter(description = "Email de l'administrateur") @RequestParam(required = false) String mail,
            @Parameter(description = "Prénom de l'administrateur") @RequestParam(required = false) String prenom,
            @Parameter(description = "Nom de l'administrateur") @RequestParam(required = false) String nom,
            @Parameter(description = "Fonction de l'administrateur") @RequestParam(required = false) String fonction
    ) {
        logger.info("Recherche des administrateurs avec les critères - mail: {}, prenom: {}, nom: {}, fonction: {}", mail, prenom, nom, fonction);
        List<AdministrateurResponseDto> resultats = administrateurService.rechercher(mail, prenom, nom, fonction);
        logger.debug("Nombre d'administrateurs trouvés : {}", resultats.size());
        return resultats;
    }
}
