package com.teste.vm_tecnologia;


import com.teste.vm_tecnologia.config.security.SecurityConfig;
import com.teste.vm_tecnologia.controller.UsuarioController;
import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
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

        Mockito.when(usuarioService.save(any(UsuarioEntradaDTO.class))).thenReturn(response);

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
}
