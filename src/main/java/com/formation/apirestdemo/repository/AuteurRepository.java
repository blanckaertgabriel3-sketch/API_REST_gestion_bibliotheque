package com.formation.apirestdemo.repository;

import com.formation.apirestdemo.model.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuteurRepository extends JpaRepository<Auteur, Integer> {
}