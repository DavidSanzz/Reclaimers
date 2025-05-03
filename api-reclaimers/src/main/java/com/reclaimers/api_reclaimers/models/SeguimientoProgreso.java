package com.reclaimers.api_reclaimers.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class SeguimientoProgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;
    private String progreso;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructor sin parámetros
    public SeguimientoProgreso() {}

    // Constructor con parámetros
    public SeguimientoProgreso(String comentario, String progreso, Usuario usuario) {
        this.comentario = comentario;
        this.progreso = progreso;
        this.usuario = usuario;
    }

    // Getter y Setter para id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter y Setter para comentario
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    // Getter y Setter para progreso
    public String getProgreso() {
        return progreso;
    }

    public void setProgreso(String progreso) {
        this.progreso = progreso;
    }

    // Getter y Setter para usuario
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
