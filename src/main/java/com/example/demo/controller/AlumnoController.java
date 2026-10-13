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
public class AlumnoController {

    public record AlumnoId(int id, String nombre, int edad) {}

    private final List<AlumnoId> lista = List.of(
            new AlumnoId(1, "Agny", 20),
            new AlumnoId(2, "Carlos", 21)
    );

    // Todos: /alumno
    @GetMapping("/alumno")
    public List<AlumnoId> todos() {
        return lista;
    }

    // Por ID: /alumno/1  o  /alumno/1?campo=nombre
    @GetMapping("/alumno/{id}")
    public Object porId(@PathVariable("id") int id,
                        @RequestParam(value = "campo", required = false) String campo) {

        AlumnoId a = lista.stream()
                .filter(x -> x.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el alumno con ID " + id));

        if (campo == null || campo.isBlank()) return a;
        if (campo.equals("nombre")) return Map.of("nombre", a.nombre());
        if (campo.equals("edad")) return Map.of("edad", a.edad());

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el campo " + campo);
    }
}