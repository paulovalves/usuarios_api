package com.teste.vm_tecnologia.controller;

import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.Usuario;
import com.teste.vm_tecnologia.model.exceptions.UsuarioExisteException;
import com.teste.vm_tecnologia.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

   @PostMapping("/salvar")
    public ResponseEntity<Object> save(@RequestBody Usuario usuario) {
        try {
            APIResponse<Usuario> response = usuarioService.save(usuario);
           return new ResponseEntity<>(response, HttpStatus.CREATED);
       } catch (UsuarioExisteException e) {
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
}
