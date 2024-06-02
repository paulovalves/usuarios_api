package com.teste.vm_tecnologia;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UnauthorizedException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import com.teste.vm_tecnologia.service.UsuarioService;
import com.teste.vm_tecnologia.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityUtils securityUtils;

    private PasswordEncoder realPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Ensure the real PasswordEncoder is used for encoding in the test setup
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> realPasswordEncoder.encode(invocation.getArgument(0)));
        when(passwordEncoder.matches(anyString(), anyString())).thenAnswer(invocation ->
                realPasswordEncoder.matches(invocation.getArgument(0), invocation.getArgument(1))
        );
    }

    @Test
    public void salvarUsuarioComSucesso() throws UsuarioJaExisteException {
        Usuario usuario = Usuario.builder()
                .nome("Teste")
                .email("teste@teste.com")
                .senha("senha")
                .build();
        UsuarioEntradaDTO usuarioEntradaDTO = UsuarioEntradaDTO.from(usuario);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        APIResponse<UsuarioSaidaDTO> response = usuarioService.save(usuarioEntradaDTO);

        assertNotNull(response);
        assertEquals("Usuário salvo com sucesso.", response.getMessage());
    }

    @Test
    public void salvarUsuarioComEmailJaExistente() {
        Usuario usuario = Usuario.builder()
                .nome("Teste")
                .email("teste@teste.com")
                .senha("senha")
                .build();
        UsuarioEntradaDTO usuarioEntradaDTO = UsuarioEntradaDTO.from(usuario);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        assertThrows(UsuarioJaExisteException.class, () -> usuarioService.save(usuarioEntradaDTO));
    }

    @Test
    public void buscarUsuarioPeloIdSucesso() throws UsuarioNaoExisteException, UnauthorizedException {
        Long id = 1L;
        String plainPassword = "senha";
        String encodedPassword = realPasswordEncoder.encode(plainPassword);
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail("test@test.com");
        usuario.setSenha(encodedPassword);

        // Encode the email and password to simulate the authentication header
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("test@test.com:senha".getBytes());


        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(
                User.builder()
                        .username("test@test.com")
                        .password(encodedPassword)
                        .build()
        );
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        APIResponse<UsuarioSaidaDTO> response = usuarioService.findById(id, authHeader);

        assertNotNull(response);
        assertEquals(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), response.getMessage());
    }

    @Test
    public void buscarUsuarioPeloIdErro() throws UsuarioNaoExisteException {
        Long id = 2L;
        String authHeader = "Basic ZW1haWwyQGVtYWlsLmNvbTpzZW5oYQ==";

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoExisteException.class, () -> usuarioService.findById(id, authHeader));
    }

    @Test
    public void buscarTodosUsuariosExiste() throws UnauthorizedException {
        int page = 0;
        int size = 10;
        Long id = 1L;
        String plainPassword = "senha";
        String encodedPassword = realPasswordEncoder.encode(plainPassword);
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail("test@test.com");
        usuario.setSenha(encodedPassword);
        String authHeader = "Basic ZW1haWwyQGVtYWlsLmNvbTpzZW5oYQ==";
        Pageable pageable = PageRequest.of(page, size);
        List<Usuario> usuarios = Arrays.asList(
                new Usuario(), new Usuario(), new Usuario()
        );

        Page<Usuario> usuarioPagina = new PageImpl<>(usuarios, pageable, usuarios.size());
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(
                User.builder()
                        .username("test@test.com")
                        .password(encodedPassword)
                        .build()
        );
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(usuarioPagina);

        Page<UsuarioSaidaDTO> response = usuarioService.findAll(page, size, authHeader, null);

        assertNotNull(response);
        assertEquals(usuarios.size(), response.getContent().size());
    }
}
