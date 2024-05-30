package com.teste.vm_tecnologia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "usuarios", schema = "public")
public class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String email;

    @Column(name = "senha", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String senha;
}
