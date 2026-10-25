package com.formation.apirestdemo.controller;

import com.formation.apirestdemo.model.Auteur;
import com.formation.apirestdemo.service.AuteurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auteurs")


public class AuteurController {
    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }
    @GetMapping
    public ResponseEntity<List<Auteur>> listerTous() {
        List<Auteur> auteurs = auteurService.recupererTous();
        return ResponseEntity.ok(auteurs); // 200 OK
    }
    @GetMapping("/{id}")
    public ResponseEntity<Auteur> obtenirParId(@PathVariable Integer id) {
        return auteurService.recupererParId(id)
                .map(ResponseEntity::ok) // 200 OK si trouvé
                .orElse(ResponseEntity.notFound().build()); // 404 si non trouvé
    }
    @PostMapping
    public ResponseEntity<Auteur> creer(@Valid @RequestBody Auteur auteur) {
        Auteur auteurCree = auteurService.creer(auteur);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
                .body(auteurCree);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Auteur> mettreAJour(@PathVariable Integer id,
                                               @Valid @RequestBody Auteur auteur)
    {
        return auteurService.mettreAJour(id, auteur)
                .map(ResponseEntity::ok) // 200 OK si mis à jour
                .orElse(ResponseEntity.notFound().build()); // 404 si non trouvé
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Integer id) {
        if (auteurService.supprimer(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 si non trouvé
    }
}