package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Nivel;

public interface NivelRepository extends JpaRepository<Nivel, Integer> {
}