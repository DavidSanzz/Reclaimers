package com.reclaimers.api_reclaimers.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long emisorId;
    private Long receptorId;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private LocalDateTime fechaEnvio;

    // Constructor vacío
    public Mensaje() {
    }

    // Constructor con campos
    public Mensaje(Long emisorId, Long receptorId, String contenido, LocalDateTime fechaEnvio) {
        this.emisorId = emisorId;
        this.receptorId = receptorId;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long emisorId) {
        this.emisorId = emisorId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
