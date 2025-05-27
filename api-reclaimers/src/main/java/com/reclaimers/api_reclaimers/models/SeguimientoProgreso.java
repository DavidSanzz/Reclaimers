package com.reclaimers.api_reclaimers.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SeguimientoProgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;
    private String progreso;

    private LocalDateTime fecha;

    private Integer nivelProgreso;

    @Enumerated(EnumType.STRING)
    private EstadoAnimo estadoAnimo;

    // ✅ Modificado para guardar como 0/1 en la BD
    @Column(columnDefinition = "TINYINT(1)")
    private Boolean recaida;

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

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
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

    public Integer getNivelProgreso() { return nivelProgreso; }
    public void setNivelProgreso(Integer nivelProgreso) { this.nivelProgreso = nivelProgreso; }

    public EstadoAnimo getEstadoAnimo() { return estadoAnimo; }
    public void setEstadoAnimo(EstadoAnimo estadoAnimo) { this.estadoAnimo = estadoAnimo; }

    public Boolean getRecaida() { return recaida; }
    public void setRecaida(Boolean recaida) { this.recaida = recaida; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    // Enum interno para Estado de Ánimo
    public enum EstadoAnimo {
        MUY_BAJO,
        BAJO,
        NEUTRO,
        ALTO,
        MUY_ALTO
    }
}
