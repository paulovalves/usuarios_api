package com.teste.vm_tecnologia.repository;

import com.teste.vm_tecnologia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class UsuarioRepository implements JpaRepository<Usuario, Long> {
}
