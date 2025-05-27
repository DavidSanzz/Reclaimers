package org.vaadin.example.models;

public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String contrasena;
    private TipoUsuario tipoUsuario;

    // CAMPOS ADICIONALES RELACIONADOS CON LA LUDOPATÍA
    private String tipoJuegoFavorito;
    private String horarioFrecuenteJuego;
    private Integer tiempoSemanalJuego;
    private Double dineroGastadoMensual;
    private NivelAutoevaluacion nivelAutoevaluacion;
    private String objetivoPersonal;

    // Constructor vacío requerido por Jackson
    public Usuario() {}

    // Constructor para registro
    public Usuario(String nombre, String email, String contrasena, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
    }

    // Constructor para login
    public Usuario(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTipoJuegoFavorito() {
        return tipoJuegoFavorito;
    }

    public void setTipoJuegoFavorito(String tipoJuegoFavorito) {
        this.tipoJuegoFavorito = tipoJuegoFavorito;
    }

    public String getHorarioFrecuenteJuego() {
        return horarioFrecuenteJuego;
    }

    public void setHorarioFrecuenteJuego(String horarioFrecuenteJuego) {
        this.horarioFrecuenteJuego = horarioFrecuenteJuego;
    }

    public Integer getTiempoSemanalJuego() {
        return tiempoSemanalJuego;
    }

    public void setTiempoSemanalJuego(Integer tiempoSemanalJuego) {
        this.tiempoSemanalJuego = tiempoSemanalJuego;
    }

    public Double getDineroGastadoMensual() {
        return dineroGastadoMensual;
    }

    public void setDineroGastadoMensual(Double dineroGastadoMensual) {
        this.dineroGastadoMensual = dineroGastadoMensual;
    }

    public NivelAutoevaluacion getNivelAutoevaluacion() {
        return nivelAutoevaluacion;
    }

    public void setNivelAutoevaluacion(NivelAutoevaluacion nivelAutoevaluacion) {
        this.nivelAutoevaluacion = nivelAutoevaluacion;
    }

    public String getObjetivoPersonal() {
        return objetivoPersonal;
    }

    public void setObjetivoPersonal(String objetivoPersonal) {
        this.objetivoPersonal = objetivoPersonal;
    }

    public enum TipoUsuario {
        LUDOPATA,
        PROFESIONAL
    }

    public enum NivelAutoevaluacion {
        BAJO,
        MEDIO,
        ALTO
    }
}
