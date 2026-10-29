package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_curso")
public class TipoCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_curso")
    public Integer idTipoCurso;

    public String nombre;
}