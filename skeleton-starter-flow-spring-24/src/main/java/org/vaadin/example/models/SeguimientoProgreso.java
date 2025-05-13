package org.vaadin.example.models;

import java.time.LocalDateTime;

public class SeguimientoProgreso {

    private Long id;
    private String comentario;
    private String progreso;
    private LocalDateTime fecha;
    private Usuario usuario;

    // NUEVOS CAMPOS para visualización y gráficas
    private Integer nivelProgreso;
    private String estadoAnimo;
    private Boolean recaida;

    // Constructor sin parámetros
    public SeguimientoProgreso() {}

    // Constructor con parámetros anteriores
    public SeguimientoProgreso(String comentario, String progreso, Usuario usuario) {
        this.comentario = comentario;
        this.progreso = progreso;
        this.usuario = usuario;
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getProgreso() { return progreso; }
    public void setProgreso(String progreso) { this.progreso = progreso; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getNivelProgreso() { return nivelProgreso; }
    public void setNivelProgreso(Integer nivelProgreso) { this.nivelProgreso = nivelProgreso; }

    public String getEstadoAnimo() { return estadoAnimo; }
    public void setEstadoAnimo(String estadoAnimo) { this.estadoAnimo = estadoAnimo; }

    public Boolean getRecaida() { return recaida; }
    public void setRecaida(Boolean recaida) { this.recaida = recaida; }
}
