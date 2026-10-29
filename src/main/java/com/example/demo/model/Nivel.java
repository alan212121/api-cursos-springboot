package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nivel")
public class Nivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    public Integer idNivel;

    public String nombre;
}