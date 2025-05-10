package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.Recurso;
import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.repositorios.RepositorioRecurso;
import com.reclaimers.api_reclaimers.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recursos")
public class ControladorRecurso {

    private final RepositorioRecurso repositorioRecurso;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorRecurso(RepositorioRecurso repositorioRecurso, RepositorioUsuario repositorioUsuario) {
        this.repositorioRecurso = repositorioRecurso;
        this.repositorioUsuario = repositorioUsuario;
    }

    @GetMapping
    public List<Recurso> obtenerRecursos() {
        return repositorioRecurso.findAll();
    }

    @PostMapping
    public ResponseEntity<Recurso> crearRecurso(@RequestBody Recurso recurso) {
        Long profesionalId = recurso.getUsuarioProfesional().getId();

        Optional<Usuario> profesionalOpt = repositorioUsuario.findById(profesionalId);

        if (profesionalOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        recurso.setUsuarioProfesional(profesionalOpt.get());

        Recurso nuevoRecurso = repositorioRecurso.save(recurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRecurso);
    }
}
