# API REST con Spring Boot

Proyecto de actividad: API REST con Spring Boot y Gradle, con controllers que responden peticiones GET con información de prueba.

## Requisitos
- Java 17 o superior
- Gradle (incluido en el proyecto mediante `gradlew`)
- Postman (para las pruebas)

## Cómo ejecutar el proyecto
Desde la carpeta del proyecto:

    gradlew bootRun

En Linux o Mac:

    ./gradlew bootRun

La API queda disponible en: http://localhost:8080

## Controllers y endpoints (método GET)

| Controller | Endpoint | Descripción |
|---|---|---|
| SaludoController | /hola | Lista todos los registros |
| SaludoController | /hola/{id} | Registro por ID |
| AlumnoController | /alumno | Lista de alumnos |
| AlumnoController | /alumno/{id} | Alumno por ID |
| MateriaController | /materia | Lista de materias |
| MateriaController | /materia/{id} | Materia por ID |
| EscuelaController | /escuela | Lista de escuelas |
| EscuelaController | /escuela/{id} | Escuela por ID |

Ejemplo: `http://localhost:8080/alumno/1?campo=nombre` devuelve `{"nombre":"Agny"}`

## Pruebas realizadas
La evidencia de las pruebas en navegador web y en Postman se encuentra en el archivo `pruebas.pdf`.

## Autor
Agny Alan Tizapantzi Zuñiga