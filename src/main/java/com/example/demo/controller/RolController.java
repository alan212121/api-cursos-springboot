package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;

@RestController
@RequestMapping("/roles")
public class RolController {

    private final RolRepository repo;

    public RolController(RolRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Rol> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Rol crear(@RequestBody Rol rol) {
        rol.idRol = null;
        return repo.save(rol);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Integer id, @RequestBody Rol datos) {
        return repo.findById(id).map(r -> {
            r.nombre = datos.nombre;
            return ResponseEntity.ok(repo.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}