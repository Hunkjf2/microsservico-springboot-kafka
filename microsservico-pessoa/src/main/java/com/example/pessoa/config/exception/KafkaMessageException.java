package com.example.pessoa.config.exception;

public class KafkaMessageException extends RuntimeException {

    public KafkaMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaMessageException(String message) {
        super(message);
    }

    /**
     * Verifica se a causa raiz é uma InterruptedException
     */
    public boolean isInterrupted() {
        Throwable cause = getCause();
        return cause instanceof InterruptedException;
    }
}
