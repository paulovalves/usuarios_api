package com.teste.vm_tecnologia.controller;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import com.teste.vm_tecnologia.dto.UsuarioSaidaDTO;
import com.teste.vm_tecnologia.model.APIResponse;
import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UnauthorizedException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioJaExisteException;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/salvar")
    @Operation(summary = "Cadastra um usuário", description = "Cadastra um novo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Usuário já existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar usuário.")
    })
    public ResponseEntity<APIResponse<UsuarioSaidaDTO>> save(@Valid @RequestBody UsuarioEntradaDTO usuarioEntradaDTO) {
        try {
            APIResponse<UsuarioSaidaDTO> response = usuarioService.save(usuarioEntradaDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            e.getMessage(),
                            null),
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException | UsuarioJaExisteException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            e.getMessage(),
                            null),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            e.getMessage(),
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Busca um usuário pelo id", description = "Busca um usuário pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.")
    })
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<APIResponse<UsuarioSaidaDTO>> findById(@PathVariable Long id, @RequestHeader("Authorization") String authorization){
        try {
            APIResponse<UsuarioSaidaDTO> response = usuarioService.findById(id, authorization);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsuarioNaoExisteException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            MessageEnum.ERRO_BUSCAR_USUARIO.getMessage(),
                            null
                    ),
                    HttpStatus.NOT_FOUND
            );
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            e.getMessage(),
                            null
                    ),
                    HttpStatus.UNAUTHORIZED
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
    @Operation(summary = "Busca todos os usuários", description = "Busca todos os usuários e retorna uma página.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuários não encontrados."),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.")
    })
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<APIResponse<Page<UsuarioSaidaDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestHeader("Authorization") String authorizationHeader
            ) {

        try {
            var response = usuarioService.findAll(page, size, authorizationHeader);
            return new ResponseEntity<>(
                    new APIResponse<>(
                            response.getContent().isEmpty() ?
                                    MessageEnum.LISTA_VAZIA.getMessage() :
                                    MessageEnum.SUCESSO_BUSCAR_USUARIOS.getMessage(),
                            response
                    ),
                    HttpStatus.OK
            );

        }  catch (UnauthorizedException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(
                            e.getMessage(),
                            null
                    ),
                    HttpStatus.UNAUTHORIZED
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
