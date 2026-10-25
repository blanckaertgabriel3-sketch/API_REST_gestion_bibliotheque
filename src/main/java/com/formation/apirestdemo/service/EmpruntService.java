package com.formation.apirestdemo.service;

import com.formation.apirestdemo.model.Emprunt;
import com.formation.apirestdemo.repository.EmpruntRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmpruntService {
    private final EmpruntRepository empruntRepository;
    public EmpruntService (EmpruntRepository empruntRepository) {
        this.empruntRepository = empruntRepository;
    }
    public List<Emprunt> recupererTous() {
        return empruntRepository.findAll();
    }
    public Optional<Emprunt> recupererParId(Integer id) {
        return empruntRepository.findById(id);
    }

    public Emprunt creer(Emprunt emprunt) {
        emprunt.setId(null);

        Emprunt nouveauEmprunt = empruntRepository.save(emprunt);

        return nouveauEmprunt;
    }
}











