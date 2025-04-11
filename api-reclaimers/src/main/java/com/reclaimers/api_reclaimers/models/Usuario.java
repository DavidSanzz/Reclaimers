package com.reclaimers.api_reclaimers.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id; // Clave primaria

    private String nombre;
    private String email;
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

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

    public enum TipoUsuario {
        LUDOPATA, PROFESIONAL
    }
}
