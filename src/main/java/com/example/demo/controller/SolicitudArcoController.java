package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.SolicitudArco;
import com.example.demo.repository.SolicitudArcoRepository;

@RestController
@RequestMapping("/solicitudes-arco")
public class SolicitudArcoController {

    private final SolicitudArcoRepository repo;

    public SolicitudArcoController(SolicitudArcoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<SolicitudArco> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudArco> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SolicitudArco crear(@RequestBody SolicitudArco solicitud) {
        solicitud.idSolicitud = null;
        return repo.save(solicitud);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudArco> actualizar(@PathVariable Integer id, @RequestBody SolicitudArco datos) {
        return repo.findById(id).map(s -> {
            if (datos.tipoDerecho != null) s.tipoDerecho = datos.tipoDerecho;
            s.detalles = datos.detalles;
            if (datos.estado != null) s.estado = datos.estado;
            s.fechaRespuesta = datos.fechaRespuesta;
            return ResponseEntity.ok(repo.save(s));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}