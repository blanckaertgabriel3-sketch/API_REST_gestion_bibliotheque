package com.formation.apirestdemo.service;

import com.formation.apirestdemo.model.Auteur;
import com.formation.apirestdemo.repository.AuteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuteurService {
    private final AuteurRepository auteurRepository;
    public AuteurService (AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }
    public List<Auteur> recupererTous() {
        return auteurRepository.findAll();
    }
}
