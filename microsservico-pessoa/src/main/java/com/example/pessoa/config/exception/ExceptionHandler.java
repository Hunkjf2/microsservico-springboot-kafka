package com.example.pessoa.config.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionHandler {
    public static ProcessingException handleException(Exception e) {
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            return new ProcessingException("Operação foi interrompida", e);
        }
        return new ProcessingException("Falha na comunicação", e);
    }
}