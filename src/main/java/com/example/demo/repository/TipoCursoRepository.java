package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.TipoCurso;

public interface TipoCursoRepository extends JpaRepository<TipoCurso, Integer> {
}