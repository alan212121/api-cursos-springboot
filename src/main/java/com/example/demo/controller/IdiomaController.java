package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Idioma;
import com.example.demo.repository.IdiomaRepository;

@RestController
@RequestMapping("/idiomas")
public class IdiomaController {

    private final IdiomaRepository repo;

    public IdiomaController(IdiomaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Idioma> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Idioma> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Idioma crear(@RequestBody Idioma idioma) {
        idioma.idIdioma = null;
        return repo.save(idioma);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Idioma> actualizar(@PathVariable Integer id, @RequestBody Idioma datos) {
        return repo.findById(id).map(i -> {
            i.nombre = datos.nombre;
            i.codigo = datos.codigo;
            return ResponseEntity.ok(repo.save(i));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}