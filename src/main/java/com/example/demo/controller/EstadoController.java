package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Estado;
import com.example.demo.repository.EstadoRepository;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoRepository repo;

    public EstadoController(EstadoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Estado> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Estado crear(@RequestBody Estado estado) {
        estado.idEstado = null;
        return repo.save(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estado> actualizar(@PathVariable Integer id, @RequestBody Estado datos) {
        return repo.findById(id).map(e -> {
            e.nombre = datos.nombre;
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}