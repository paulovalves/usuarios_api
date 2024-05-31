package com.teste.vm_tecnologia.service;

import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(rollbackFor = Exception.class)
    public Usuario save(Usuario usuario) {
        try {
            Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioExistente != null) {
                throw new Exception("Já existe um usuário cadastrado com este e-mail.");
            }
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
            throw new RuntimeException("Error ao salvar o usuario: " + e.getMessage());
        }
    }
}
