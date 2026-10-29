package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modalidad")
public class Modalidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modalidad")
    public Integer idModalidad;

    public String nombre;
}