package com.teste.vm_tecnologia.dto;

import com.teste.vm_tecnologia.model.Usuario;
import jdk.jshell.Snippet;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSaidaDTO {
    private Long id;
    private String nome;
    private String email;

    public static UsuarioSaidaDTO from(Usuario usuario) {
        return UsuarioSaidaDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }
}
