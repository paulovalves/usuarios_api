package com.teste.vm_tecnologia.model;

import com.teste.vm_tecnologia.dto.UsuarioEntradaDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe que representa a entidade Usuario.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios", schema = "public")
@Valid
public class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @NotNull(message = "O campo nome não pode ser nulo.")
    @NotEmpty(message = "O campo nome não pode ser vazio.")
    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Email(message = "O campo email deve ser um endereço de e-mail válido.")
    @NotNull(message = "O campo email não pode ser nulo.")
    @NotEmpty(message = "O campo email não pode ser vazio.")
    @NotBlank(message = "O campo email é obrigatório.")
    private String email;

    @Column(name = "senha", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @NotNull(message = "O campo senha não pode ser nulo.")
    @NotEmpty(message = "O campo senha não pode ser vazio.")
    @NotBlank(message = "O campo senha é obrigatório.")
    private String senha;

    public static Usuario from(UsuarioEntradaDTO usuarioEntradaDTO) {
        return Usuario.builder()
                .nome(usuarioEntradaDTO.getNome())
                .email(usuarioEntradaDTO.getEmail())
                .senha(usuarioEntradaDTO.getSenha())
                .build();
    }

}
