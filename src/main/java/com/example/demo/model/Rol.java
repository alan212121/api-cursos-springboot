package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    public Integer idRol;

    @Column(unique = true, nullable = false, length = 50)
    public String nombre;
}