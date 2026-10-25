package com.formation.apirestdemo.controller;

import com.formation.apirestdemo.model.Auteur;
import com.formation.apirestdemo.service.AuteurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auteurs")


public class AuteurController {
    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }
    @GetMapping("/bonjour")
    public Map<String, String> auteurs() {
        return Map.of(
                "message", "Bonjour les auteurs !"
        );
    }
    @GetMapping
    public ResponseEntity<List<Auteur>> listerTous() {
        List<Auteur> produits = auteurService.recupererTous();
        return ResponseEntity.ok(produits); // 200 OK
    }
}