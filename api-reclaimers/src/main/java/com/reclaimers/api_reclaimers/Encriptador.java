package com.reclaimers.api_reclaimers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Encriptador {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // Método para encriptar una contraseña
    public static String encriptar(String contrasena) {
        return encoder.encode(contrasena);
    }

    // Método para verificar si la contraseña escrita coincide con la encriptada
    public static boolean verificar(String contrasenaOriginal, String contrasenaEncriptada) {
        return encoder.matches(contrasenaOriginal, contrasenaEncriptada);
    }
}
