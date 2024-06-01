package com.teste.vm_tecnologia.dto;

import com.teste.vm_tecnologia.model.Usuario;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntradaDTO {
    private String nome;
    private String email;
    private String senha;

    public static UsuarioEntradaDTO from(Usuario usuario) {
        return UsuarioEntradaDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .build();
    }
}
