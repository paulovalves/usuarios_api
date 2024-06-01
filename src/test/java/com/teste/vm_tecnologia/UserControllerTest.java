package com.teste.vm_tecnologia;


import com.teste.vm_tecnologia.config.security.SecurityConfig;
import com.teste.vm_tecnologia.controller.UsuarioController;
import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.rmi.ServerRuntimeException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void SalvarUsuarioComSucesso() throws Exception, UsuarioJaExisteException {
        UsuarioEntradaDTO usuarioEntradaDTO = new UsuarioEntradaDTO();
        usuarioEntradaDTO.setNome("Teste");
        usuarioEntradaDTO.setEmail("teste@teste.com");
        usuarioEntradaDTO.setSenha("teste");
        UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
        APIResponse<UsuarioSaidaDTO> response = new APIResponse<>("Usuário criado com sucesso.", usuarioSaidaDTO);

        when(usuarioService.save(any(UsuarioEntradaDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuario/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "nome": "Teste",
                            "email": "teste@teste.com",
                            "senha": "teste"
                        }"""))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Usuário criado com sucesso."));
    }

    @Test
    public void SalvarUsuarioComFalhaRetornaBadRequest() throws UsuarioJaExisteException, Exception {
        UsuarioEntradaDTO usuarioEntradaDTO = new UsuarioEntradaDTO();
        usuarioEntradaDTO.setNome("Teste");
        usuarioEntradaDTO.setEmail("teste@teste.com");
        usuarioEntradaDTO.setSenha("teste");

        when(usuarioService.save(any(UsuarioEntradaDTO.class))).thenThrow(new UsuarioJaExisteException(MessageEnum.ERRO_SALVAR_USUARIO.getMessage()));

        mockMvc.perform(post("/api/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Teste",
                            "email": "teste@teste.com",
                            "senha": "teste"
                        }"""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MessageEnum.ERRO_SALVAR_USUARIO.getMessage())
        );
    }

    @Test
    public void SalvarUsuarioComFalhaRetornaInteralServerError() throws UsuarioJaExisteException, Exception {
        UsuarioEntradaDTO usuarioEntradaDTO = new UsuarioEntradaDTO();
        usuarioEntradaDTO.setNome("Teste");
        usuarioEntradaDTO.setEmail("teste@teste.com");
        usuarioEntradaDTO.setSenha("teste");

        when(usuarioService.save(any(UsuarioEntradaDTO.class))).thenThrow(
                new RuntimeException(MessageEnum.ERRO_SALVAR_USUARIO.getMessage())
        );

        mockMvc.perform(post("/api/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "nome": "Teste",
                        "email": "teste@teste.com",
                        "senha": "teste"
                    }"""))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MessageEnum.ERRO_SALVAR_USUARIO.getMessage()));
    }

    @Test
    public void buscarUsuarioPeloIdSucesso() throws UsuarioNaoExisteException, Exception {
        Long id = 1L;
        UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
        usuarioSaidaDTO.setId(id);
        usuarioSaidaDTO.setEmail("teste@teste.com");

        APIResponse<UsuarioSaidaDTO> response = new APIResponse<>(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage(), usuarioSaidaDTO);
        when(usuarioService.findById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/usuario/buscar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MessageEnum.SUCESSO_BUSCAR_USUARIO.getMessage()));


    }

    @Test
    public void buscarUsuarioPeloIdFalhaNaoEncontrado() throws UsuarioNaoExisteException, Exception {
        Long id = 2L;

        when(usuarioService.findById(anyLong())).thenThrow(new UsuarioNaoExisteException(MessageEnum.ERRO_BUSCAR_USUARIO.getMessage()));

        mockMvc.perform(get("/api/usuario/buscar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MessageEnum.ERRO_BUSCAR_USUARIO.getMessage()));

    }

    @Test
    public void buscarTodosUsuariosSucesso() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Usuario> usuarios = Arrays.asList(
                new Usuario(), new Usuario(), new Usuario()
        );

        Page<Usuario> response = new PageImpl<>(usuarios, pageable, usuarios.size());
        var saida = response.map(UsuarioSaidaDTO::new);
        when(usuarioService.findAll(anyInt(),anyInt())).thenReturn(saida);

        mockMvc.perform(get("/api/usuario/buscar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MessageEnum.SUCESSO_BUSCAR_USUARIOS.getMessage()));
    }

    @Test
    public void buscarTodosUsuariosListaVazia() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Usuario> usuarios = List.of();

        Page<Usuario> response = new PageImpl<>(usuarios, pageable, 0);
        var saida = response.map(UsuarioSaidaDTO::new);
        when(usuarioService.findAll(anyInt(), anyInt())).thenReturn(saida);

        mockMvc.perform(get("/api/usuario/buscar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MessageEnum.LISTA_VAZIA.getMessage()));

    }


}
