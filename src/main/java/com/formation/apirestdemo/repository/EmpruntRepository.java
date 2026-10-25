package com.formation.apirestdemo.repository;

import com.formation.apirestdemo.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
}