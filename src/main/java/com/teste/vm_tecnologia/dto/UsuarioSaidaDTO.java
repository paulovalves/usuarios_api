package com.teste.vm_tecnologia.dto;

import com.teste.vm_tecnologia.model.Usuario;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioSaidaDTO extends RepresentationModel<UsuarioSaidaDTO> {
    private Long id;
    private String nome;
    private String email;

    public UsuarioSaidaDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    public UsuarioSaidaDTO(Optional<Usuario> usuario) {
        this.id = usuario.map(Usuario::getId).orElse(null);
        this.nome = usuario.map(Usuario::getNome).orElse(null);
        this.email = usuario.map(Usuario::getEmail).orElse(null);
    }
}
