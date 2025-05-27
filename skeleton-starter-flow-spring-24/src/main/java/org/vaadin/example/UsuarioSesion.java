package org.vaadin.example;

import org.vaadin.example.models.Usuario;

public class UsuarioSesion {
    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioSesion.usuario = usuario;
    }

    public static void limpiar() {
        usuario = null;
    }
}
