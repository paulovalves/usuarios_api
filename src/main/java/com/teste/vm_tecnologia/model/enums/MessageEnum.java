package com.teste.vm_tecnologia.model.enums;

public enum MessageEnum {
    USUARIO_EXISTE("Usuário já existe."),
    ERRO_SALVAR_USUARIO("Erro ao salvar usuário."),
    ERRO_BUSCAR_USUARIO("Erro ao buscar usuário."),
    SUCESSO_SALVAR_USUARIO("Usuário salvo com sucesso."),
    SUCESSO_BUSCAR_USUARIO("Usuário encontrado.");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }
}
