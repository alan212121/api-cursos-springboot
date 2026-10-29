# API de Cursos (Spring Boot + PostgreSQL)

API REST con 8 controllers CRUD y derechos ARCO (rectificación, olvidé contraseña con token y cancelación con código).

## Requisitos
- Java 17 o superior
- PostgreSQL con la base cursos_db creada
- Postman

## Inicio rápido
1. Crear la base de datos cursos_db y cargar los datos del script SQL que se entrega con el manual.
2. En src/main/resources/application.properties poner la contraseña de tu PostgreSQL en spring.datasource.password y dejar arco.mostrar-codigo=true
3. Arrancar el proyecto: ./gradlew bootRun
4. Agregar esta línea al archivo hosts del sistema: 127.0.0.1   api.cursos.test
5. Importar Cursos_API.postman_collection.json en Postman y correr las carpetas "1 - CRUDs" y "2 - Derechos ARCO" con el Runner.

## Plan B (terminal)
    Invoke-RestMethod http://api.cursos.test/idiomas