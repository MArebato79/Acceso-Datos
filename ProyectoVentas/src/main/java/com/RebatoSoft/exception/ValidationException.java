package com.RebatoSoft.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> errores;

    public ValidationException(List<String> errores) {
        super("Errores de validación: " + String.join(", ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}