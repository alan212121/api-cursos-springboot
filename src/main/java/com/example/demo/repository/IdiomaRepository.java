package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Idioma;

public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {
}