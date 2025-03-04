package com.accenture.controller;

import com.accenture.repository.entity.Vehicule;
import com.accenture.service.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("vehicules")
@Tag(name = "Vehicules", description = "Parc de vehicules")
public class VehiculeController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculeController.class);

    @Autowired
    private VehiculeService vehiculeService;

    /**
     * Trouver tous les véhicules.
     *
     * @return la liste de tous les véhicules
     */
    @Operation(summary = "Trouver un vehicule")
    @GetMapping("/tous")
    public ResponseEntity<List<Vehicule>> trouverToutVehicules() {
        logger.info("Entrée dans la méthode trouverToutVehicules");
        List<Vehicule> vehicules = vehiculeService.trouverToutVehicules();
        logger.info("Sortie de la méthode trouverToutVehicules avec {} véhicules trouvés", vehicules.size());
        return ResponseEntity.ok(vehicules);
    }

    /**
     * Rechercher des véhicules par critères.
     *
     * @param actif        indique si le véhicule est actif
     * @param retireDuParc indique si le véhicule est retiré du parc
     * @return la liste des véhicules correspondant aux critères
     */
    @Operation(summary = "Trouver vehicules par recherches")
    @GetMapping("/rechercher")
    public ResponseEntity<List<Vehicule>> rechercher(
            @RequestParam(required = false) Boolean actif,
            @RequestParam(required = false) Boolean retireDuParc) {
        logger.info("Entrée dans la méthode rechercher avec actif={} et retireDuParc={}", actif, retireDuParc);
        List<Vehicule> vehicules = vehiculeService.rechercher(actif, retireDuParc);
        logger.info("Sortie de la méthode rechercher avec {} véhicules trouvés", vehicules.size());
        return ResponseEntity.ok(vehicules);
    }
}