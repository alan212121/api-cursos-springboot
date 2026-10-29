package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Modalidad;

public interface ModalidadRepository extends JpaRepository<Modalidad, Integer> {
}