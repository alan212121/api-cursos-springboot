package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.util.Hash;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        usuario.idUsuario = null;
        usuario.passwordHash = Hash.sha256(usuario.passwordHash);
        return repo.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @RequestBody Usuario datos) {
        return repo.findById(id).map(u -> {
            u.nombre = datos.nombre;
            u.correo = datos.correo;
            u.genero = datos.genero;
            if (datos.telefono != null) u.telefono = datos.telefono;
            if (datos.passwordHash != null) u.passwordHash = Hash.sha256(datos.passwordHash);
            if (datos.activo != null) u.activo = datos.activo;
            if (datos.idRol != null) u.idRol = datos.idRol;
            return ResponseEntity.ok(repo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}