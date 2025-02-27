package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.Recurso;
import com.reclaimers.api_reclaimers.repositorios.RepositorioRecurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recursos")
public class ControladorRecurso {

    private final RepositorioRecurso repositorioRecurso;

    // Inyección por constructor
    @Autowired
    public ControladorRecurso(RepositorioRecurso repositorioRecurso) {
        this.repositorioRecurso = repositorioRecurso;
    }

    // Obtener todos los recursos
    @GetMapping
    public List<Recurso> obtenerRecursos() {
        return repositorioRecurso.findAll();
    }

    // Crear un nuevo recurso (solo profesionales pueden hacerlo)
    @PostMapping
    public ResponseEntity<Recurso> crearRecurso(@RequestBody Recurso recurso) {
        // Aquí podrías agregar la lógica para verificar que el usuario es un profesional
        // Por ejemplo, con un campo en el token JWT o sesión del usuario.

        // Guardar el nuevo recurso
        Recurso nuevoRecurso = repositorioRecurso.save(recurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRecurso);
    }
}