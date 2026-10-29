package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Modalidad;
import com.example.demo.repository.ModalidadRepository;

@RestController
@RequestMapping("/modalidades")
public class ModalidadController {

    private final ModalidadRepository repo;

    public ModalidadController(ModalidadRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Modalidad> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modalidad> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Modalidad crear(@RequestBody Modalidad modalidad) {
        modalidad.idModalidad = null;
        return repo.save(modalidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Modalidad> actualizar(@PathVariable Integer id, @RequestBody Modalidad datos) {
        return repo.findById(id).map(m -> {
            m.nombre = datos.nombre;
            return ResponseEntity.ok(repo.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}