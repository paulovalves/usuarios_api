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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    public UsuarioService(UsuarioRepository usuarioRepository, UserDetailsService userDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userDetailsService = userDetailsService;
    }


    @Transactional(rollbackFor = Exception.class)
    public APIResponse<UsuarioSaidaDTO> save(UsuarioEntradaDTO usuarioEntradaDTO) throws UsuarioJaExisteException {

        Usuario usuario = Usuario.builder()
                .nome(usuarioEntradaDTO.getNome())
                .email(usuarioEntradaDTO.getEmail())
                .senha(passwordEncoder.encode(usuarioEntradaDTO.getSenha()))
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
            System.err.println(MessageEnum.ERRO_SALVAR_USUARIO + e.getMessage());
            throw new RuntimeException(MessageEnum.ERRO_SALVAR_USUARIO.getMessage());
        }
    }

    public APIResponse<UsuarioSaidaDTO> findById(Long id, String authorizationHeader) throws UsuarioNaoExisteException, UnauthorizedException {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty() || usuario.get().getEmail() == null || usuario.get().getEmail().isEmpty()) {
                throw new UsuarioNaoExisteException(MessageEnum.USUARIO_NAO_ENCONTRADO.getMessage());
            }

            checkCreds(authorizationHeader);

            UsuarioSaidaDTO usuarioSaidaDTO = UsuarioSaidaDTO.from(usuario.get());
            return new APIResponse<>(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), usuarioSaidaDTO);
        } catch (UsuarioNaoExisteException e) {
            throw new UsuarioNaoExisteException(e.getMessage());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    private void checkCreds(String authorizationHeader) throws UnauthorizedException {
        String[] credentials = extractCredentialsFromHeader(authorizationHeader);
        String email = credentials[0];
        String senha = credentials[1];
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(senha, userDetails.getPassword())) {
            throw new UnauthorizedException("senha inválido.");
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

    private String[] extractCredentialsFromHeader(String authHeader) throws UnauthorizedException {
        if(authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new UnauthorizedException(MessageEnum.USUARIO_NAO_AUTORIZADO.getMessage());
        }

        String base64Credentials = authHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCreds = new String(decodedBytes, StandardCharsets.UTF_8);
        return decodedCreds.split(":", 2);
    }
}
