package com.example.demo.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    public Integer idUsuario;

    public String nombre;
    public String correo;
    public String genero;
    public String telefono;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_hash")
    public String passwordHash;

    public Boolean activo = true;

    @Column(name = "creado_en")
    public LocalDateTime creadoEn = LocalDateTime.now();

    @Column(name = "id_rol")
    public Integer idRol;
}