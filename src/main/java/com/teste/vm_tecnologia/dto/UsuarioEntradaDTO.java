package com.teste.vm_tecnologia.dto;

import com.teste.vm_tecnologia.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
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
