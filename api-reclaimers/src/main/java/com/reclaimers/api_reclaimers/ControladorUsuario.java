package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return repositorioUsuario.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario usuario) {
        Usuario usuarioExistente = repositorioUsuario.findByEmail(usuario.getEmail());
        Map<String, Object> response = new HashMap<>();

        if (usuarioExistente == null) {
            response.put("status", "error");
            response.put("message", "Correo incorrecto");
            return ResponseEntity.ok(response);
        }

        // Verificar contraseña encriptada
        boolean coincide = Encriptador.verificar(usuario.getContrasena(), usuarioExistente.getContrasena());

        if (!coincide) {
            response.put("status", "error");
            response.put("message", "Contraseña incorrecta");
            return ResponseEntity.ok(response);
        }

        response.put("status", "success");
        response.put("message", "Login exitoso");
        response.put("tipoUsuario", usuarioExistente.getTipoUsuario().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        Map<String, String> errores = new HashMap<>();

        // Validar nombre vacío
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            errores.put("nombre", "El nombre no puede estar vacío");
        }

        // Validar email vacío o sin formato
        if (usuario.getEmail() == null || !usuario.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errores.put("email", "El email no es válido");
        }

        // Validar contraseña vacía o muy corta
        if (usuario.getContrasena() == null || usuario.getContrasena().length() < 6) {
            errores.put("contrasena", "La contraseña debe tener al menos 6 caracteres");
        }

        // Validar correo repetido
        if (repositorioUsuario.findByEmail(usuario.getEmail()) != null) {
            errores.put("email", "El correo ya está registrado");
        }

        // Si hay errores, los devolvemos con un código 200 OK, pero con los mensajes de error
        if (!errores.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (Map.Entry<String, String> entry : errores.entrySet()) {
                errorMessages.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            return ResponseEntity.status(HttpStatus.OK).body(errorMessages.toString());  // Cambié el código de estado a 200 OK
        }

        // Tipo de usuario por defecto
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.LUDOPATA);
        }

        // Encriptar y guardar
        usuario.setContrasena(Encriptador.encriptar(usuario.getContrasena()));
        repositorioUsuario.save(usuario);

        return ResponseEntity.status(HttpStatus.OK).body("Usuario registrado correctamente como " + usuario.getTipoUsuario());  // Devolvemos con código 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        if (!repositorioUsuario.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        repositorioUsuario.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
