package com.accenture.controller;

import com.accenture.model.Permis;
import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clients")

public class ClientController {
    private final ClientService clientService;
    private Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody @Valid ClientRequestDto clientRequestDto) {
        ClientResponseDto clientEnreg = clientService.ajouter(clientRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(clientEnreg.mail())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    List<ClientResponseDto> client() {
        return clientService.trouverTous();
    }

    @GetMapping("/{id}")
    ResponseEntity<ClientResponseDto> unClient(@PathVariable("id") String mail) {
        ClientResponseDto trouve = clientService.trouver(mail);
        return ResponseEntity.ok(trouve);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> supprimer(@PathVariable("id") String mail) {
        clientService.supprimer(mail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<ClientResponseDto> modifier(@PathVariable("id") String mail, @RequestBody @Valid ClientRequestDto clientRequestDto) {
        ClientResponseDto reponse = clientService.modifier(mail, clientRequestDto);
        return ResponseEntity.ok(reponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ClientResponseDto> modifierPartiellement(@PathVariable("id") String mail, @RequestBody ClientRequestDto clientRequestDto) {
        return ResponseEntity.ok(clientService.modifierPartiellement(mail, clientRequestDto));
    }

    @GetMapping("(/search)")
    List<ClientResponseDto> recherche(
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) LocalDate dateNaissance,
            @RequestParam(required = false) String rue,
            @RequestParam(required = false) String codePostal,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) Boolean desactive,
            @RequestParam(required = false) List<Permis> listePermis,
            @RequestParam(required = false) LocalDate dateInscription
    ){
        return clientService.rechercher(mail, prenom, nom, dateNaissance, rue, codePostal, ville, desactive, listePermis, dateInscription);
    }

}
