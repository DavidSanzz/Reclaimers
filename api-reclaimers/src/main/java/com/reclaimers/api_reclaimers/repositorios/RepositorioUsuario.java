package com.reclaimers.api_reclaimers.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reclaimers.api_reclaimers.models.Usuario;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    // Método para verificar si el email ya existe
    boolean existsByEmail(String email);

    // Método para encontrar usuario por email
    Usuario findByEmail(String email);
}
