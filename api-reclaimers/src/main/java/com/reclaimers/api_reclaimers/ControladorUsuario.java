package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {

    private final RepositorioUsuario repositorioUsuario;

    // Inyección por constructor
    @Autowired
    public ControladorUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return repositorioUsuario.findAll();
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Verificar si el correo ya está registrado
        if (repositorioUsuario.existsByEmail(usuario.getEmail())) {
            // Si el correo ya existe, devolver un error 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Asignar un tipo de usuario por defecto si no se recibe uno desde el frontend
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.LUDOPATA);  // Por defecto, asignamos LUDOPATA
        }

        // Guardar el nuevo usuario
        Usuario nuevoUsuario = repositorioUsuario.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        // Verificar si el usuario existe
        Usuario usuarioExistente = repositorioUsuario.findByEmail(usuario.getEmail());
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        // Verificar si la contraseña es correcta
        if (!usuarioExistente.getContrasena().equals(usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        // Aquí podemos devolver un mensaje personalizado según el tipo de usuario
        if (usuarioExistente.getTipoUsuario() == Usuario.TipoUsuario.LUDOPATA) {
            return ResponseEntity.status(HttpStatus.OK).body("Inicio de sesión exitoso como Ludópata");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Inicio de sesión exitoso como Profesional");
        }
    }
}
