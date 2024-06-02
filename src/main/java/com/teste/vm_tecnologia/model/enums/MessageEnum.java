package com.teste.vm_tecnologia.model.enums;

public enum MessageEnum {
    USUARIO_EXISTE("Usuário já existe."),
    ERRO_SALVAR_USUARIO("Erro ao salvar usuário."),
    ERRO_BUSCAR_USUARIO("Erro ao buscar usuário."),
    SUCESSO_SALVAR_USUARIO("Usuário salvo com sucesso."),
    SUCESSO_BUSCAR_USUARIO("Usuário encontrado."),
    USUARIO_NAO_ENCONTRADO("Usuário não foi encontrado."),
    SUCESSO_BUSCAR_USUARIOS("Usuários encontrados."),
    ERRO_BUSCAR_USUARIOS("Erro ao buscar usuários."),
    LISTA_VAZIA("Nenhum usuário encontrado."),
    USUARIO_NAO_AUTORIZADO("Usuário não autorizado."),
    EMAIL_OBRIGATORIO("O campo email é obrigatório."),
    SENHA_OBRIGATORIA("O campo senha é obrigatório."),
    EMAIL_INVALIDO("Email inválido."),
    NOME_OBRIGATORIO("O campo nome é obrigatório.");
    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
