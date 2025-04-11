package org.vaadin.example.models;

public class Usuario {
    private String nombre;
    private String email;
    private String contrasena;
    private TipoUsuario tipoUsuario;

    // Constructor con cuatro parámetros (para el registro)
    public Usuario(String nombre, String email, String contrasena, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
    }

    // Constructor con dos parámetros (para login)
    public Usuario(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

    // Getters y Setters
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

    // Enum TipoUsuario
    public enum TipoUsuario {
        LUDOPATA,
        PROFESIONAL
    }
}
