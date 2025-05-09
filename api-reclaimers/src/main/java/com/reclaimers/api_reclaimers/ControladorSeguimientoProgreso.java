package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.SeguimientoProgreso;
import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.repositorios.RepositorioSeguimientoProgreso;
import com.reclaimers.api_reclaimers.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/seguimiento")
public class ControladorSeguimientoProgreso {

    private final RepositorioSeguimientoProgreso repositorioSeguimientoProgreso;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorSeguimientoProgreso(RepositorioSeguimientoProgreso repositorioSeguimientoProgreso, RepositorioUsuario repositorioUsuario) {
        this.repositorioSeguimientoProgreso = repositorioSeguimientoProgreso;
        this.repositorioUsuario = repositorioUsuario;
    }

    @PostMapping
    public ResponseEntity<String> crearSeguimiento(@RequestBody SeguimientoProgreso seguimientoProgreso) {
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(seguimientoProgreso.getUsuario().getId());

        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario no existe");
        }

        // Guardar el seguimiento si el usuario existe
        repositorioSeguimientoProgreso.save(seguimientoProgreso);
        return ResponseEntity.status(HttpStatus.OK).body("Seguimiento registrado correctamente");
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> obtenerSeguimientoPorUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(usuarioId);

        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario no existe");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(repositorioSeguimientoProgreso.findByUsuarioId(usuarioId));
    }

    @GetMapping("/usuario/{id}")
    public List<SeguimientoProgreso> obtenerPorUsuario(@PathVariable Long id) {
        return repositorioSeguimientoProgreso.findByUsuarioId(id);
    }

    @GetMapping("/todos")
    public List<SeguimientoProgreso> obtenerTodosLosSeguimientos() {
        return repositorioSeguimientoProgreso.findAll();
    }

}
