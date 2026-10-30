package com.formation.apirestdemo.controller;

import com.formation.apirestdemo.dto.AuteurRequest;
import com.formation.apirestdemo.dto.AuteurResponse;
import com.formation.apirestdemo.dto.LivreResponse;
import com.formation.apirestdemo.mapper.AuteurMapper;
import com.formation.apirestdemo.mapper.LivreMapper;
import com.formation.apirestdemo.model.Auteur;
import com.formation.apirestdemo.model.Livre;
import com.formation.apirestdemo.service.AuteurService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {

    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<AuteurResponse>> listerTous(
            @PageableDefault(size = 10, sort = "nom") Pageable pageable) {
        Page<Auteur> page = auteurService.recupererTous(pageable);
        return ResponseEntity.ok(AuteurMapper.versPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirParId(@PathVariable Long id) {
        Optional<Auteur> resultat = auteurService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(AuteurMapper.versResponse(resultat.get()));
    }

    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur auteur = auteurService.creer(AuteurMapper.versEntite(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AuteurMapper.versResponse(auteur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> mettreAJour(@PathVariable Long id,
                                                      @Valid @RequestBody AuteurRequest request) {
        Optional<Auteur> resultat = auteurService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Auteur auteur = auteurService.mettreAJour(resultat.get(), AuteurMapper.versEntite(request));
        return ResponseEntity.ok(AuteurMapper.versResponse(auteur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        if (auteurService.aDesLivres(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        auteurService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/livres")
    public ResponseEntity<PagedModel<LivreResponse>> listerLivres(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        Page<Livre> page = auteurService.recupererLivres(id, pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }
}