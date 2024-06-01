package com.teste.vm_tecnologia;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import com.teste.vm_tecnologia.service.UsuarioService;
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void salvarUsuarioComSucesso() throws UsuarioJaExisteException {
        Usuario usuario = Usuario.builder()
                .nome("Teste")
                .email("teste@teste.com")
                .senha("teste")
                .build();
        UsuarioEntradaDTO usuarioEntradaDTO = UsuarioEntradaDTO.from(usuario);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);
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
                .senha("teste")
                .build();
        UsuarioEntradaDTO usuarioEntradaDTO = UsuarioEntradaDTO.from(usuario);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);

        assertThrows(UsuarioJaExisteException.class, () -> usuarioService.save(usuarioEntradaDTO));
    }

    @Test
    public void buscarUsuarioPeloIdSucesso() throws UsuarioNaoExisteException {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail("test@test.com");

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        APIResponse<UsuarioSaidaDTO> response = usuarioService.findById(id);

        assertNotNull(response);
        assertEquals(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), response.getMessage());
    }

    @Test
    public void buscarUsuarioPeloIdErro() throws UsuarioNaoExisteException {
        Long id = 2L;

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoExisteException.class, () -> usuarioService.findById(id));
    }

    @Test
    public void buscarTodosUsuariosExiste() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Usuario> usuarios = Arrays.asList(
                new Usuario(), new Usuario(), new Usuario()
        );

        Page<Usuario> usuarioPagina = new PageImpl<>(usuarios, pageable, usuarios.size());

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(usuarioPagina);

        Page<UsuarioSaidaDTO> response = usuarioService.findAll(page, size);

        assertNotNull(response);
        assertEquals(usuarios.size(), response.getContent().size());
    }
}
