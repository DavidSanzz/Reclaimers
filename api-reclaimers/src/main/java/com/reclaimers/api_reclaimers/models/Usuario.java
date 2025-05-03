package com.reclaimers.api_reclaimers.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    // CAMPOS ADICIONALES RELACIONADOS CON LA LUDOPATÍA
    private String tipoJuegoFavorito;         // Tipo de juego preferido por el usuario
    private String horarioFrecuenteJuego;     // Horarios habituales de juego
    private Integer tiempoSemanalJuego;       // Tiempo semanal de juego (en horas)
    private Double dineroGastadoMensual;      // Dinero gastado al mes (en euros)

    @Enumerated(EnumType.STRING)
    private NivelAutoevaluacion nivelAutoevaluacion;  // Nivel de autoevaluación del usuario

    private String objetivoPersonal;          // Objetivo personal redactado por el usuario

    // Constructor sin parámetros (no-arg constructor) requerido por Spring y JPA
    public Usuario() {
    }

    // Constructor con 4 parámetros (para registro)
    public Usuario(String nombre, String email, String contrasena, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
    }

    // Constructor con 2 parámetros (para login)
    public Usuario(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getTipoJuegoFavorito() { return tipoJuegoFavorito; }
    public void setTipoJuegoFavorito(String tipoJuegoFavorito) { this.tipoJuegoFavorito = tipoJuegoFavorito; }

    public String getHorarioFrecuenteJuego() { return horarioFrecuenteJuego; }
    public void setHorarioFrecuenteJuego(String horarioFrecuenteJuego) { this.horarioFrecuenteJuego = horarioFrecuenteJuego; }

    public Integer getTiempoSemanalJuego() { return tiempoSemanalJuego; }
    public void setTiempoSemanalJuego(Integer tiempoSemanalJuego) { this.tiempoSemanalJuego = tiempoSemanalJuego; }

    public Double getDineroGastadoMensual() { return dineroGastadoMensual; }
    public void setDineroGastadoMensual(Double dineroGastadoMensual) { this.dineroGastadoMensual = dineroGastadoMensual; }

    public NivelAutoevaluacion getNivelAutoevaluacion() { return nivelAutoevaluacion; }
    public void setNivelAutoevaluacion(NivelAutoevaluacion nivelAutoevaluacion) { this.nivelAutoevaluacion = nivelAutoevaluacion; }

    public String getObjetivoPersonal() { return objetivoPersonal; }
    public void setObjetivoPersonal(String objetivoPersonal) { this.objetivoPersonal = objetivoPersonal; }

    // Enum para distinguir el tipo de usuario
    public enum TipoUsuario {
        LUDOPATA,
        PROFESIONAL
    }

    // Enum para representar el nivel de autoevaluación
    public enum NivelAutoevaluacion {
        BAJO,
        MEDIO,
        ALTO
    }
}
