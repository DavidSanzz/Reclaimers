package org.vaadin.example;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.vaadin.example.models.Usuario;

import java.util.Map;

@Service
public class ServicioUsuario {

    private static final String BASE_URL = "http://localhost:8081/usuarios";  // Asegúrate de que esta URL esté correcta

    // Método para hacer login
    public Map<String, Object> login(String email, String contrasena) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/login"; // URL de login

        Usuario usuario = new Usuario(email, contrasena);

        try {
            // Hacer la solicitud POST enviando el objeto Usuario
            ResponseEntity<Map> response = restTemplate.postForEntity(url, usuario, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Devolver el cuerpo de la respuesta
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "error", "message", "Error en la conexión con el servidor");
        }

        return Map.of("status", "error", "message", "Error desconocido");
    }

    // Método para registrar un usuario
    public String registrarUsuario(Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL; // Usamos la URL base para registrar el usuario

        // Si el tipo de usuario no está asignado, asignamos un valor predeterminado
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.LUDOPATA);  // Tipo de usuario por defecto
        }

        try {
            // Hacer la solicitud POST enviando el objeto Usuario
            ResponseEntity<String> response = restTemplate.postForEntity(url, usuario, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "Usuario creado con éxito"; // Mensaje de éxito
            } else {
                return response.getBody(); // El mensaje de error del backend
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error en la conexión con el servidor"; // Mensaje de error si ocurre un problema
        }
    }
}
