package com.teste.vm_tecnologia.service;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UnauthorizedException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import com.teste.vm_tecnologia.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final SecurityUtils securityUtils;
    public UsuarioService(UsuarioRepository usuarioRepository, SecurityUtils securityUtils) {
        this.usuarioRepository = usuarioRepository;
        this.securityUtils = securityUtils;
    }


    @Transactional(rollbackFor = Exception.class)
    public APIResponse<UsuarioSaidaDTO> save(UsuarioEntradaDTO usuarioEntradaDTO) throws UsuarioJaExisteException {

        Usuario usuario = Usuario.builder()
                .nome(usuarioEntradaDTO.getNome())
                .email(usuarioEntradaDTO.getEmail())
                .senha(securityUtils.encodePassword(usuarioEntradaDTO.getSenha()))
                .build();
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && usuarioExistente.get().getEmail() != null) {
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
            System.err.println(e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public APIResponse<UsuarioSaidaDTO> findById(Long id, String authorizationHeader) throws UsuarioNaoExisteException, UnauthorizedException {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty() || usuario.get().getEmail() == null || usuario.get().getEmail().isEmpty()) {
                throw new UsuarioNaoExisteException(MessageEnum.USUARIO_NAO_ENCONTRADO.getMessage());
            }

            securityUtils.checkCreds(authorizationHeader);

            UsuarioSaidaDTO usuarioSaidaDTO = UsuarioSaidaDTO.from(usuario.get());
            return new APIResponse<>(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), usuarioSaidaDTO);
        } catch (UsuarioNaoExisteException e) {
            throw new UsuarioNaoExisteException(e.getMessage());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }



    public Page<UsuarioSaidaDTO> findAll(int page, int size, String authorizationHeader) throws UnauthorizedException {
        Pageable pageable = PageRequest.of(page, size);
        try {

            securityUtils.checkCreds(authorizationHeader);
            Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
            return usuarios.map(UsuarioSaidaDTO::from);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (Exception e) {
            System.err.println(MessageEnum.ERRO_BUSCAR_USUARIOS + e.getMessage());
            throw new RuntimeException(MessageEnum.ERRO_BUSCAR_USUARIOS.getMessage());
        }
    }


}
