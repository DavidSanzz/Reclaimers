package com.reclaimers.api_reclaimers.repositorios;

import com.reclaimers.api_reclaimers.models.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioRecurso extends JpaRepository<Recurso, Long> {
    // Puedes agregar métodos personalizados, si lo necesitas.
}