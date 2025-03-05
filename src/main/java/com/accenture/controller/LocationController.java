package com.accenture.controller;

import com.accenture.exception.LocationException;
import com.accenture.service.LocationService;
import com.accenture.service.dto.LocationRequestDto;
import com.accenture.service.dto.LocationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("locations")
@Tag(name = "Locations", description = "Gestion des locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @PostMapping
    @Operation(summary = "Ajouter une nouvelle location", description = "Ajoute une nouvelle location à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Void> ajouter(@RequestBody @Valid LocationRequestDto locationRequestDto) {
        logger.info("Ajout d'une nouvelle location : {}", locationRequestDto);
        LocationResponseDto locationEnreg = locationService.ajouter(locationRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(locationEnreg.id())
                .toUri();
        logger.info("Location ajoutée avec succès : {}", locationEnreg);
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
        logger.info("Récupération de toutes les locations");
        List<LocationResponseDto> locations = locationService.trouverToutes();
        logger.info("Nombre de locations récupérées : {}", locations.size());
        return ResponseEntity.ok(locations);
    }

}
