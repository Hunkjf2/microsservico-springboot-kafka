package com.example.pessoa.config.exception;

public class KafkaMessageException extends RuntimeException {
    public KafkaMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
