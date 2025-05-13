package com.reclaimers.api_reclaimers.repositorios;

import com.reclaimers.api_reclaimers.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RepositorioMensaje extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByEmisorIdOrReceptorId(Long emisorId, Long receptorId);
}

