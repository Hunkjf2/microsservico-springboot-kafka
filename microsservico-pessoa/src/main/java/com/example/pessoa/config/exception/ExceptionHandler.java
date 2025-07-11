package com.example.pessoa.config.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ExceptionHandler {
    public static <T> T handleAndThrow(String topic, Exception e) {
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            log.error("Thread interrompida durante comunicação com tópico '{}'", topic);
            throw new ProcessingException("Operação foi interrompida", e);
        }
        log.error("Erro na comunicação síncrona com tópico '{}': {}", topic, e.getMessage());
        throw new ProcessingException("Falha na comunicação", e);
    }
}