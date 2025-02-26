package com.accenture.controller;

import com.accenture.service.AdministrateurService;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
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
public class AdministrateurController {

    private final AdministrateurService administrateurService;
    private Logger logger = LoggerFactory.getLogger(AdministrateurController.class);

    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;

    }

    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody @Valid AdministrateurRequestDto administrateurRequestDto){
        AdministrateurResponseDto adminEnreg = administrateurService.ajouter(administrateurRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(adminEnreg.mail())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> supprimer(@PathVariable("id") String mail){
        administrateurService.supprimer(mail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("(/search)")
    List<AdministrateurResponseDto> recherche(
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String fonction
    ){
        return administrateurService.rechercher(mail, prenom, nom,fonction);
    }
}
