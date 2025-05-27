package com.reclaimers.api_reclaimers.models;

import jakarta.persistence.*;

@Entity
@Table(name = "recursos")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;

    // Tipo de recurso, por ejemplo, "artículo", "video", "documento", etc.
    private String tipo;

    // Campo que indica el archivo o enlace relacionado con el recurso
    private String enlace;

    // Relación con el usuario profesional que sube el recurso
    @ManyToOne
    @JoinColumn(name = "usuario_profesional_id")
    private Usuario usuarioProfesional;

    // Constructor vacío para JPA
    public Recurso() {
    }

    // Constructor para facilitar la creación de objetos
    public Recurso(String titulo, String descripcion, String tipo, String enlace, Usuario usuarioProfesional) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.enlace = enlace;
        this.usuarioProfesional = usuarioProfesional;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public Usuario getUsuarioProfesional() {
        return usuarioProfesional;
    }

    public void setUsuarioProfesional(Usuario usuarioProfesional) {
        this.usuarioProfesional = usuarioProfesional;
    }
}