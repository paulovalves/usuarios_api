package com.teste.vm_tecnologia.service;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
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
    public APIResponse<UsuarioSaidaDTO> save(UsuarioEntradaDTO usuarioEntradaDTO) throws UsuarioExisteException {

        Usuario usuario = mapToUsuario(usuarioEntradaDTO);
        Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            throw new UsuarioExisteException("Já existe um usuário cadastrado com este e-mail.");
        }
        try {
            Usuario response = usuarioRepository.save(usuario);
            UsuarioSaidaDTO usuarioSaidaDTO = mapToSaida(response);

            return new APIResponse<>("Usuário salvo com sucesso.", usuarioSaidaDTO);
        } catch (Exception e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
            throw new RuntimeException("Error ao salvar o usuario: " + e.getMessage());
        }
    }
    private Usuario mapToUsuario(UsuarioEntradaDTO usuarioEntradaDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioEntradaDTO.getNome());
        usuario.setEmail(usuarioEntradaDTO.getEmail());
        usuario.setSenha(usuarioEntradaDTO.getSenha());
        return usuario;
    }

    private UsuarioSaidaDTO mapToSaida(Usuario usuario) {
        UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
        usuarioSaidaDTO.setId(usuario.getId());
        usuarioSaidaDTO.setEmail(usuario.getEmail());
        usuarioSaidaDTO.setNome(usuario.getNome());
        return usuarioSaidaDTO;
    }
}
