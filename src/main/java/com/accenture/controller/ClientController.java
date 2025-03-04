package com.accenture.controller;

import com.accenture.model.param.Permis;
import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {
    private final ClientService clientService;
    private Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouveau client", description = "Ajoute un nouveau client à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    ResponseEntity<Void> ajouter(@RequestBody @Valid ClientRequestDto clientRequestDto) {
        logger.info("Ajout d'un nouveau client : {}", clientRequestDto);
        ClientResponseDto clientEnreg = clientService.ajouter(clientRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientEnreg.mail())
                .toUri();
        logger.info("Client ajouté avec succès : {}", clientEnreg);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les clients", description = "Récupère la liste de tous les clients disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des clients récupérée avec succès")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<ClientResponseDto> client() {
        logger.info("Récupération de tous les clients");
        List<ClientResponseDto> clients = clientService.trouverTous();
        logger.info("Nombre de clients récupérés : {}", clients.size());
        return clients;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un client par ID", description = "Récupère les détails d'un client spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client trouvé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    ResponseEntity<ClientResponseDto> unClient(@Parameter(description = "ID du client à récupérer") @PathVariable("id") String mail) {
        logger.info("Récupération du client avec ID : {}", mail);
        ClientResponseDto trouve = clientService.trouver(mail);
        if (trouve != null) {
            logger.info("Client trouvé : {}", trouve);
            return ResponseEntity.ok(trouve);
        } else {
            logger.error("Client non trouvé avec ID : {}", mail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    ResponseEntity<Void> supprimer(@Parameter(description = "ID du client à supprimer") @PathVariable("id") String mail) {
        logger.info("Suppression du client avec ID : {}", mail);
        clientService.supprimer(mail);
        logger.info("Client supprimé avec succès : {}", mail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement un client", description = "Met à jour partiellement les détails d'un client existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    ResponseEntity<ClientResponseDto> modifierPartiellement(@Parameter(description = "ID du client à modifier") @PathVariable("id") String mail, @RequestBody ClientRequestDto clientRequestDto) {
        logger.info("Modification partielle du client avec ID : {}", mail);
        ClientResponseDto clientModifie = clientService.modifierPartiellement(mail, clientRequestDto);
        logger.info("Client mis à jour avec succès : {}", clientModifie);
        return ResponseEntity.ok(clientModifie);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des clients", description = "Recherche des clients en fonction de différents critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    })
    List<ClientResponseDto> recherche(
            @Parameter(description = "Email du client") @RequestParam(required = false) String mail,
            @Parameter(description = "Prénom du client") @RequestParam(required = false) String prenom,
            @Parameter(description = "Nom du client") @RequestParam(required = false) String nom,
            @Parameter(description = "Date de naissance du client") @RequestParam(required = false) LocalDate dateNaissance,
            @Parameter(description = "Rue du client") @RequestParam(required = false) String rue,
            @Parameter(description = "Code postal du client") @RequestParam(required = false) String codePostal,
            @Parameter(description = "Ville du client") @RequestParam(required = false) String ville,
            @Parameter(description = "Statut désactivé du client") @RequestParam(required = false) Boolean desactive,
            @Parameter(description = "Liste des permis du client") @RequestParam(required = false) List<Permis> listePermis,
            @Parameter(description = "Date d'inscription du client") @RequestParam(required = false) LocalDate dateInscription
    ) {
        logger.info("Recherche de clients avec les critères : mail={}, prenom={}, nom={}, dateNaissance={}, rue={}, codePostal={}, ville={}, desactive={}, listePermis={}, dateInscription={}",
                mail, prenom, nom, dateNaissance, rue, codePostal, ville, desactive, listePermis, dateInscription);
        List<ClientResponseDto> clients = clientService.rechercher(mail, prenom, nom, dateNaissance, rue, codePostal, ville, desactive, listePermis, dateInscription);
        logger.info("Nombre de clients trouvés : {}", clients.size());
        return clients;
    }
}