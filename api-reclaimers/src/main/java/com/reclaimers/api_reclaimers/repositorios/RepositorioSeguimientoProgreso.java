package com.reclaimers.api_reclaimers.repositorios;

import com.reclaimers.api_reclaimers.models.SeguimientoProgreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioSeguimientoProgreso extends JpaRepository<SeguimientoProgreso, Long> {

    // Método para obtener los seguimientos por usuario
    List<SeguimientoProgreso> findByUsuarioId(Long usuarioId);

}
