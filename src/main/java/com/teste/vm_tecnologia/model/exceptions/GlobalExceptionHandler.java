package com.teste.vm_tecnologia.model.exceptions;

import com.teste.vm_tecnologia.model.APIResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StringBuilder errorMessage = new StringBuilder("Erro ao salvar usuário. ");
        errors.forEach((field, message) -> errorMessage.append(message).append(" "));

        return new ResponseEntity<>(
                new APIResponse<>(errorMessage.toString().trim(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder("Erro ao salvar usuário. ");
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errorMessage.append(violation.getMessage()).append(" ");
        }

        return new ResponseEntity<>(
                new APIResponse<>(errorMessage.toString().trim(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ResponseEntity<APIResponse<Void>> handleUsuarioJaExisteException(UsuarioJaExisteException ex) {
        return new ResponseEntity<>(
                new APIResponse<>(ex.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(
                new APIResponse<>("Erro ao salvar usuário. " + ex.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

