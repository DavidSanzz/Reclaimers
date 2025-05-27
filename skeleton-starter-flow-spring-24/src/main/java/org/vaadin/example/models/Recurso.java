package org.vaadin.example.models;

public class Recurso {

    private Long id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String enlace;
    private Usuario usuarioProfesional;

    public Recurso() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEnlace() { return enlace; }
    public void setEnlace(String enlace) { this.enlace = enlace; }

    public Usuario getUsuarioProfesional() { return usuarioProfesional; }
    public void setUsuarioProfesional(Usuario usuarioProfesional) { this.usuarioProfesional = usuarioProfesional; }
}
