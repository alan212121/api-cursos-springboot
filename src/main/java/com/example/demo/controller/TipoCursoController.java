package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.TipoCurso;
import com.example.demo.repository.TipoCursoRepository;

@RestController
@RequestMapping("/tipos-curso")
public class TipoCursoController {

    private final TipoCursoRepository repo;

    public TipoCursoController(TipoCursoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<TipoCurso> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCurso> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TipoCurso crear(@RequestBody TipoCurso tipo) {
        tipo.idTipoCurso = null;
        return repo.save(tipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCurso> actualizar(@PathVariable Integer id, @RequestBody TipoCurso datos) {
        return repo.findById(id).map(t -> {
            t.nombre = datos.nombre;
            return ResponseEntity.ok(repo.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}