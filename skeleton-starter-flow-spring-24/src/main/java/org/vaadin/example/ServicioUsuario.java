package org.vaadin.example;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.models.Usuario;
import org.vaadin.example.models.SeguimientoProgreso;

import java.util.*;

@Service
public class ServicioUsuario {

    private static final String BASE_URL = "http://localhost:8081/usuarios";
    private static final String BASE_URL_SEGUIMIENTO = "http://localhost:8081/seguimiento";

    private final RestTemplate restTemplate;

    @Autowired
    public ServicioUsuario(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // NUEVO: Método de login que devuelve directamente el Usuario
    public Usuario loginYObtenerUsuario(String email, String contrasena) {
        String url = BASE_URL + "/login";

        Usuario loginUsuario = new Usuario(email, contrasena);

        try {
            ResponseEntity<Usuario> response = restTemplate.postForEntity(url, loginUsuario, Usuario.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Método anterior de login con mapa (por si aún lo usas en otra parte)
    public Map<String, Object> login(String email, String contrasena) {
        String url = BASE_URL + "/login";
        Usuario usuario = new Usuario(email, contrasena);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, usuario, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "error", "message", "Error en la conexión con el servidor");
        }

        return Map.of("status", "error", "message", "Error desconocido");
    }

    public String registrarUsuario(Usuario usuario) {
        String url = BASE_URL;

        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.LUDOPATA);
        }

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, usuario, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "Usuario creado con éxito";
            } else {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error en la conexión con el servidor";
        }
    }

    public String guardarSeguimiento(SeguimientoProgreso seguimiento) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL_SEGUIMIENTO, seguimiento, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return "Error en la creación del seguimiento: " + response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar el seguimiento";
        }
    }

    public Usuario obtenerUsuarioActual() {
        String url = BASE_URL + "/usuarioActual";
        try {
            ResponseEntity<Usuario> response = restTemplate.getForEntity(url, Usuario.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SeguimientoProgreso> obtenerSeguimientosPorUsuario(Long usuarioId) {
        String url = BASE_URL_SEGUIMIENTO + "/usuario/" + usuarioId;
        try {
            ResponseEntity<SeguimientoProgreso[]> response = restTemplate.getForEntity(url, SeguimientoProgreso[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
