package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "idioma")
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_idioma")
    public Integer idIdioma;

    public String nombre;
    public String codigo;
}