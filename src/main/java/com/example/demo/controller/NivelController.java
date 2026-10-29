package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Nivel;
import com.example.demo.repository.NivelRepository;

@RestController
@RequestMapping("/niveles")
public class NivelController {

    private final NivelRepository repo;

    public NivelController(NivelRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Nivel> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nivel> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Nivel crear(@RequestBody Nivel nivel) {
        nivel.idNivel = null;
        return repo.save(nivel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nivel> actualizar(@PathVariable Integer id, @RequestBody Nivel datos) {
        return repo.findById(id).map(n -> {
            n.nombre = datos.nombre;
            return ResponseEntity.ok(repo.save(n));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}