package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    public Integer idEstado;

    public String nombre;
}