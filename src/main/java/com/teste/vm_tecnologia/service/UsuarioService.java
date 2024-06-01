package com.teste.vm_tecnologia.service;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(rollbackFor = Exception.class)
    public APIResponse<UsuarioSaidaDTO> save(UsuarioEntradaDTO usuarioEntradaDTO) throws UsuarioJaExisteException {

        Usuario usuario = Usuario.builder()
                .nome(usuarioEntradaDTO.getNome())
                .email(usuarioEntradaDTO.getEmail())
                .senha(usuarioEntradaDTO.getSenha())
                .build();
        Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            throw new UsuarioJaExisteException(MessageEnum.USUARIO_EXISTE.getMessage());
        }
        try {
            Usuario response = usuarioRepository.save(usuario);
            UsuarioSaidaDTO usuarioSaidaDTO = UsuarioSaidaDTO.builder()
                    .id(response.getId())
                    .nome(response.getNome())
                    .email(response.getEmail())
                    .build();

            return new APIResponse<>("Usuário salvo com sucesso.", usuarioSaidaDTO);
        } catch (Exception e) {
            System.err.println(MessageEnum.ERRO_SALVAR_USUARIO + e.getMessage());
            throw new RuntimeException(MessageEnum.ERRO_SALVAR_USUARIO.getMessage());
        }
    }

    public APIResponse<UsuarioSaidaDTO> findById(Long id) throws UsuarioNaoExisteException {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty() || usuario.get().getEmail() == null || usuario.get().getEmail().isEmpty()) {
                throw new UsuarioNaoExisteException(MessageEnum.USUARIO_NAO_ENCONTRADO.getMessage());
            }
            UsuarioSaidaDTO usuarioSaidaDTO = UsuarioSaidaDTO.from(usuario.get());
            return new APIResponse<>(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), usuarioSaidaDTO);
        } catch (UsuarioNaoExisteException e) {
            throw new UsuarioNaoExisteException(e.getMessage());
        }
    }

    public Page<UsuarioSaidaDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
            return usuarios.map(UsuarioSaidaDTO::from);
        } catch (Exception e) {
            System.err.println(MessageEnum.ERRO_BUSCAR_USUARIOS + e.getMessage());
            throw new RuntimeException(MessageEnum.ERRO_BUSCAR_USUARIOS.getMessage());
        }
    }
}
