package com.formation.apirestdemo.service;

import com.formation.apirestdemo.model.Auteur;
import com.formation.apirestdemo.repository.AuteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuteurService {
    private final AuteurRepository auteurRepository;
    public AuteurService (AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }
    public List<Auteur> recupererTous() {
        return auteurRepository.findAll();
    }
    public Optional<Auteur> recupererParId(Integer id) {
        return auteurRepository.findById(id);
    }
    public Auteur creer(Auteur auteur) {
        auteur.setId(null);

        Auteur nouveauAuteur = auteurRepository.save(auteur);

        return nouveauAuteur;
    }
    public Optional<Auteur> mettreAJour(Integer id, Auteur auteurModifie) {
        return auteurRepository.findById(id)
                .map(auteurExistant -> {
                    auteurExistant.setNom(auteurModifie.getNom());
                    auteurExistant.setPrenom(auteurModifie.getPrenom());
                    auteurExistant.setNationalite(auteurModifie.getNationalite());
                    return auteurRepository.save(auteurExistant);
                });
    }
    public boolean supprimer(Integer id) {
        if (auteurRepository.existsById(id)) {
            auteurRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
