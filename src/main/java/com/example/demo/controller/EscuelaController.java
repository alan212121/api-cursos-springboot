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
public class EscuelaController {

    public record EscuelaId(int id, String nombre, String ciudad) {}

    private final List<EscuelaId> lista = List.of(
            new EscuelaId(1, "UTTEC", "México"),
            new EscuelaId(2, "UNAM", "México")
    );

    // Todas: /escuela
    @GetMapping("/escuela")
    public List<EscuelaId> todas() {
        return lista;
    }

    // Por ID: /escuela/1  o  /escuela/1?campo=ciudad
    @GetMapping("/escuela/{id}")
    public Object porId(@PathVariable("id") int id,
                        @RequestParam(value = "campo", required = false) String campo) {

        EscuelaId e = lista.stream()
                .filter(x -> x.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la escuela con ID " + id));

        if (campo == null || campo.isBlank()) return e;
        if (campo.equals("nombre")) return Map.of("nombre", e.nombre());
        if (campo.equals("ciudad")) return Map.of("ciudad", e.ciudad());

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el campo " + campo);
    }
}