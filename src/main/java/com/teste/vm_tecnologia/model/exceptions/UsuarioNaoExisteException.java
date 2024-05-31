package com.teste.vm_tecnologia.model.exceptions;

public class UsuarioNaoExisteException extends Throwable{
    public UsuarioNaoExisteException(String message) {
        super(message);
    }
}
