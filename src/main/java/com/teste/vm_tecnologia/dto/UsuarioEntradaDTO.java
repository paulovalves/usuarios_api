package com.teste.vm_tecnologia.dto;

import com.teste.vm_tecnologia.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntradaDTO {
    private String nome;
    private String email;
    private String senha;

    public UsuarioEntradaDTO(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
    }
}
