package com.teste.vm_tecnologia.service;

import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.exceptions.UsuarioExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(rollbackFor = Exception.class)
    public APIResponse<Usuario> save(Usuario usuario) throws UsuarioExisteException {
            Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioExistente != null) {
                throw new UsuarioExisteException("Já existe um usuário cadastrado com este e-mail.");
            }
            try {
                return new APIResponse<>("Usuário salvo com sucesso.", usuarioRepository.save(usuario));
            } catch (Exception e) {
                System.err.println("Erro ao salvar usuário: " + e.getMessage());
                throw new RuntimeException("Error ao salvar o usuario: " + e.getMessage());
            }
    }
}
