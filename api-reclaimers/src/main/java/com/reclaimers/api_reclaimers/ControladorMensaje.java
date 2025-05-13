package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.Mensaje;
import com.reclaimers.api_reclaimers.repositorios.RepositorioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mensajes")
@CrossOrigin(origins = "*")
public class ControladorMensaje {

    @Autowired
    private RepositorioMensaje repositorioMensaje;

    // Enviar un nuevo mensaje con validaciones
    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody Mensaje mensaje) {
        if (mensaje.getEmisorId() == null || mensaje.getReceptorId() == null) {
            return ResponseEntity.badRequest().body("El emisor y el receptor deben estar definidos.");
        }

        if (mensaje.getContenido() == null || mensaje.getContenido().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El contenido del mensaje no puede estar vacío.");
        }

        if (mensaje.getEmisorId().equals(mensaje.getReceptorId())) {
            return ResponseEntity.badRequest().body("No puedes enviarte mensajes a ti mismo.");
        }

        mensaje.setFechaEnvio(LocalDateTime.now());
        Mensaje guardado = repositorioMensaje.save(mensaje);
        return ResponseEntity.ok(guardado);
    }

    // Obtener todos los mensajes de un usuario (como emisor o receptor)
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> obtenerMensajesDeUsuario(@PathVariable Long usuarioId) {
        List<Mensaje> mensajes = repositorioMensaje.findByEmisorIdOrReceptorId(usuarioId, usuarioId);
        return ResponseEntity.ok(mensajes);
    }
}
