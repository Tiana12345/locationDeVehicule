package com.accenture.controller;

import com.accenture.repository.entity.Vehicule;
import com.accenture.service.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("vehicules")
@Tag(name = "Vehicules", description = "Parc de vehicules")
public class VehiculeController {


    private VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    /**
     * Trouver tous les véhicules.
     *
     * @return la liste de tous les véhicules
     */
    @Operation(summary = "Trouver un vehicule")
    @GetMapping("/tous")
    public ResponseEntity<List<Vehicule>> trouverToutVehicules() {
        log.info("Entrée dans la méthode trouverToutVehicules");
        List<Vehicule> vehicules = vehiculeService.trouverToutVehicules();
        log.info("Sortie de la méthode trouverToutVehicules avec {} véhicules trouvés", vehicules.size());
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
            @RequestParam(required = true) Boolean actif,
            @RequestParam(required = true) Boolean retireDuParc) {
        log.info("Entrée dans la méthode rechercher avec actif={} et retireDuParc={}", actif, retireDuParc);
        List<Vehicule> vehicules = vehiculeService.rechercher(actif, retireDuParc);
        log.info("Sortie de la méthode rechercher avec {} véhicules trouvés", vehicules.size());
        return ResponseEntity.ok(vehicules);
    }
}