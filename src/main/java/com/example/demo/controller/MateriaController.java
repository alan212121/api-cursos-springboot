package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MateriaController {

    public record MateriaId(int id, String nombre, int creditos) {}

    private final List<MateriaId> lista = List.of(
            new MateriaId(1, "Programación", 8),
            new MateriaId(2, "Matemáticas", 6)
    );

    // Todas: /materia
    @GetMapping("/materia")
    public List<MateriaId> todas() {
        return lista;
    }

    // Por ID: /materia/1  o  /materia/1?campo=creditos
    @GetMapping("/materia/{id}")
    public Object porId(@PathVariable("id") int id,
                        @RequestParam(value = "campo", required = false) String campo) {

        MateriaId m = lista.stream()
                .filter(x -> x.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la materia con ID " + id));

        if (campo == null || campo.isBlank()) return m;
        if (campo.equals("nombre")) return Map.of("nombre", m.nombre());
        if (campo.equals("creditos")) return Map.of("creditos", m.creditos());

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el campo " + campo);
    }
}