package com.formation.apirestdemo.controller;

import com.formation.apirestdemo.model.Auteur;
import com.formation.apirestdemo.model.Emprunt;
import com.formation.apirestdemo.service.EmpruntService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;


@RestController
@RequestMapping("/api/v1/emprunts")


public class EmpruntController {
    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }
    @GetMapping
    public ResponseEntity<List<Emprunt>> listerTous() {
        List<Emprunt> emprunt = empruntService.recupererTous();
        return ResponseEntity.ok(emprunt); // 200 OK
    }
    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> obtenirParId(@PathVariable Integer id) {
        return empruntService.recupererParId(id)
                .map(ResponseEntity::ok) // 200 OK si trouvé
                .orElse(ResponseEntity.notFound().build()); // 404 si non trouvé
    }
    @PostMapping
    public ResponseEntity<Emprunt> creer(@Valid @RequestBody Emprunt emprunt) {
        Emprunt empruntCree = empruntService.creer(emprunt);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
                .body(empruntCree);
    }
    @PatchMapping("/{id}/retour")
    public ResponseEntity<Emprunt> retourner(@PathVariable Integer id) {
        Emprunt empruntRetourne = empruntService.retourner(id);
        return ResponseEntity.ok(empruntRetourne);
    }
}