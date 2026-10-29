package com.example.demo.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "solicitud_arco")
public class SolicitudArco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    public Integer idSolicitud;

    @Column(name = "id_usuario")
    public Integer idUsuario;

    @Column(name = "tipo_derecho")
    public String tipoDerecho;

    public String detalles;

    public String estado = "PENDIENTE";

    @Column(name = "fecha_solicitud")
    public LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "fecha_respuesta")
    public LocalDateTime fechaRespuesta;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String codigo;

    @Column(name = "expira_en")
    public LocalDateTime expiraEn;
}