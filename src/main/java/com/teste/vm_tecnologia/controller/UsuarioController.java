package com.teste.vm_tecnologia.controller;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


   @PostMapping("/salvar")
    public ResponseEntity<APIResponse<UsuarioSaidaDTO>> save(@RequestBody UsuarioEntradaDTO usuarioEntradaDTO) {
        try {
            APIResponse<UsuarioSaidaDTO> response = usuarioService.save(usuarioEntradaDTO);
           return new ResponseEntity<>(response, HttpStatus.CREATED);
       } catch (UsuarioJaExisteException e) {
           return new ResponseEntity<>(
                   new APIResponse<>(
                           e.getMessage(),
                            null),
                   HttpStatus.BAD_REQUEST);
       } catch (Exception e) {
           return new ResponseEntity<>(
                   new APIResponse<>(
                            "Erro ao salvar usuário.",
                            null),
                   HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @GetMapping("/buscar/{id}")
   public ResponseEntity<APIResponse<UsuarioSaidaDTO>> findById(@PathVariable Long id){
        try {
            APIResponse<UsuarioSaidaDTO> response = usuarioService.findById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsuarioNaoExisteException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            MessageEnum.ERRO_BUSCAR_USUARIO.getMessage(),
                            null
                    ),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            e.getMessage(),
                            null
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
   }

    @GetMapping("/buscar")
    public ResponseEntity<APIResponse<Page<UsuarioSaidaDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        try {
            var response = usuarioService.findAll(page, size);
            return new ResponseEntity<>(
                    new APIResponse<>(
                            response.getContent().isEmpty() ?
                                    MessageEnum.LISTA_VAZIA.getMessage() :
                            MessageEnum.SUCESSO_BUSCAR_USUARIOS.getMessage(),
                            response
                    ),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            MessageEnum.ERRO_BUSCAR_USUARIOS.getMessage(),
                            null
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
