package com.teste.vm_tecnologia;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UserNotFoundException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import com.teste.vm_tecnologia.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void salvarUsuarioComSucesso() throws UsuarioNaoExisteException {
        UsuarioEntradaDTO usuarioEntradaDTO = new UsuarioEntradaDTO();
        usuarioEntradaDTO.setNome("Teste");
        usuarioEntradaDTO.setEmail("teste@teste.com");
        usuarioEntradaDTO.setSenha("teste");

        Usuario usuario = mapToUsuario(usuarioEntradaDTO);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        APIResponse<UsuarioSaidaDTO> response = usuarioService.save(usuarioEntradaDTO);

        assertNotNull(response);
        assertEquals("Usuário salvo com sucesso.", response.getMessage());
    }

    @Test
    public void salvarUsuarioComEmailJaExistente() {
        UsuarioEntradaDTO usuarioEntradaDTO = new UsuarioEntradaDTO();
        usuarioEntradaDTO.setNome("Teste");
        usuarioEntradaDTO.setEmail("teste@teste.com");
        usuarioEntradaDTO.setSenha("teste");

        Usuario usuario = mapToUsuario(usuarioEntradaDTO);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);

        assertThrows(UsuarioNaoExisteException.class, () -> usuarioService.save(usuarioEntradaDTO));
    }

    @Test
    public void buscarUsuarioPeloIdSucesso() throws UserNotFoundException {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        APIResponse<UsuarioSaidaDTO> response = usuarioService.findById(id);

        assertNotNull(response);
        assertEquals(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), response.getMessage());
    }
    private Usuario mapToUsuario(UsuarioEntradaDTO usuarioEntradaDTO) {
        return new Usuario(
                usuarioEntradaDTO.getNome(),
                usuarioEntradaDTO.getEmail(),
                usuarioEntradaDTO.getSenha()
        );
    }
}
