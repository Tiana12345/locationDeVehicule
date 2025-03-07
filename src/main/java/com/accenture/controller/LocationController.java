package com.accenture.controller;

import com.accenture.model.param.Accessoires;
import com.accenture.model.param.Etat;
import com.accenture.model.param.TypeVehiculeEnum;
import com.accenture.repository.entity.Client;
import com.accenture.repository.entity.Location;
import com.accenture.repository.entity.Vehicule;
import com.accenture.service.ClientService;
import com.accenture.service.LocationService;
import com.accenture.service.VehiculeService;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.dto.LocationRequestDto;
import com.accenture.service.dto.LocationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
@Slf4j

@RestController
@RequestMapping("locations")
@Tag(name = "Locations", description = "Gestion des locations")
public class LocationController {


    private LocationService locationService;

    public LocationController(LocationService locationService, ClientService clientService, ClientController clientController, VehiculeService vehiculeService) {
        this.locationService = locationService;

    }

    @PostMapping
    @Operation(summary = "Ajouter une nouvelle location", description = "Ajoute une nouvelle location à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Void> ajouter(
            @Parameter(description = "Détails de la location à ajouter", required = false) @RequestBody @Valid LocationRequestDto locationRequestDto,
            @Parameter(description = "Adresse email du client") @RequestParam(required = true)String mail,
            @Parameter(description = "ID du véhicule") @RequestParam(required = true)Long id,
            @Parameter(description = "TypeDeVehicule") @RequestParam(required = true) TypeVehiculeEnum typeVehiculeEnum,
            @Parameter(description = "Accessoires de la location") @RequestParam(required = true) List<Accessoires> accessoires,
            @Parameter(description = "Date de début de la location (YYYY/MM/dd) ") @RequestParam(required = true) LocalDate dateDebut,
            @Parameter(description = "Date de fin de la location (YYYY/MM/dd) ") @RequestParam(required = true) LocalDate dateFin,
            @Parameter(description = "Kilomètres parcourus") @RequestParam(required = true) Integer kilometresParcourus,
            @Parameter(description = "Montant total de la location") @RequestParam(required = true) Double montantTotal,
            @Parameter(description = "État de la location") @RequestParam(required = true) Etat etat) {
        log.info("Ajout d'une nouvelle location : {}", locationRequestDto);


        LocationResponseDto locationEnreg = locationService.ajouter(locationRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(locationEnreg.id())
                .toUri();
        log.info("Location ajoutée avec succès : {}", locationEnreg);
        return ResponseEntity.created(location).build();
    }

    /**
     * Trouver toutes les locations.
     *
     * @return la liste de toutes les locations
     */
    @GetMapping
    @Operation(summary = "Obtenir toutes les locations", description = "Récupère la liste de toutes les locations disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des locations récupérée avec succès")
    })
    public ResponseEntity<List<LocationResponseDto>> trouverToutes() {
        log.info("Récupération de toutes les locations");
        List<LocationResponseDto> locations = locationService.trouverToutes();
        log.info("Nombre de locations récupérées : {}", locations.size());
        return ResponseEntity.ok(locations);
    }

}
