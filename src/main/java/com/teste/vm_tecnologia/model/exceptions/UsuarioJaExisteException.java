package com.teste.vm_tecnologia.model.exceptions;

public class UsuarioJaExisteException extends Throwable {
    public UsuarioJaExisteException(String message) {
        super(message);
    }
}
