package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.SolicitudArco;

public interface SolicitudArcoRepository extends JpaRepository<SolicitudArco, Integer> {
    Optional<SolicitudArco> findFirstByCodigoAndTipoDerechoAndEstado(String codigo, String tipoDerecho, String estado);
}