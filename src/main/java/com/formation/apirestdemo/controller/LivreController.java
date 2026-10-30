package com.formation.apirestdemo.controller;

import com.formation.apirestdemo.dto.LivreRequest;
import com.formation.apirestdemo.dto.LivreResponse;
import com.formation.apirestdemo.mapper.LivreMapper;
import com.formation.apirestdemo.model.Livre;
import com.formation.apirestdemo.service.LivreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<LivreResponse>> listerTous(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererTous(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));                       // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirParId(@PathVariable Long id) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();                               // 404
        }
        return ResponseEntity.ok(LivreMapper.versResponse(resultat.get()));         // 200
    }

    @PostMapping
    public ResponseEntity<LivreResponse> creer(@Valid @RequestBody LivreRequest request) {
        if (!livreService.auteurExiste(request.getAuteurId())) {
            return ResponseEntity.badRequest().build();                             // 400
        }
        if (livreService.isbnExiste(request.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();              // 409 (RM2)
        }
        Livre cree = livreService.creer(LivreMapper.versEntite(request));
        return ResponseEntity.status(HttpStatus.CREATED)                            // 201
                .body(LivreMapper.versResponse(cree));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> mettreAJour(@PathVariable Long id,
                                                     @Valid @RequestBody LivreRequest request) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();                               // 404 en premier
        }
        if (!livreService.auteurExiste(request.getAuteurId())) {
            return ResponseEntity.badRequest().build();                             // 400
        }
        if (livreService.isbnUtiliseParUnAutreLivre(request.getIsbn(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();              // 409 (RM2)
        }
        Livre modifie = livreService.mettreAJour(resultat.get(), LivreMapper.versEntite(request));
        return ResponseEntity.ok(LivreMapper.versResponse(modifie));                // 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!livreService.existe(id)) {
            return ResponseEntity.notFound().build();                               // 404
        }
        livreService.supprimer(id);
        return ResponseEntity.noContent().build();                                  // 204
    }

    @GetMapping("/recherche")
    public ResponseEntity<PagedModel<LivreResponse>> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.rechercherParTitre(titre, pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));                       // 200
    }

    @GetMapping("/disponibles")
    public ResponseEntity<PagedModel<LivreResponse>> listerDisponibles(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererDisponibles(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));                       // 200
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<LivreResponse> ajouterStock(@PathVariable Long id,
                                                      @RequestParam Integer quantite) {
        if (quantite <= 0) {
            return ResponseEntity.badRequest().build();                             // 400
        }
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();                               // 404
        }
        Livre modifie = livreService.ajouterStock(resultat.get(), quantite);
        return ResponseEntity.ok(LivreMapper.versResponse(modifie));                // 200
    }
}