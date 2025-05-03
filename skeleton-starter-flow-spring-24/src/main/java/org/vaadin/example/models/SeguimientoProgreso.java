package org.vaadin.example.models;

public class SeguimientoProgreso {

    private Long id;
    private String comentario;
    private String progreso;
    private Usuario usuario;  // Este sería el usuario que está registrando el seguimiento

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
