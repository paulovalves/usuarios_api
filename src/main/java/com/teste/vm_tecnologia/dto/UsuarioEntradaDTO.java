package com.teste.vm_tecnologia.dto;

import com.teste.vm_tecnologia.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Classe que representa o DTO de entrada de um usuário.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntradaDTO {

    @NotNull(message = "O campo nome é obrigatório.")
    @NotEmpty(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    @NotNull(message = "O campo email é obrigatório.")
    @NotEmpty(message = "O campo email é obrigatório.")
    @NotBlank(message = "O campo email é obrigatório.")
    private String email;

    @NotNull(message = "O campo senha é obrigatório.")
    @NotEmpty(message = "O campo senha é obrigatório.")
    @NotBlank(message = "O campo senha é obrigatório.")
    private String senha;

    public static UsuarioEntradaDTO from(Usuario usuario) {
        return UsuarioEntradaDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .build();
    }
}
