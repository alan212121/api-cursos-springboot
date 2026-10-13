// package com.example.demo.controller;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class SaludoController {

//     @GetMapping("/hola")
//     public String holaXd() {
//         return "Hola que hace";
//     }
// }

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
public class SaludoController {

    private final List<Datos> lista = List.of(
            new Datos(1,
                    new Alumno("Agny", 20),
                    new Materia("Programación", 8),
                    new Escuela("UTTEC", "México")),
            new Datos(2,
                    new Alumno("Carlos", 21),
                    new Materia("Matemáticas", 6),
                    new Escuela("UTTEC", "México"))
    );

    @GetMapping("/hola")
    public List<Datos> todos() {
        return lista;
    }

    @GetMapping("/hola/{id}")
    public Object porId(@PathVariable("id") int id,
                        @RequestParam(value = "tipo", required = false) String tipo,
                        @RequestParam(value = "campo", required = false) String campo) {

        Datos d = buscar(id);

        if (tipo == null || tipo.isBlank()) {
            return d;
        }

        boolean sinCampo = (campo == null || campo.isBlank());

        switch (tipo) {
            case "alumno":
                if (sinCampo) return d.alumno();
                if (campo.equals("nombre")) return Map.of("nombre", d.alumno().nombre());
                if (campo.equals("edad")) return Map.of("edad", d.alumno().edad());
                break;
            case "materia":
                if (sinCampo) return d.materia();
                if (campo.equals("nombre")) return Map.of("nombre", d.materia().nombre());
                if (campo.equals("creditos")) return Map.of("creditos", d.materia().creditos());
                break;
            case "escuela":
                if (sinCampo) return d.escuela();
                if (campo.equals("nombre")) return Map.of("nombre", d.escuela().nombre());
                if (campo.equals("ciudad")) return Map.of("ciudad", d.escuela().ciudad());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el tipo " + tipo);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el campo " + campo);
    }

    private Datos buscar(int id) {
        return lista.stream()
                .filter(d -> d.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el ID " + id));
    }

    public record Datos(int id, Alumno alumno, Materia materia, Escuela escuela) {}
    public record Alumno(String nombre, int edad) {}
    public record Materia(String nombre, int creditos) {}
    public record Escuela(String nombre, String ciudad) {}
}