package org.vaadin.example;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.models.Usuario;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Mensaje;

import java.util.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ServicioUsuario {

    private static final String BASE_URL = "https://reset-app-q6h3.onrender.com/usuarios";
    private static final String BASE_URL_SEGUIMIENTO = "https://reset-app-q6h3.onrender.com/seguimiento";
    private static final String BASE_URL_MENSAJES = "https://reset-app-q6h3.onrender.com/mensajes";

    private final RestTemplate restTemplate;

    @Autowired
    public ServicioUsuario(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    public String actualizarInformacionAdicional(Long id, Usuario usuario) {
        String url = BASE_URL + "/" + id + "/informacion-adicional";

        try {
            restTemplate.put(url, usuario);
            return "Información actualizada correctamente";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la información";
        }
    }

    // NUEVO MÉTODO: Enviar informe PDF al correo indicado
    public String enviarInformePdf(Long usuarioId, String correoDestino) {
        try {
            String correoCodificado = URLEncoder.encode(correoDestino, StandardCharsets.UTF_8.toString());
            String url = BASE_URL + "/" + usuarioId + "/enviar-pdf?correoDestino=" + correoCodificado;

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            return "No se pudo enviar el informe: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error inesperado al enviar el informe: " + e.getMessage();
        }
    }

    // NUEVOS MÉTODOS PARA MENSAJES

    public List<Mensaje> obtenerMensajesDeUsuario(Long usuarioId) {
        String url = BASE_URL_MENSAJES + "/" + usuarioId;
        try {
            Mensaje[] mensajes = restTemplate.getForObject(url, Mensaje[].class);
            return Arrays.asList(mensajes);
        } catch (Exception e) {
            System.err.println("Error al obtener mensajes: " + e.getMessage());
            return List.of();
        }
    }

    public String enviarMensaje(Mensaje mensaje) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL_MENSAJES, mensaje, String.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
            return "Error al enviar el mensaje";
        }
    }

    public List<Usuario> obtenerProfesionales() {
        try {
            String url = "http://localhost:8081/usuarios/profesionales";
            Usuario[] respuesta = restTemplate.getForObject(url, Usuario[].class);
            return Arrays.asList(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Usuario> obtenerPacientes() {
        try {
            String url = "https://reset-app-q6h3.onrender.com/usuarios/profesionales";
            Usuario[] respuesta = restTemplate.getForObject(url, Usuario[].class);
            return Arrays.asList(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
