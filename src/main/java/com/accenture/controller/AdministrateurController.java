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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/administrateurs")
@Tag(name = "Administrateurs", description = "Gestion des administrateurs")
public class AdministrateurController {

    private final AdministrateurService administrateurService;

    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouvel administrateur", description = "Ajoute un nouvel administrateur à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Void> ajouter(
            @Parameter(description = "Détails de l'administrateur à ajouter", required = true) @RequestBody @Valid AdministrateurRequestDto administrateurRequestDto,
            @Parameter(description = "Adresse email de l'administrateur") @RequestParam(required = false) String mail,
            @Parameter(description = "Mot de passe de l'administrateur") @RequestParam(required = false) String password,
            @Parameter(description = "Nom de l'administrateur") @RequestParam(required = false) String nom,
            @Parameter(description = "Prénom de l'administrateur") @RequestParam(required = false) String prenom,
            @Parameter(description = "Fonction de l'administrateur") @RequestParam(required = false) String fonction) {
        log.info("Ajout d'un nouvel administrateur avec l'email : {}", administrateurRequestDto.mail());
        AdministrateurResponseDto adminEnreg = administrateurService.ajouter(administrateurRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adminEnreg.mail())
                .toUri();
        log.debug("Administrateur créé avec succès : {}", adminEnreg);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement un administrateur", description = "Met à jour partiellement les détails d'un administrateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    public ResponseEntity<AdministrateurResponseDto> modifierPartiellement(
            @Parameter(description = "ID de l'administrateur à modifier", required = true) @PathVariable("id") String mail,
            @Parameter(description = "Détails de l'administrateur à modifier", required = true) @RequestBody AdministrateurRequestDto administrateurRequestDto,
            @Parameter(description = "Mot de passe de l'administrateur") @RequestParam(required = false) String password,
            @Parameter(description = "Nom de l'administrateur") @RequestParam(required = false) String nom,
            @Parameter(description = "Prénom de l'administrateur") @RequestParam(required = false) String prenom,
            @Parameter(description = "Fonction de l'administrateur") @RequestParam(required = false) String fonction) {
        log.info("Modification partielle de l'administrateur avec ID : {}", mail);
        AdministrateurResponseDto adminModifie = administrateurService.modifierPartiellement(mail, administrateurRequestDto);
        log.info("Administrateur mis à jour avec succès : {}", adminModifie);
        return ResponseEntity.ok(adminModifie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un administrateur", description = "Supprime un administrateur de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    ResponseEntity<Void> supprimer(@Parameter(description = "ID de l'administrateur à supprimer") @PathVariable("id") String mail) {
        log.info("Suppression de l'administrateur avec l'email : {}", mail);
        administrateurService.supprimer(mail);
        log.debug("Administrateur supprimé avec succès");
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
        log.info("Recherche des administrateurs avec les critères - mail: {}, prenom: {}, nom: {}, fonction: {}", mail, prenom, nom, fonction);
        List<AdministrateurResponseDto> resultats = administrateurService.rechercher(mail, prenom, nom, fonction);
        log.debug("Nombre d'administrateurs trouvés : {}", resultats.size());
        return resultats;
    }
}
