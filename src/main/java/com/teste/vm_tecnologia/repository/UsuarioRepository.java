package com.teste.vm_tecnologia.repository;

import com.teste.vm_tecnologia.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
