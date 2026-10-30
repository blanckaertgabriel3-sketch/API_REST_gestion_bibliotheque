package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.EmpruntRequest;
import com.formation.bibliotheque.dto.EmpruntResponse;
import com.formation.bibliotheque.mapper.EmpruntMapper;
import com.formation.bibliotheque.model.Emprunt;
import com.formation.bibliotheque.service.EmpruntService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/emprunts")
public class EmpruntController {
    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EmpruntResponse>> listerTous(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererTous(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponse> obtenirParId(@PathVariable Long id) {
        Optional<Emprunt> resultat = empruntService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EmpruntMapper.versResponse(resultat.get()));
    }

    @PostMapping
    public ResponseEntity<EmpruntResponse> creer(@Valid @RequestBody EmpruntRequest request) {
        if (!empruntService.livreExiste(request.getLivreId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!empruntService.livreDisponible(request.getLivreId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Emprunt emprunt = empruntService.creer(EmpruntMapper.versEntite(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpruntMapper.versResponse(emprunt));
    }

    @PatchMapping("/{id}/retour")
    public ResponseEntity<EmpruntResponse> retourner(@PathVariable Long id) {
        Optional<Emprunt> resultat = empruntService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Emprunt emprunt = resultat.get();
        if (emprunt.getRendu()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Emprunt rendu = empruntService.retourner(emprunt);
        return ResponseEntity.ok(EmpruntMapper.versResponse(rendu));
    }

    @GetMapping("/en-cours")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnCours(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnCours(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @GetMapping("/en-retard")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnRetard(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnRetard(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }
}